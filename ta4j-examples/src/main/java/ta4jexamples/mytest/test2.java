package ta4jexamples.mytest;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.utils.BarSeriesUtils;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class test2 {

    public static void main(String[] args) {

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
        for (int start = 0; start < series.getBarCount(); start++) {
            Bar bar1 = series.getBar(start);

            //当前bar是否和指定bar在同年同月同日同一小时
            boolean sameDayAndHour = (bar.getBeginTime().getYear() == bar1.getBeginTime().getYear()) &&
                    (bar.getBeginTime().getMonth() == bar1.getBeginTime().getMonth()) &&
                    (bar.getBeginTime().getDayOfMonth() == bar1.getBeginTime().getDayOfMonth()) &&
                    (bar.getBeginTime().getHour() == bar1.getBeginTime().getHour());

            if (sameDayAndHour) {
                if (lastBarBeginTime == null){
                    lastBarBeginTime = bar1.getBeginTime();
                    lastOpenPrice = bar1.getOpenPrice();
                }
                if (start == series.getBarCount()-1 || bar.getBeginTime().isEqual(bar1.getBeginTime())){
//                    DecimalNum closePrice = DecimalNum.valueOf("59675.01");
                    BaseBar baseBar = new BaseBar(Duration.ofHours(1),lastBarBeginTime,bar1.getEndTime(),lastOpenPrice, DecimalNum.ZERO,DecimalNum.ZERO,bar1.getClosePrice(),DecimalNum.ZERO,DecimalNum.ZERO,0l);
                    mySeries.addBar(baseBar);
                    break;
                }
                System.out.println();
                continue;
            }

//            if (bar.getBeginTime().isEqual(bar1.getBeginTime())){
//                BaseBar baseBar = new BaseBar(Duration.ofHours(1),bar1.getBeginTime(),bar1.getEndTime(),bar1.getOpenPrice(), DecimalNum.ZERO,DecimalNum.ZERO,bar1.getClosePrice(),DecimalNum.ZERO,DecimalNum.ZERO,0l);
//                mySeries.addBar(baseBar);
//                break;
//            }

            if (start>0){
                Bar lastBar = mySeries.getLastBar();
                if (lastBar.getEndTime().isAfter(bar1.getBeginTime())){
                    continue;
                }
            }

            int end = start + timeFrame - 1;
//            if (end > series.getBarCount()-1){ //如果当前结束数据为最后一个，则结束
//                Bar barO = series.getBar(start);
//                Bar barC = series.getBar(series.getBarCount() - 1);
//                BaseBar baseBar = new BaseBar(Duration.ofHours(1),barO.getBeginTime(),barC.getEndTime(),barO.getOpenPrice(), DecimalNum.ZERO,DecimalNum.ZERO,barC.getClosePrice(),DecimalNum.ZERO,DecimalNum.ZERO,0l);
//                mySeries.addBar(baseBar);
//                break;
//            }

//            double dailyClose = hourlyPrices.get(start); // 取最后一个小时的价格作为日线收盘价
            Bar openBar = series.getBar(start); //获取第一根bar为开盘
            Bar closeBar = series.getBar(end); //获取时间范围最后一根bar为收盘
            BaseBar baseBar = new BaseBar(Duration.ofHours(1),openBar.getBeginTime(),closeBar.getEndTime(),openBar.getOpenPrice(), DecimalNum.ZERO,DecimalNum.ZERO,closeBar.getClosePrice(),DecimalNum.ZERO,DecimalNum.ZERO,0l);
            mySeries.addBar(baseBar);
        }
        int index = mySeries.getEndIndex();
        EmaV3Indicator emaV3C = new EmaV3Indicator(new ClosePriceIndicator(mySeries), 8);
        EmaV3Indicator emaV3O = new EmaV3Indicator(new OpenPriceIndicator(mySeries), 8);
        Bar bar1 = mySeries.getBar(index);
        Bar bar2 = mySeries.getBar(index - 1);
        Bar bar3 = mySeries.getBar(index - 2);
        Bar bar4 = mySeries.getBar(index - 3);

        Num emaV3Value = emaV3C.getValue(index);
        Num emaV3Value2 = emaV3C.getValue(index-1);
        Num emaV3Value3 = emaV3C.getValue(index-2);

        Num value = emaV3O.getValue(index);
        Num value1 = emaV3O.getValue(index - 1);
        Num value2 = emaV3O.getValue(index - 2);
        System.out.println("当前bar开始时间："+mySeries.getBar(index).getBeginTime()+"结束时间："+mySeries.getBar(index).getEndTime()+",当前emaV3值："+emaV3Value.doubleValue());




    }
}