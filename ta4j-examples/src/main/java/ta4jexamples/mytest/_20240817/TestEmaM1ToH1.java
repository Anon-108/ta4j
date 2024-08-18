package ta4jexamples.mytest._20240817;

import com.alibaba.excel.EasyExcel;
import com.google.gson.Gson;
import org.ta4j.core.*;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.FixedDecimalIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.OverIndicatorRule;
import org.ta4j.core.rules.UnderIndicatorRule;
import org.ta4j.core.utils.BarSeriesUtils;
import ta4jexamples.mytest.EmaV3Indicator;
import ta4jexamples.mytest._20240816.MyOrder;
import ta4jexamples.mytest._20240816.MyOrderExcel;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

public class TestEmaM1ToH1 {
    private static  Map trade = new HashMap<String,Boolean>();
    private static TradingRecord tradingRecord = new BaseTradingRecord();
    private static Map position = new HashMap<String,Num>();
    private static List<MyOrder> orders = new ArrayList<>();
    private static Map<String,Integer> emaStatus = new HashMap<>();


    public static void main(String[] args) throws IOException {
        trade.put("isLong",false);
        trade.put("isShort",false);
        trade.put("start",true);

        emaStatus.put("closeUpIndex",null);
        emaStatus.put("closeDownIndex",null);

        String filePath = "D:\\Program Files\\Code\\ta4j\\ta4j-core\\src\\main\\resources\\Klines\\BTCUSDT\\2024_08_15\\Binance_BTCUSDT_2024-08-15_m1_1.json";
//        String filePath = "C:\\Users\\Administrator\\Desktop\\backtest\\Binance_BTCUSDT_2024-08-15_m1_1.json";
        BarSeries series = BarSeriesUtils.buildBinanceData(-1, filePath);

////==========================测试1======================================
//        BaseBarSeries barSeries = new BaseBarSeries("test");
//        Indicator<Num> evaluatedIndicator = new FixedDecimalIndicator(barSeries, 8, 9, 10,13, 12, 9, 11, 12, 13);
//        CrossedUpIndicatorRule rule = new CrossedUpIndicatorRule(evaluatedIndicator, 10);
//        boolean satisfied = rule.isSatisfied(0);
//        boolean satisfied1 = rule.isSatisfied(1);
//        boolean satisfied2 = rule.isSatisfied(2);
//        boolean satisfied3 = rule.isSatisfied(3);
//        boolean satisfied4 = rule.isSatisfied(4);
//        boolean satisfied5 = rule.isSatisfied(5);
//        boolean satisfied6 = rule.isSatisfied(6);
//        boolean satisfied7 = rule.isSatisfied(7);
//        System.out.println();

////==========================测试1======================================

////==========================测试======================================
//        String filePath = "D:\\Program Files\\Code\\ta4j\\ta4j-core\\src\\main\\resources\\Klines\\BTCUSDT\\2024_08_14\\Binance_BTCUSDT_2024-08-15_h1_1.json";
//        BarSeries series = BarSeriesUtils.buildBinanceData(-1, filePath);
//
//        BaseBarSeries mySeries2 = new BaseBarSeries("xxxx");
//        for (int i = 0; i < series.getBarData().size(); i++) {
//            Bar bar5 = series.getBar(i); //59247.83
//            DecimalNum closePrice = DecimalNum.valueOf(bar5.getClosePrice().doubleValue());
//            if (i == series.getEndIndex()){
//                closePrice = DecimalNum.valueOf("59247.83");
//            }
//            BaseBar baseBar = new BaseBar(Duration.ofHours(1),bar5.getBeginTime(),bar5.getEndTime(),DecimalNum.ZERO, DecimalNum.ZERO,DecimalNum.ZERO,closePrice,DecimalNum.ZERO,DecimalNum.ZERO,0l);
//            mySeries2.addBar(baseBar);
//
//        }
//        EmaV3Indicator emaV31 = new EmaV3Indicator(new ClosePriceIndicator(mySeries2), 8);
//        Bar bar6 = mySeries2.getBar(mySeries2.getEndIndex());
//        Num value = emaV31.getValue(mySeries2.getEndIndex());
////==========================测试======================================

        Bar bar = null;
        ZonedDateTime time = ZonedDateTime.parse("2024-08-15T00:01+08:00[Asia/Shanghai]");

        for (int i = 0; i < series.getBarData().size(); i++) {
            bar = series.getBar(i);
            if (time.isEqual(bar.getBeginTime())){
                break;
            }
        }
        //需要计算的序列
        BaseBarSeries mySeries = new BaseBarSeries("mySeries");

        int timeFrame = 60; //时间框架  60=1小时
        //最后一根bar的开始时间
        ZonedDateTime lastBarBeginTime = null;
        Num lastOpenPrice = null;
        boolean ifShort = false;

//===================================策略回测 开始================================================
        //盈利 交易总数:60,盈利数量:16.0,亏损数量:14.0,利润：550.09胜率:53.333333333333336
//        int barCount = 20;
//        int barCount2 = 30;

        //盈利 交易总数:290,盈利数量:76.0,亏损数量:69.0,利润：959.13胜率:52.41379310344828
//        int barCount = 20;
//        int barCount2 = 15;

        //盈利 交易总数:6,盈利数量:3.0,亏损数量:0.0,利润：1916.48胜率:100.0
//        int barCount = 80;
//        int barCount2 = 8;

        //盈利 交易总数:143,盈利数量:33.0,亏损数量:38.0,利润：1762.29胜率:46.478873239436616
//        int barCount = 18;
//        int barCount2 = 8;

        //盈利 交易总数:131,盈利数量:32.0,亏损数量:33.0,利润：1110.73胜率:49.23076923076923
//        int barCount = 8;
//        int barCount2 = 18;

        //盈利 交易总数:20,盈利数量:6.0,亏损数量:4.0,利润：620.02胜率:60.0
//        int barCount = 55;
//        int barCount2 = 15;

        //盈利 交易总数:22,盈利数量:6.0,亏损数量:5.0,利润：607.99胜率:54.54545454545454
//        int barCount = 55;
//        int barCount2 = 5;

//      盈利 交易总数:450,盈利数量:123.0,亏损数量:101.0,利润：7892.76胜率:54.91071428571429
        int barCount = 9;
        int barCount2 = 5;

        BaseBarSeries myBackTest = new BaseBarSeries("myBackTesting");
        EmaV3Indicator closeLine = new EmaV3Indicator(new ClosePriceIndicator(myBackTest), barCount);
        EmaV3Indicator openLine = new EmaV3Indicator(new OpenPriceIndicator(myBackTest), barCount2);
        List<Bar> timeBars = new ArrayList<>();


        CrossedUpIndicatorRule upIndicatorRule = new CrossedUpIndicatorRule(closeLine, openLine);
        CrossedDownIndicatorRule downIndicatorRule = new CrossedDownIndicatorRule(closeLine, openLine);

        // 开多仓规则：当 closeLine 上穿 openLine 时
//        Rule entryLongRule = new OverIndicatorRule(closeLine, openLine).and(upIndicatorRule);
        Rule entryLongRule = new OverIndicatorRule(closeLine, openLine);
        // 平多仓规则：当 closeLine 下穿 openLine 且 ifShort 为 false 时
//        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new IndicatorRule(closeLine, openLine).isLessThan(Num.valueOf(ifShort ? 1 : 0)));
//        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new BooleanRule(!ifShort));
//        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(downIndicatorRule);
        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine);

        // 开空仓规则：当 closeLine 下穿 openLine 且 ifShort 为 true 时
//        Rule entryShortRule = new UnderIndicatorRule(closeLine, openLine).and(downIndicatorRule);
        Rule entryShortRule = new UnderIndicatorRule(closeLine, openLine);
        // 平空仓规则：当 closeLine 上穿 openLine 且 ifShort 为 true 时
//        Rule exitShortRule = new OverIndicatorRule(closeLine, openLine).and(upIndicatorRule);
        Rule exitShortRule = new OverIndicatorRule(closeLine, openLine);


        position.put("opLong",null);
        position.put("opShort",null);

        // 定义Excel文件路径
        String fileName = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\20240817\\Binance_BTCUSDT_2024-08-15_m1_17.xlsx";

        // 创建示例数据
        List<MyOrderExcel> data = new ArrayList<>();

        for (int start = 0; start < series.getBarCount(); start++) {
            if (myBackTest.getBarData().size() < 100){//历史k线不能少于100
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

////                =========================
//                Bar bar1 = myBackTest.getBar(endIndex);
//                Num value1 = openLine.getValue(myBackTest.getEndIndex());
//                Num value = closeLine.getValue(myBackTest.getEndIndex());
//                System.out.println();
////                =========================

                //回测
                backTest(entryLongRule,  exitLongRule,  entryShortRule,  exitShortRule, upIndicatorRule,downIndicatorRule,closeLine, openLine, endIndex);
            }else {
                //将第一条的bar结束时间和收盘价格设置为当前最新bar
//                Bar bar0 = timeBars.get(0);
                timeBars.add(barData);

//                bar0.setEndTime(barData.getEndTime());
//                bar0.setClosePrice(barData.getClosePrice());

                myBackTest.getLastBar().setEndTime(barData.getEndTime());
                myBackTest.getLastBar().setClosePrice(barData.getClosePrice());

//                myBackTest.addBar(barData);

////                =========================
//                int endIndex1 = myBackTest.getEndIndex();
//                Bar bar1 = myBackTest.getBar(endIndex1);
//                Num value1 = openLine.getValue(myBackTest.getEndIndex());
//                Num value = closeLine.getValue(myBackTest.getEndIndex());
//                System.out.println();
////                =========================

                int endIndex = myBackTest.getEndIndex();
                //回测
                backTest(entryLongRule,  exitLongRule,  entryShortRule,  exitShortRule, upIndicatorRule,downIndicatorRule,closeLine, openLine, endIndex);

                if (timeBars.size() == timeFrame){  //如果当前已经存储满 时间框架，则清空 开始下一个时间
                    timeBars.clear();
                }
            }
        }
        Bar lastBar = myBackTest.getLastBar();
//        System.out.println(tradingRecord);
        Num profit = DecimalNum.ZERO;
        double profitQty = 0;
        double lossesQty = 0;
        int i = 0;
        for (MyOrder order : orders) {
            Num num = order.getProfit();
            if (num.isGreaterThan(DecimalNum.ZERO)){
                ++profitQty;
            }else if (num.isLessThan(DecimalNum.ZERO)){
                ++lossesQty;
            }
            ++i;
            System.out.println(order);
            if (i % 2 == 0){
                System.out.println("---------------------------------------------------");
            }

            profit = profit.plus(num);
            MyOrderExcel orderExcel = new MyOrderExcel(order.getTradeType(), order.getOrderTime(), order.getOrderPrice().doubleValue(), order.getKlinePrice().doubleValue()
                    , order.getProfit().doubleValue(), order.getOrderEma().doubleValue(), order.getOpenEma().doubleValue(), order.getCloseEma().doubleValue());
            data.add(orderExcel);
        }
        // 使用EasyExcel将数据写入Excel文件
//        EasyExcel.write(fileName, MyOrderExcel.class).sheet("数据表").doWrite(data);

        System.out.println("Excel 文件已成功创建！");
        double proportion = profitQty / (profitQty + lossesQty) * 100;
        System.out.println("交易总数:"+orders.size()+",盈利数量:"+profitQty+",亏损数量:"+lossesQty+",利润："+profit+"胜率:"+proportion);
    }

    /**
     * 回测
     * @param closeLine
     * @param openLine
     * @param index
     *
     */
    private static void backTest(Rule entryLongRule, Rule exitLongRule, Rule entryShortRule, Rule exitShortRule,
                                 CrossedUpIndicatorRule crossUpRule,CrossedDownIndicatorRule crossDownRule
                                ,EmaV3Indicator closeLine, EmaV3Indicator openLine,int index) {

//        // 开多仓规则：当 closeLine 上穿 openLine 时
//        Rule entryLongRule = new OverIndicatorRule(closeLine, openLine);
//        // 平多仓规则：当 closeLine 下穿 openLine 且 ifShort 为 false 时
////        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new IndicatorRule(closeLine, openLine).isLessThan(Num.valueOf(ifShort ? 1 : 0)));
////        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new BooleanRule(!ifShort));
//        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine);
//
//        // 开空仓规则：当 closeLine 下穿 openLine 且 ifShort 为 true 时
//        Rule entryShortRule = new UnderIndicatorRule(closeLine, openLine);
//        // 平空仓规则：当 closeLine 上穿 openLine 且 ifShort 为 true 时
//        Rule exitShortRule = new OverIndicatorRule(closeLine, openLine);
//========================================测试 无用逻辑
//        ZonedDateTime time = ZonedDateTime.parse("2024-08-12T10:00+08:00[Asia/Shanghai]");
//        if (bar.getBeginTime().isBefore(time)){
//            return;
//        }
//========================================测试


//        Boolean isLong = (Boolean)trade.get("isLong");
//        Boolean isShort = (Boolean)trade.get("isShort");
//        Boolean isStart = (Boolean)trade.get("start");

        Num openLineValue = openLine.getValue(index);
        Num closeLineValue = closeLine.getValue(index);

        //止盈亏比例
        Num profitAndLossRatio = DecimalNum.valueOf(1.26).dividedBy(DecimalNum.valueOf(100));

        Bar bar = closeLine.getBarSeries().getBar(index);

        Num opLong = (Num)position.get("opLong");
        Num opShort = (Num)position.get("opShort");
        boolean closeLong = false;
        boolean closeShort = false;

        if (Objects.nonNull(opLong)){
            Num plusPrice = opLong.multipliedBy(DecimalNum.valueOf(1).plus(profitAndLossRatio));//多头止盈价格
            Num minusPrice = opLong.multipliedBy(DecimalNum.valueOf(1).minus(profitAndLossRatio));//多头止损价格

            if (bar.getClosePrice().isGreaterThanOrEqual(plusPrice) || bar.getClosePrice().isLessThanOrEqual(minusPrice)){
                closeLong = true;
            }
        }
        if (Objects.nonNull(opShort)){
            Num plusPrice = opShort.multipliedBy(DecimalNum.valueOf(1).plus(profitAndLossRatio)); //空头止损价格
            Num minusPrice = opShort.multipliedBy(DecimalNum.valueOf(1).minus(profitAndLossRatio));//空头止盈价格
            if (bar.getClosePrice().isGreaterThanOrEqual(plusPrice) || bar.getClosePrice().isLessThanOrEqual(minusPrice)){
                closeShort = true;
            }
        }
//
//        if (closeLong){
//            System.out.println();
//        }
//        if (closeShort){
//            System.out.println();
//        }


        boolean isEntryLong = entryLongRule.isSatisfied(index); 
        boolean isExitLong = exitLongRule.isSatisfied(index);
        boolean isEntryShort = entryShortRule.isSatisfied(index);
        boolean isExitShort = exitShortRule.isSatisfied(index);

        /*交叉信号指标*/
        boolean isCrossUp = crossUpRule.isSatisfied(index); //金叉指标
        boolean isCrossDown = crossDownRule.isSatisfied(index); //死叉指标

        if (isCrossUp && Objects.isNull(emaStatus.get("closeUpIndex"))){
            emaStatus.put("closeUpIndex",index);
        }else if (isCrossDown && Objects.isNull(emaStatus.get("closeDownIndex"))){
            emaStatus.put("closeDownIndex",index);
        }

        boolean openLong = false;
        boolean openShort = false;

        if (Objects.nonNull(emaStatus.get("closeUpIndex"))){
            Integer upIndex = emaStatus.get("closeUpIndex");

            int i = index - upIndex;
            if (i == 1){  //当前是金叉指标的下一次 , 条件达成
                openLong = true;
            }else if (i > 1){  //当前不是金叉指标的下一次 , 清除金叉指标
                emaStatus.put("closeUpIndex",null);
            }
        }
        if (Objects.nonNull(emaStatus.get("closeDownIndex"))){
            Integer downIndex = emaStatus.get("closeDownIndex");

            int i = index - downIndex;
            if (i == 1){  //当前是死叉指标的下一次 , 条件达成
                openShort = true;
            }else if (i > 1){  //当前不是死叉指标的下一次 , 清除死叉指标
                emaStatus.put("closeDownIndex",null);
            }
        }



        //第一次开盘的上一个指标必须是第一次 金叉
        if ((Boolean)trade.get("start") && openLong){
//            Num emaClose = closeLine.getValue(index - 1);
//            Num emaOpen = openLine.getValue(index - 1);
            if (isEntryLong){
                //System.out.println("开多, 当前时间: "+bar.getEndTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isLong",true);
                trade.put("start",false);
//                tradingRecord.enter(index, bar.getClosePrice(), DecimalNum.valueOf(100));
//                tradingRecord.enter(index, bar.getClosePrice(), DecimalNum.valueOf(100));
                position.put("opLong",bar.getClosePrice());
                MyOrder order = new MyOrder("开多", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), DecimalNum.ZERO, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);
            }
        }

        //开多：当前是多头指标并且金叉指标达成
        if (isEntryLong && openLong){
            //如果当前 有了多单和空则不开
            if (!(Boolean)trade.get("isLong") && !(Boolean)trade.get("start") && !(Boolean)trade.get("isShort")){
                //System.out.println("开多, 当前时间: "+bar.getEndTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isLong",true);
//                tradingRecord.enter(index, bar.getClosePrice(), DecimalNum.valueOf(100));
                position.put("opLong",bar.getClosePrice());

                MyOrder order = new MyOrder("开多", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), DecimalNum.ZERO, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);
            }
        }

        if (isExitLong || closeLong || openShort){
            //如果当前 没有了多单则不平
            if ((Boolean)trade.get("isLong") && !(Boolean)trade.get("start")){
                //System.out.println("平多, 当前时间: "+bar.getEndTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isLong",false);
//                tradingRecord.exit(index, bar.getClosePrice(), DecimalNum.valueOf(100));

                Num result = opLong.minus(bar.getClosePrice());
                MyOrder order = new MyOrder("平多", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), result, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);

                position.put("opLong",null);

//                System.out.println("-------------------------------------");
            }
        }
        //开空：当前是空头指标并且死叉指标达成
        if (isEntryShort && openShort){
            //如果当前 有了多单和空单则不开
            if (!(Boolean)trade.get("isLong") && !(Boolean)trade.get("start") && !(Boolean)trade.get("isShort")){
//                System.out.println("开空, 当前时间: "+bar.getEndTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isShort",true);
//                tradingRecord.exit(index, bar.getClosePrice(), DecimalNum.valueOf(100));
                position.put("opShort",bar.getClosePrice());

                MyOrder order = new MyOrder("开空", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), DecimalNum.ZERO, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);

            }
        }
        if (isExitShort|| closeShort || openLong){
            //如果当前 没有了空单则不开
            if (!(Boolean)trade.get("isLong") && !(Boolean)trade.get("start") && (Boolean)trade.get("isShort")){
//                System.out.println("平空, 当前时间: "+bar.getEndTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isShort",false);
//                tradingRecord.enter(index, bar.getClosePrice(), DecimalNum.valueOf(100));
                position.put("opShort",null);

                Num result = opShort.minus(bar.getClosePrice());
                MyOrder order = new MyOrder("平空", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), result, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);

//                System.out.println("-------------------------------------");
            }
        }
    }
//===================================策略回测 结束================================================
}