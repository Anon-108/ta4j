package ta4jexamples.mytest._20240821;

import com.alibaba.excel.EasyExcel;
import org.ta4j.core.*;
import org.ta4j.core.analysis.cost.CostModel;
import org.ta4j.core.analysis.cost.FixedTransactionCostModel;
import org.ta4j.core.analysis.cost.ZeroCostModel;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.*;
import org.ta4j.core.utils.BarSeriesUtils;
import ta4jexamples.mytest.EmaV3Indicator;
import ta4jexamples.mytest._20240816.MyOrder;
import ta4jexamples.mytest._20240816.MyOrderExcel;
import ta4jexamples.mytest._20240817.OrderRecord;
import ta4jexamples.mytest._240818.ExcelReader;
import ta4jexamples.mytest._240818.MyTradingRecordAnalysis;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

public class TestEmaS1ToH1_RSI_2_4 {
    private static  Map trade = null;
    private static List<MyOrder> orders = null;
    private static Map<String,Integer> emaStatus = null;
    private static  String backtestFile = null;
    private static   Integer rsiBarCount  = null;
    private static   Integer barCount1  = null;
    private static   Integer barCount2  = null;
    private static   Integer fileNum  = 0;
    public static void main(String[] args) throws IOException {


        BarSeries series = BarSeriesUtils.buildBinanceDataBig(-1, null);

        Bar firstBar = series.getFirstBar();//2024-08-12T21:34:46.999+08:00[Asia/Shanghai]
        Bar lastBar = series.getLastBar();


        int timeFrame = 60 * 60 ; //时间框架 基于k线级别：1分钟：60=1小时
        ExcelReader excelReader = new ExcelReader();
        String writeFileName = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\randomEmaTest\\RandomEmaTestRSI_2_file2_筛选后的指标.xlsx"; // 文件路径
        for (OrderRecord orderRecord : excelReader.getOrderRecords(writeFileName)) {
            barCount1 = orderRecord.getBarCount1();
            barCount2 = orderRecord.getBarCount2();
            rsiBarCount = 2;
            testTrade (series,barCount1,barCount2,100,timeFrame);
        }


    }


    public static void testTrade (BarSeries series,int barCount1,int barCount2,int startKline,int timeFrame) throws IOException {


//===================================策略回测 开始================================================
//        int timeFrame = 60; //时间框架

        trade = new HashMap<String,Boolean>();
        orders = new ArrayList<>();
        emaStatus = new HashMap<>();

        BaseBarSeries myBackTest = new BaseBarSeries("myBackTesting");
        EmaV3Indicator closeLine = new EmaV3Indicator(new ClosePriceIndicator(myBackTest), barCount1);
        EmaV3Indicator openLine = new EmaV3Indicator(new OpenPriceIndicator(myBackTest), barCount2);
        List<Bar> timeBars = new ArrayList<>();


        CrossedUpIndicatorRule upIndicatorRule = new CrossedUpIndicatorRule(closeLine, openLine);
        CrossedDownIndicatorRule downIndicatorRule = new CrossedDownIndicatorRule(closeLine, openLine);

        // 开多仓规则：当 closeLine 上穿 openLine 时
        Rule entryLongRule = new OverIndicatorRule(closeLine, openLine);
        // 平多仓规则：当 closeLine 下穿 openLine 且 ifShort 为 false 时
        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine);

        // 开空仓规则：当 closeLine 下穿 openLine 且 ifShort 为 true 时
        Rule entryShortRule = new UnderIndicatorRule(closeLine, openLine);
        // 平空仓规则：当 closeLine 上穿 openLine
        Rule exitShortRule = new OverIndicatorRule(closeLine, openLine);

        RSIIndicator rsi = new RSIIndicator(new ClosePriceIndicator(myBackTest), rsiBarCount);

        Rule longEntry = entryLongRule.and(new CrossedDownIndicatorRule(rsi, 5));
        Strategy longStrategy = new BaseStrategy(longEntry, new BooleanRule(false));

        Rule shortRule = entryShortRule.and(new CrossedUpIndicatorRule(rsi, 95));
        Strategy shortStrategy = new BaseStrategy(shortRule, new BooleanRule(false));

        CostModel costModel = new FixedTransactionCostModel((0.003 / 100));
        ZeroCostModel zeroCostModel = new ZeroCostModel();
        TradingRecord longTradingRecord = new BaseTradingRecord(Trade.TradeType.BUY, costModel,zeroCostModel);
        TradingRecord shortTradingRecord = new BaseTradingRecord(Trade.TradeType.SELL, costModel,zeroCostModel);

        // 创建示例数据
        List<MyOrderExcel> writeData = new ArrayList<>();

        wrapKlineByTimeFrame(series, startKline, timeFrame, myBackTest, timeBars, longStrategy,shortStrategy,longTradingRecord,shortTradingRecord, upIndicatorRule,downIndicatorRule);


        Num profit = DecimalNum.ZERO;
        double profitQty = 0;
        double lossesQty = 0;

        for (int i = 0; i < orders.size(); i++) {
            MyOrder order = orders.get(i);
            Num num = order.getProfit();
            if (num.isGreaterThan(DecimalNum.ZERO)){
                ++profitQty;
            }else if (num.isLessThan(DecimalNum.ZERO)){
                ++lossesQty;
            }
            profit = profit.plus(num);
            MyOrderExcel orderExcel = new MyOrderExcel(order.getTradeType(), order.getOrderTime(), order.getOrderPrice().doubleValue(), order.getKlinePrice().doubleValue()
                    , order.getProfit().doubleValue(), order.getOrderEma().doubleValue(), order.getOpenEma().doubleValue(), order.getCloseEma().doubleValue());
            writeData.add(orderExcel);

        }

        List<MyTradingRecordAnalysis> analysisList = analyzeTradingRecords(orders);
        analysisList.sort(Comparator.comparingInt(MyTradingRecordAnalysis::getStatus).thenComparing(MyTradingRecordAnalysis::getCount).reversed());

        int index = 0;
        for (int i = 0; i < analysisList.size(); i++) {
            MyTradingRecordAnalysis analysis = analysisList.get(i);
            MyOrderExcel orderExcel = writeData.get(index);
            if (analysis.getStatus() == 0){
                orderExcel.setMaxProfitCount(analysis.getCount());
                orderExcel.setStartingProfitTime(analysis.getStartTime());
                ++index;
            }
        }
        index = 0;
        for (int i = 0; i < analysisList.size(); i++) {
            MyTradingRecordAnalysis analysis = analysisList.get(i);
            MyOrderExcel orderExcel = writeData.get(index);
            if (analysis.getStatus() == 1){
                orderExcel.setMaxLossesCount(analysis.getCount());
                orderExcel.setStartingTimeOfLoss(analysis.getStartTime());
                ++index;
            }
        }

        double proportion = profitQty / (profitQty + lossesQty) * 100;
        System.out.println("交易总数:"+orders.size()+" ,盈利数量:"+profitQty+" ,亏损数量:"+lossesQty+" ,利润："+profit+" 胜率:"+proportion);
        MyOrderExcel order = writeData.get(0);
        order.setCount(orders.size());
        order.setProfitQty((int)profitQty);
        order.setLossesQty((int)lossesQty);
        order.setCountProfit(profit.doubleValue());
        order.setProportion(proportion);
        order.setRsiBarCount(rsiBarCount);
        order.setEmaBarCount1(TestEmaS1ToH1_RSI_2_4.barCount1);
        order.setEmaBarCount2(TestEmaS1ToH1_RSI_2_4.barCount2);


        // 使用EasyExcel将数据写入Excel文件
//        backtestFile = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\randomEmaTest\\_20240821\\ETHUSDT\\" +
//                "EmaTestRSI_2_backtest_barCount1_"+TestEmaS1ToH1_RSI_2_2.barCount1+"_barCount2_"+TestEmaS1ToH1_RSI_2_2.barCount2+"_胜率："+proportion+".xlsx";
        String symble = "ETHUSDT";
        backtestFile = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\randomEmaTest\\_20240822\\"+symble+"\\" +
                symble+"_backtest_胜率："+proportion+(++fileNum)+".xlsx";
        EasyExcel.write(backtestFile, MyOrderExcel.class).sheet("数据表").doWrite(writeData);
        System.out.println("写出文件:+"+backtestFile);

//        if (profit.doubleValue() >= 0 && proportion >= 49){
//            OrderRecord orderRecord = new OrderRecord(orders.size(), profitQty, lossesQty, profit.doubleValue(), proportion,barCount1,barCount2);
//            orderRecords.add(orderRecord);
//        }
//        OrderRecord orderRecord = new OrderRecord(orders.size(), profitQty, lossesQty, profit.doubleValue(), proportion,barCount1,barCount2);
//        orderRecords.add(orderRecord);

    }

    /**
     * 通过自定义时间框架级别包装时间 ： 1分钟---> 1 小时
     * @param series 原始bar序列
     * @param startKline 指标开始最少前置k线
     * @param timeFrame 时间框架
     * @param myBackTest 测算指标bar序列
     * @param timeBars 基于时间框架收集bar
     * @param upIndicatorRule
     * @param downIndicatorRule
     */
    private static void wrapKlineByTimeFrame(BarSeries series, int startKline, int timeFrame, BaseBarSeries myBackTest, List<Bar> timeBars,
                                             Strategy longStrategy, Strategy shortStrategy,
                                             TradingRecord longTradingRecord, TradingRecord shortTradingRecord,
                                             CrossedUpIndicatorRule upIndicatorRule,CrossedDownIndicatorRule downIndicatorRule) {
        //        List<MyOrder> myOrders = new ArrayList<>();
        ZonedDateTime time = ZonedDateTime.parse("2024-04-30T09:00:00+08:00[Asia/Shanghai]");
        for (int start = 0; start < series.getBarCount(); start++) {

            if (series.getBar(start).getBeginTime().isBefore(time)){
                continue;
            }

            if (myBackTest.getBarData().size() < startKline){//历史k线不能少于100
                Bar openBar = series.getBar(start); //获取第一根bar为开盘
                start = start + timeFrame - 1;
                Bar closeBar = series.getBar(start); //获取时间范围最后一根bar为收盘
                BaseBar baseBar = new BaseBar(Duration.ofHours(1),openBar.getBeginTime(),closeBar.getEndTime(),openBar.getOpenPrice(), DecimalNum.ZERO,DecimalNum.ZERO,closeBar.getClosePrice(),DecimalNum.ZERO,DecimalNum.ZERO,0l);
                myBackTest.addBar(baseBar);
                continue;
            }
            Bar barData = series.getBar(start);
            if (timeBars.isEmpty()){ //如果当前为0表示时间段开始第一个bar
                timeBars.add(barData);
                BaseBar baseBar = new BaseBar(Duration.ofHours(1),barData.getBeginTime(),barData.getEndTime(),barData.getOpenPrice(), DecimalNum.ZERO,DecimalNum.ZERO,barData.getClosePrice(),DecimalNum.ZERO,DecimalNum.ZERO,0l);
                myBackTest.addBar(baseBar);
                int endIndex = myBackTest.getEndIndex();

                //回测
                backTest(longStrategy,shortStrategy, longTradingRecord,shortTradingRecord,upIndicatorRule, downIndicatorRule, barData,endIndex);
            }else {
                //将第一条的bar结束时间和收盘价格设置为当前最新bar
                timeBars.add(barData);

                myBackTest.getLastBar().setEndTime(barData.getEndTime());
                myBackTest.getLastBar().setClosePrice(barData.getClosePrice());

                int endIndex = myBackTest.getEndIndex();
                //回测
                backTest(longStrategy,shortStrategy, longTradingRecord,shortTradingRecord,upIndicatorRule, downIndicatorRule,barData,endIndex);

                if (timeBars.size() == timeFrame){  //如果当前已经存储满 时间框架，则清空 开始下一个时间
                    timeBars.clear();
                }
            }
        }
    }

    /**
     * 回测
     * @param longStrategy 开多策略
     * @param shortStrategy 开空策略
     * @param longTradingRecord 开多订单记录
     * @param shortTradingRecord 开空订单记录
     * @param crossUpRule 金叉指标
     * @param crossDownRule 死叉指标
     * @param index 当前索引
     */
    private static void backTest(Strategy longStrategy, Strategy shortStrategy, TradingRecord longTradingRecord, TradingRecord shortTradingRecord,
                                 CrossedUpIndicatorRule crossUpRule, CrossedDownIndicatorRule crossDownRule,Bar bar,int index) {
        //止盈亏比例
        Num takeProfitRate = DecimalNum.valueOf(1.5).dividedBy(DecimalNum.valueOf(1000)); //十倍杠杆
        Num stopLossRate = DecimalNum.valueOf(1).dividedBy(DecimalNum.valueOf(1000));

        //多头
        Trade longEntry = longTradingRecord.getCurrentPosition().getEntry();
        //空头
        Trade shortEntry = shortTradingRecord.getCurrentPosition().getEntry();

        boolean close = false;
        if (Objects.nonNull(longEntry)){
//            Num entryLongValue = entryLong.getValue(); //交易的价值  金额 * 开仓价 * 数量(默认：1)
//            Num amount = entryLong.getAmount(); // 交易的金额
            Num orderPrice = longEntry.getPricePerAsset();

            //当前价格 大于等于 开仓止盈/小于等于 止损 则平多
            Num plusPrice = orderPrice.multipliedBy(DecimalNum.valueOf(1).plus(takeProfitRate));//多头止盈价格
            Num minusPrice = orderPrice.multipliedBy(DecimalNum.valueOf(1).minus(stopLossRate));//多头止损价格
            if (bar.getClosePrice().isGreaterThanOrEqual(plusPrice) || bar.getClosePrice().isLessThanOrEqual(minusPrice)){
                close = true;
            }
        }else if (Objects.nonNull(shortEntry)){
            Num orderPrice = shortEntry.getPricePerAsset();

            //当前价格 大于等于 开仓止盈/小于等于 止损 则平多
            Num plusPrice = orderPrice.multipliedBy(DecimalNum.valueOf(1).plus(stopLossRate));//空头止损价格
            Num minusPrice = orderPrice.multipliedBy(DecimalNum.valueOf(1).minus(takeProfitRate));//空头止盈价格
            if (bar.getClosePrice().isGreaterThanOrEqual(plusPrice) || bar.getClosePrice().isLessThanOrEqual(minusPrice)){
                close = true;
            }
        }

        /*交叉信号指标*/
        boolean isCrossUp = crossUpRule.isSatisfied(index-1); //金叉指标
        boolean isCrossDown = crossDownRule.isSatisfied(index-1); //死叉指标

        //开多：当前是多头指标并且金叉指标达成 rsi 超卖
        if (longStrategy.shouldEnter(index) && isCrossUp){
            //如果当前有空单则先平空单
            if (Objects.nonNull(shortEntry)){
                boolean exited = shortTradingRecord.exit(index, bar.getClosePrice(), DecimalNum.valueOf(10));
                if (exited){
                    Num result = shortEntry.getPricePerAsset().minus(bar.getClosePrice()); //开空时的价格
                    MyOrder order = new MyOrder("平空", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), result,  DecimalNum.ZERO,  DecimalNum.ZERO,  DecimalNum.ZERO);
                    orders.add(order);
                }
            }

            //如果当前 有了多单这不开
            boolean entered = longTradingRecord.enter(index, bar.getClosePrice(), DecimalNum.valueOf(10));
            if (entered){
                MyOrder order = new MyOrder("开多", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), DecimalNum.ZERO,  DecimalNum.ZERO,  DecimalNum.ZERO,  DecimalNum.ZERO);
                orders.add(order);
            }
        }else if (close){
            //如果当前 没有了多单则不平
            boolean exited = longTradingRecord.exit(index, bar.getClosePrice(), DecimalNum.valueOf(10));
            if (exited){
                Num result = longEntry.getPricePerAsset().minus(bar.getClosePrice()); //开多时的价格
                MyOrder order = new MyOrder("平多", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), result,  DecimalNum.ZERO,  DecimalNum.ZERO,  DecimalNum.ZERO);
                orders.add(order);
            }

        }

        //开空：当前是空头指标并且死叉指标达成  rsi 超买
        if (shortStrategy.shouldEnter(index) && isCrossDown ){
            //如果当前 有了多单则先平多
            if (Objects.nonNull(longEntry)){
                boolean exited = longTradingRecord.exit(index, bar.getClosePrice(), DecimalNum.valueOf(10));
                if (exited){
                    Num result = longEntry.getPricePerAsset().minus(bar.getClosePrice()); //开多时的价格
                    MyOrder order = new MyOrder("平多", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), result,  DecimalNum.ZERO,  DecimalNum.ZERO,  DecimalNum.ZERO);
                    orders.add(order);
                }
            }

            boolean entered = shortTradingRecord.enter(index, bar.getClosePrice(), DecimalNum.valueOf(10));
            if (entered){
                MyOrder order = new MyOrder("开空", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), DecimalNum.ZERO, DecimalNum.ZERO,  DecimalNum.ZERO,  DecimalNum.ZERO);
                orders.add(order);
            }
        }else if(close){
            //如果当前 没有了空单则不开
            boolean exited = shortTradingRecord.exit(index, bar.getClosePrice(), DecimalNum.valueOf(10));
            if (exited){
                Num result = shortEntry.getPricePerAsset().minus(bar.getClosePrice()); //开空时的价格
                MyOrder order = new MyOrder("平空", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), result,  DecimalNum.ZERO,  DecimalNum.ZERO,  DecimalNum.ZERO);
                orders.add(order);
            }
        }
    }


    public static List<MyTradingRecordAnalysis>  analyzeTradingRecords(List<MyOrder> orders) {
        ArrayList<MyTradingRecordAnalysis> tradingRecordAnalyses = new ArrayList<>();
        MyTradingRecordAnalysis analysis = new MyTradingRecordAnalysis();
        for (int i = 0; i < orders.size(); i++) {
            MyOrder order = orders.get(i);
            double profit = order.getProfit().doubleValue();

            if (order.getTradeType().equals("开多") || order.getTradeType().equals("开空")){
                continue;
            }

            if (tradingRecordAnalyses.size() == 0) {
                if (profit > 0){
                    analysis.setCount(1);
                    analysis.setStartTime(order.getOrderTime());
                    tradingRecordAnalyses.add(analysis);
                }else {
                    analysis.setCount(1);
                    analysis.setStartTime(order.getOrderTime());
                    analysis.setStatus(1);
                    tradingRecordAnalyses.add(analysis);
                }
                continue;
            }

            if (profit > 0){
                if (analysis.getStatus() == 0){ //如果上次是盈利
                    int count = analysis.getCount() + 1;
                    analysis.setCount(count);
                }else {
                    tradingRecordAnalyses.add(analysis);
                    analysis = new MyTradingRecordAnalysis();
                    analysis.setCount(1);
                    analysis.setStartTime(order.getOrderTime());
                }

            }else {
                if (analysis.getStatus() == 1){ //如果上次是亏损状态
                    int count = analysis.getCount() + 1;
                    analysis.setCount(count);
                }else {
                    tradingRecordAnalyses.add(analysis);
                    analysis = new MyTradingRecordAnalysis();
                    analysis.setCount(1);
                    analysis.setStartTime(order.getOrderTime());
                    analysis.setStatus(1);
                }
            }

        }

        return tradingRecordAnalyses;
    }

}