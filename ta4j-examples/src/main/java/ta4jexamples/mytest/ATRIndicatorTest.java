package ta4jexamples.mytest;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.ATRIndicator;
import org.ta4j.core.indicators.ATRIndicatorEMA;
import org.ta4j.core.indicators.ATRIndicatorSMA;
import org.ta4j.core.indicators.ATRIndicatorWMA;
import org.ta4j.core.num.Num;
import org.ta4j.core.utils.BarSeriesUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ATRIndicatorTest {

    public static void main(String[] args) throws IOException {
        BarSeries series = BarSeriesUtils.buildBinanceData(-1, "ta4j-core/src/test/resources/binance2024-07-07-15.json");
        Bar bar1 = series.getBar(0);
        Bar bar2 = series.getBar(series.getEndIndex());

        int count = 30;
        for (int i = 0; i < series.getBarData().size()-1; i++) {
            if (i < series.getBarData().size() - 10){
                continue;
            }

            Bar bar = series.getBar(i);
            org.ta4j.core.indicators.ATRIndicator atrIndicator = new ATRIndicator(series, count);
            Num mma = atrIndicator.getValue(i);
            System.out.println("mma:"+mma);
            BigDecimal bigDecimalmma = new BigDecimal(mma.toString()).setScale(2, RoundingMode.HALF_UP);

            ATRIndicatorEMA atrIndicatorEMA = new ATRIndicatorEMA(series, count);
            Num ema = atrIndicatorEMA.getValue(i);
            System.out.println("ema:"+ema);
            BigDecimal bigDecimal1 = new BigDecimal(ema.toString()).setScale(2, RoundingMode.HALF_UP);

            ATRIndicatorSMA atrIndicatorSMA = new ATRIndicatorSMA(series, count);
            Num sma = atrIndicatorSMA.getValue(i);
            System.out.println("sma:"+sma);
            BigDecimal bigDecimal2 = new BigDecimal(sma.toString()).setScale(2, RoundingMode.HALF_UP);

            ATRIndicatorWMA atrIndicatorWMA = new ATRIndicatorWMA(series, count);
            Num wma = atrIndicatorWMA.getValue(i);
            System.out.println("wma:"+wma);
            BigDecimal bigDecimal3 = new BigDecimal(wma.toString()).setScale(2, RoundingMode.HALF_UP);

            System.out.println();

        }
    }
}
