package ta4jexamples.mytest._20240820;

import com.alibaba.excel.EasyExcel;
import org.ta4j.core.*;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
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
import ta4jexamples.mytest._20240817.OrderRecord;
import ta4jexamples.mytest._240818.ExcelReader;
import ta4jexamples.mytest._240818.MyTradingRecordAnalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

public class TestEmaS1ToH1_RSI_2_3 {
    private static  Map trade = null;
    private static TradingRecord tradingRecord = null;
    private static Map position = null;
    private static List<MyOrder> orders = null;
    private static Map<String,Integer> emaStatus = null;
    private static List<OrderRecord> orderRecords = new ArrayList<>();
    private static  String backtestFile = null;
    private static  String winningProbability = "";
    private static   Integer rsiBarCount  = null;
    private static   Integer barCount1  = null;
    private static   Integer barCount2  = null;
    private static   Integer fileNum  = 0;
    private static   String symbol  = null;
    public static void main(String[] args) throws IOException {

//==========================================================================================
        // 假设我们要读取的目录是当前工作目录
        File directory = new File("E:\\tradeData\\Data\\binance\\s1\\2024_08_18");//TODO 改成E盘
        String createFileName = null;

        // 使用listFiles()方法获取目录下的所有文件和文件夹
        File[] files = directory.listFiles();

        // 检查目录是否为空或不存在
        if (files != null) {
            for (File file : files) {
                createFileName = file.getName(); //获取代币目录，并创建目录
                String filePath = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\randomEmaTest\\_20240821\\"+createFileName;
                Path createFilePath = Paths.get(filePath);

                if (!Files.exists(createFilePath)) {
                    try {
                        // 如果目录不存在则创建
                        Files.createDirectories(createFilePath);
                        System.out.println("目录创建成功: " + createFilePath.toAbsolutePath());
                    } catch (IOException e) {
                        // 处理创建目录时的异常
                        System.err.println("无法创建目录: " + e.getMessage());
                        continue;
                    }
                } else {
                    System.out.println("目录已存在: " + createFilePath.toAbsolutePath());
                    continue;
                }


                File[] files2 = file.listFiles();
                // 检查目录是否为空或不存在
                if (files != null) {
                    // 使用自定义的比较器按最后修改时间对文件进行排序
                    Arrays.sort(files2, new Comparator<File>() {
                        @Override
                        public int compare(File f1, File f2) {
                            // 如果f1比f2最后修改时间早，则返回正数；如果晚，则返回负数
                            return Long.compare(f1.lastModified(), f2.lastModified());
                        }
                    });
                    if (files2.length < 1 ){
                        System.out.println();
                        continue;
                    }
                    startTest(files2,createFileName);

//                    for (File file2 : files2) {
//                        if (!file2.isDirectory()) { // 假设我们只关心文件，不关心目录
//                            System.out.println(file2.getName() + " - Last Modified: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(file2.lastModified())));
//                        }
//                    }
                }
            }
        } else {
            System.out.println("目录不存在或为空");
        }


//==========================================================================================



    }

    private static void startTest(File[] files,String directory) {
        BarSeries series = BarSeriesUtils.buildBinanceDataBig(files);
        symbol = directory;
        Bar firstBar = series.getFirstBar();//2024-08-12T21:34:46.999+08:00[Asia/Shanghai]
        Bar lastBar = series.getLastBar();


        int maxValue = 50; //最大数先用55
        Set<String> random = random(maxValue,50);
        int timeFrame = 60 * 60 ; //时间框架 基于k线级别：1分钟：60=1小时

        ExcelReader excelReader = new ExcelReader();
        String writeFileName = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\randomEmaTest\\RandomEmaTestRSI_2_file2_筛选后的指标.xlsx"; // 文件路径
        for (OrderRecord orderRecord : excelReader.getOrderRecords(writeFileName)) {
            barCount1 = orderRecord.getBarCount1();
            barCount2 = orderRecord.getBarCount2();
            rsiBarCount = 2;
//            backtestFile = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\randomEmaTest\\_20240820\\ETHUSDT\\" +
//                    "EmaTestRSI_2_backtest_barCount1_"+barCount1+"_barCount2_"+barCount2+"胜率："+winningProbability+".xlsx";
            // rsi 14 盈利：交易总数:48,盈利数量:22.0,亏损数量:2.0,利润：3147.90胜率:91.66666666666666
            try {
                testTrade (series,barCount1,barCount2,100,timeFrame);
            } catch (IOException e) {
                System.out.println("回测指标失败");
            }
        }
    }


    public static void testTrade (BarSeries series,int barCount1,int barCount2,int startKline,int timeFrame) throws IOException {


//===================================策略回测 开始================================================
//        int timeFrame = 60; //时间框架

        trade = new HashMap<String,Boolean>();
        tradingRecord = new BaseTradingRecord();
        position = new HashMap<String,Num>();
        orders = new ArrayList<>();
        emaStatus = new HashMap<>();


        trade.put("isLong",false);
        trade.put("isShort",false);
        trade.put("start",true);

        emaStatus.put("closeUpIndex",null);
        emaStatus.put("closeDownIndex",null);

        BaseBarSeries myBackTest = new BaseBarSeries("myBackTesting");
        EmaV3Indicator closeLine = new EmaV3Indicator(new ClosePriceIndicator(myBackTest), barCount1);
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

        RSIIndicator rsi = new RSIIndicator(new ClosePriceIndicator(myBackTest), rsiBarCount);


        position.put("opLong",null);
        position.put("opShort",null);

        // 定义Excel文件路径
//        String fileName = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\20240817\\Binance_BTCUSDT_2024-08-15_m1_17.xlsx";

        // 创建示例数据
        List<MyOrderExcel> writeData = new ArrayList<>();
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
                backTest(entryLongRule,  exitLongRule,  entryShortRule,  exitShortRule, upIndicatorRule,downIndicatorRule,closeLine, openLine, endIndex, rsi);
            }else {
                //将第一条的bar结束时间和收盘价格设置为当前最新bar
                timeBars.add(barData);

                myBackTest.getLastBar().setEndTime(barData.getEndTime());
                myBackTest.getLastBar().setClosePrice(barData.getClosePrice());

                int endIndex = myBackTest.getEndIndex();
                //回测
                backTest(entryLongRule,  exitLongRule,  entryShortRule,  exitShortRule, upIndicatorRule,downIndicatorRule,closeLine, openLine, endIndex,rsi);

                if (timeBars.size() == timeFrame){  //如果当前已经存储满 时间框架，则清空 开始下一个时间
                    timeBars.clear();
                }
            }
        }


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
        order.setEmaBarCount1(TestEmaS1ToH1_RSI_2_3.barCount1);
        order.setEmaBarCount2(TestEmaS1ToH1_RSI_2_3.barCount2);


        // 使用EasyExcel将数据写入Excel文件
//        backtestFile = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\randomEmaTest\\_20240821\\ETHUSDT\\" +
//                "EmaTestRSI_2_backtest_barCount1_"+TestEmaS1ToH1_RSI_2_2.barCount1+"_barCount2_"+TestEmaS1ToH1_RSI_2_2.barCount2+"_胜率："+proportion+".xlsx";
        backtestFile = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\randomEmaTest\\_20240821\\"+symbol+"\\" +
                symbol+"_backtest_胜率："+proportion+(++fileNum)+".xlsx";
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
     * 回测
     *
     * @param closeLine
     * @param openLine
     * @param index
     * @param rsi
     */
    private static void backTest(Rule entryLongRule, Rule exitLongRule, Rule entryShortRule, Rule exitShortRule,
                                 CrossedUpIndicatorRule crossUpRule, CrossedDownIndicatorRule crossDownRule
                                , EmaV3Indicator closeLine, EmaV3Indicator openLine, int index, RSIIndicator rsi) {


        Num openLineValue = openLine.getValue(index);
        Num closeLineValue = closeLine.getValue(index);

        Bar bar = closeLine.getBarSeries().getBar(index);

        Num opLong = (Num)position.get("opLong");
        Num opShort = (Num)position.get("opShort");
        boolean closeLong = false;
        boolean closeShort = false;

        //止盈亏比例
        Num profitAndLossRatio = DecimalNum.valueOf(1.26).dividedBy(DecimalNum.valueOf(100));

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
//        if (Objects.nonNull(opLong)){
//            Num plusPrice = opLong.multipliedBy(DecimalNum.valueOf(1).plus(profitAndLossRatio));//多头止盈价格
//            Num minusPrice = opLong.multipliedBy(DecimalNum.valueOf(1).minus(profitAndLossRatio));//多头止损价格
//
//            if (bar.getClosePrice().isGreaterThanOrEqual(plusPrice) || bar.getClosePrice().isLessThanOrEqual(minusPrice)){
//                closeLong = true;
//            }
//        }
//        if (Objects.nonNull(opShort)){
//            Num plusPrice = opShort.multipliedBy(DecimalNum.valueOf(1).plus(profitAndLossRatio)); //空头止损价格
//            Num minusPrice = opShort.multipliedBy(DecimalNum.valueOf(1).minus(profitAndLossRatio));//空头止盈价格
//            if (bar.getClosePrice().isGreaterThanOrEqual(plusPrice) || bar.getClosePrice().isLessThanOrEqual(minusPrice)){
//                closeShort = true;
//            }
//        }

        boolean isEntryLong = entryLongRule.isSatisfied(index); 
        boolean isExitLong = exitLongRule.isSatisfied(index);
        boolean isEntryShort = entryShortRule.isSatisfied(index);
        boolean isExitShort = exitShortRule.isSatisfied(index);

        /*交叉信号指标*/
        boolean isCrossUp = crossUpRule.isSatisfied(index); //金叉指标
        boolean isCrossDown = crossDownRule.isSatisfied(index); //死叉指标

        //超买/超买
        double rsiVal = rsi.getValue(index).doubleValue();
        boolean rsiOverbuy =false;
        boolean rsiOversold =false;
        if (rsiVal > 70){
            rsiOverbuy = true;
        }else if (rsiVal < 30 ){
            rsiOversold = true;
        }

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

                position.put("opLong",bar.getClosePrice());
                MyOrder order = new MyOrder("开多", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), DecimalNum.ZERO, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);
            }
        }

        //开多：当前是多头指标并且金叉指标达成 rsi 超卖
        if (isEntryLong && openLong && rsiOversold){
            //如果当前 有了多单和空则不开
            if (!(Boolean)trade.get("isLong") && !(Boolean)trade.get("start") && !(Boolean)trade.get("isShort")){
                //System.out.println("开多, 当前时间: "+bar.getEndTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isLong",true);
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

                Num result = opLong.minus(bar.getClosePrice());
                MyOrder order = new MyOrder("平多", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), result, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);

                position.put("opLong",null);

            }
        }
        //开空：当前是空头指标并且死叉指标达成  rsi 超买
        if (isEntryShort && openShort && rsiOverbuy){
            //如果当前 有了多单和空单则不开
            if (!(Boolean)trade.get("isLong") && !(Boolean)trade.get("start") && !(Boolean)trade.get("isShort")){
//                System.out.println("开空, 当前时间: "+bar.getEndTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isShort",true);
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
                position.put("opShort",null);

                Num result = opShort.minus(bar.getClosePrice());
                MyOrder order = new MyOrder("平空", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), result, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);

            }
        }
    }

    public static Set<String> random(int MAX_VALUE,int multiplier) {
        Set<String> generatedPairs = new HashSet<>();
        Random random = new Random();
        while (true) {
            // 生成一对随机数
            int a = random.nextInt(MAX_VALUE) + 1;
            int b = random.nextInt(MAX_VALUE) + 1;

            // 构造数对字符串
            String pair = a + "," + b;

            // 检查是否已经生成过
            if (!generatedPairs.contains(pair)) {
                // 未生成过，则输出并存储
//                System.out.println("Generated pair: (" + a + ", " + b + ")");
                generatedPairs.add(pair);
            }

            // 检查是否已经生成了所有可能的数对
            if (generatedPairs.size() >= MAX_VALUE * multiplier) {
//                System.out.println("All possible pairs have been generated.");
                break;
            }
        }
        return generatedPairs;
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