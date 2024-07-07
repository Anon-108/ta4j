package ta4jexamples.mytest;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.ATRIndicatorEMA;
import org.ta4j.core.indicators.ATRIndicatorSMA;
import org.ta4j.core.indicators.ATRIndicatorWMA;
import org.ta4j.core.num.Num;
import org.ta4j.core.utils.BarSeriesUtils;

import java.io.IOException;

public class ATRIndicator {
    public static void main(String[] args) throws IOException {
        BarSeries series = BarSeriesUtils.buildBinanceData(-1, "ta4j-core/src/test/resources/binance2024-07-07-15.json");
        Bar bar1 = series.getBar(0);
        Bar bar2 = series.getBar(series.getEndIndex());

//        Bar bar3 = series.getBar(1999);
//        Bar bar4 = series.getBar(2000);
//        Bar bar5 = series.getBar(2001);
//
//        Bar bar6 = series.getBar(49999);
//        Bar bar7 = series.getBar(50000);
//        Bar bar8 = series.getBar(50001);

        int num = series.getBarCount() - 2;
        int count = 30;
        Bar bar = series.getBar(num);
        org.ta4j.core.indicators.ATRIndicator atrIndicator = new org.ta4j.core.indicators.ATRIndicator(series, count);
        Num mma = atrIndicator.getValue(num);
        System.out.println("mma:"+mma);

        ATRIndicatorEMA atrIndicatorEMA = new ATRIndicatorEMA(series, count);
        Num ema = atrIndicatorEMA.getValue(num);
        System.out.println("ema:"+ema);

        ATRIndicatorSMA atrIndicatorSMA = new ATRIndicatorSMA(series, count);
        Num sma = atrIndicatorSMA.getValue(num);
        System.out.println("sma:"+sma);

        ATRIndicatorWMA atrIndicatorWMA = new ATRIndicatorWMA(series, count);
        Num wma = atrIndicatorWMA.getValue(num);
        System.out.println("wma:"+wma);
    }
}
