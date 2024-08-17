package ta4jexamples.mytest;

import org.ta4j.core.*;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.OverIndicatorRule;
import org.ta4j.core.rules.UnderIndicatorRule;
import org.ta4j.core.utils.BarSeriesUtils;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test240815 {
    private static  Map trade = new HashMap<String,Boolean>();
    public static void main(String[] args) {
        trade.put("isLong",false);
        trade.put("isShort",false);
        trade.put("start",true);

//        String filePath = "D:\\Program Files\\Code\\ta4j\\ta4j-core\\src\\main\\resources\\Klines\\BTCUSDT\\2024_08_14\\Binance_BTCUSDT_2024-08-14_m15_1.json";
        String filePath = "D:\\Program Files\\Code\\ta4j\\ta4j-core\\src\\main\\resources\\Klines\\BTCUSDT\\2024_08_15\\Binance_BTCUSDT_2024-08-15_m1_1.json";
        BarSeries series = BarSeriesUtils.buildBinanceData(-1, filePath);

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

//===================================合并数据================================================
//        for (int start = 0; start < series.getBarCount(); start++) {
//            Bar bar1 = series.getBar(start);
//
//            //当前bar是否和指定bar在同年同月同日同一小时
//            boolean sameDayAndHour = (bar.getBeginTime().getYear() == bar1.getBeginTime().getYear()) &&
//                    (bar.getBeginTime().getMonth() == bar1.getBeginTime().getMonth()) &&
//                    (bar.getBeginTime().getDayOfMonth() == bar1.getBeginTime().getDayOfMonth()) &&
//                    (bar.getBeginTime().getHour() == bar1.getBeginTime().getHour());
//
//            if (sameDayAndHour) {
//                if (lastBarBeginTime == null){
//                    lastBarBeginTime = bar1.getBeginTime();
//                    lastOpenPrice = bar1.getOpenPrice();
//                }
//                if (start == series.getBarCount()-1 || bar.getBeginTime().isEqual(bar1.getBeginTime())){
////                    DecimalNum closePrice = DecimalNum.valueOf("59675.01");
//                    BaseBar baseBar = new BaseBar(Duration.ofHours(1),lastBarBeginTime,bar1.getEndTime(),lastOpenPrice, DecimalNum.ZERO,DecimalNum.ZERO,bar1.getClosePrice(),DecimalNum.ZERO,DecimalNum.ZERO,0l);
//                    mySeries.addBar(baseBar);
//                    break;
//                }
//                System.out.println();
//                continue;
//            }
//
////            if (bar.getBeginTime().isEqual(bar1.getBeginTime())){
////                BaseBar baseBar = new BaseBar(Duration.ofHours(1),bar1.getBeginTime(),bar1.getEndTime(),bar1.getOpenPrice(), DecimalNum.ZERO,DecimalNum.ZERO,bar1.getClosePrice(),DecimalNum.ZERO,DecimalNum.ZERO,0l);
////                mySeries.addBar(baseBar);
////                break;
////            }
//
//            if (start>0){
//                Bar lastBar = mySeries.getLastBar();
//                if (lastBar.getEndTime().isAfter(bar1.getBeginTime())){
//                    continue;
//                }
//            }
//
//            int end = start + timeFrame - 1;
////            if (end > series.getBarCount()-1){ //如果当前结束数据为最后一个，则结束
////                Bar barO = series.getBar(start);
////                Bar barC = series.getBar(series.getBarCount() - 1);
////                BaseBar baseBar = new BaseBar(Duration.ofHours(1),barO.getBeginTime(),barC.getEndTime(),barO.getOpenPrice(), DecimalNum.ZERO,DecimalNum.ZERO,barC.getClosePrice(),DecimalNum.ZERO,DecimalNum.ZERO,0l);
////                mySeries.addBar(baseBar);
////                break;
////            }
//
////            double dailyClose = hourlyPrices.get(start); // 取最后一个小时的价格作为日线收盘价
//            Bar openBar = series.getBar(start); //获取第一根bar为开盘
//            Bar closeBar = series.getBar(end); //获取时间范围最后一根bar为收盘
//            BaseBar baseBar = new BaseBar(Duration.ofHours(1),openBar.getBeginTime(),closeBar.getEndTime(),openBar.getOpenPrice(), DecimalNum.ZERO,DecimalNum.ZERO,closeBar.getClosePrice(),DecimalNum.ZERO,DecimalNum.ZERO,0l);
//            mySeries.addBar(baseBar);
//        }
//        int index = mySeries.getEndIndex();
//        EmaV3Indicator closeLine = new EmaV3Indicator(new ClosePriceIndicator(mySeries), 8);
//        EmaV3Indicator openLine = new EmaV3Indicator(new OpenPriceIndicator(mySeries), 8);
//===================================合并数据 结束================================================
//        Bar bar1 = mySeries.getBar(index);
//        Bar bar2 = mySeries.getBar(index - 1);
//        Bar bar3 = mySeries.getBar(index - 2);
//        Bar bar4 = mySeries.getBar(index - 3);
//
//        Num emaV3Value = closeLine.getValue(index);
//        Num emaV3Value2 = closeLine.getValue(index-1);
//        Num emaV3Value3 = closeLine.getValue(index-2);
//
//        Num value = openLine.getValue(index);
//        Num value1 = openLine.getValue(index - 1);
//        Num value2 = openLine.getValue(index - 2);
//        System.out.println("当前bar开始时间："+mySeries.getBar(index).getBeginTime()+"结束时间："+mySeries.getBar(index).getEndTime()+",当前emaV3值："+emaV3Value.doubleValue());

//===================================策略回测 开始================================================
        BaseBarSeries myBackTest = new BaseBarSeries("myBackTesting");
        EmaV3Indicator closeLine = new EmaV3Indicator(new ClosePriceIndicator(myBackTest), 8);
        EmaV3Indicator openLine = new EmaV3Indicator(new OpenPriceIndicator(myBackTest), 8);
        List<Bar> timeBars = new ArrayList<>();
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

//                =========================
                Bar bar1 = myBackTest.getBar(endIndex);
                Num value1 = openLine.getValue(myBackTest.getEndIndex());
                Num value = closeLine.getValue(myBackTest.getEndIndex());
                System.out.println();
//                =========================

                //回测
                ifShort = backTest(closeLine, openLine, ifShort, endIndex);
            }else {
                //将第一条的bar结束时间和收盘价格设置为当前最新bar 
                Bar bar0 = timeBars.get(0);
                timeBars.add(barData);
                bar0.setEndTime(barData.getEndTime());
                bar0.setClosePrice(barData.getClosePrice());
                myBackTest.addBar(barData);

//                =========================
                int endIndex1 = myBackTest.getEndIndex();
                Bar bar1 = myBackTest.getBar(endIndex1);
                Num value1 = openLine.getValue(myBackTest.getEndIndex());
                Num value = closeLine.getValue(myBackTest.getEndIndex());
                System.out.println();
//                =========================

                int endIndex = myBackTest.getEndIndex();
                //回测
                ifShort = backTest(closeLine, openLine, ifShort, endIndex);

                if (timeBars.size() == timeFrame){  //如果当前已经存储满 时间框架，则清空 开始下一个时间
                    timeBars.clear();
                }
            }


        }
    }
    /**
     * 回测
     * @param closeLine
     * @param openLine
     * @param ifShort
     * @param index
     *
     */
    private static boolean backTest(EmaV3Indicator closeLine, EmaV3Indicator openLine, boolean ifShort,int index) {
        boolean result = false;

        // 开多仓规则：当 closeLine 上穿 openLine 时
        Rule entryLongRule = new OverIndicatorRule(closeLine, openLine);
        // 平多仓规则：当 closeLine 下穿 openLine 且 ifShort 为 false 时
//        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new IndicatorRule(closeLine, openLine).isLessThan(Num.valueOf(ifShort ? 1 : 0)));
//        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new BooleanRule(!ifShort));
        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine);

        // 开空仓规则：当 closeLine 下穿 openLine 且 ifShort 为 true 时
        Rule entryShortRule = new UnderIndicatorRule(closeLine, openLine);
        // 平空仓规则：当 closeLine 上穿 openLine 且 ifShort 为 true 时
        Rule exitShortRule = new OverIndicatorRule(closeLine, openLine);

        Num openLineValue = openLine.getValue(index);
        Num closeLineValue = closeLine.getValue(index);

        Bar bar = closeLine.getBarSeries().getBar(index);
        Boolean isLong = (Boolean)trade.get("isLong");
        Boolean isShort = (Boolean)trade.get("isShort");
        Boolean isStart = (Boolean)trade.get("start");
        //第一次开盘必须是第一次 金叉
        if (isStart){
            Num emaClose = closeLine.getValue(index - 1);
            Num emaOpen = openLine.getValue(index - 1);
            if (entryLongRule.isSatisfied(index) && emaOpen.isGreaterThan(emaClose)){
                System.out.println("开多, 当前时间: "+bar.getBeginTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isLong",true);
                trade.put("start",false);
            }
            return true;
        }

        if (entryLongRule.isSatisfied(index)){
            //如果当前 有了多单和空则不开
            if (!isLong && !isStart){
                System.out.println("开多, 当前时间: "+bar.getBeginTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isLong",true);
                result = false;
            }
        }
        if (exitLongRule.isSatisfied(index)){
            //如果当前 没有了多单则不平
            if (isLong && !isStart){
                System.out.println("平多, 当前时间: "+bar.getBeginTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isLong",false);
                result = false;
            }
        }

        if (entryShortRule.isSatisfied(index)){
            //如果当前 有了多单和空单则不开
            if (!isLong && !isStart){
                System.out.println("开空, 当前时间: "+bar.getBeginTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isShort",true);
                result = true;
            }
        }
        if (exitShortRule.isSatisfied(index)){
            //如果当前 没有了空单则不开
            if (!isLong && isStart){{
                System.out.println("平空, 当前时间: "+bar.getBeginTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isShort",false);
                result = false;
            }}
        }


        return result;
    }
//===================================策略回测 结束================================================
}