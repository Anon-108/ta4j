package ta4jexamples.mytest;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.*;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.HighestValueIndicator;
import org.ta4j.core.indicators.helpers.LowestValueIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.utils.BarSeriesUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BollingerBandIndicatorTest {
    public static void main(String[] args) throws IOException {
        BarSeries series = BarSeriesUtils.buildBinanceData(-1, "ta4j-core/src/test/resources/binance2024-07-07-15.json");
        Bar bar1 = series.getBar(0);
        Bar bar2 = series.getBar(series.getEndIndex());


        int barCount = 20;
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(series);
        SMAIndicator smaIndicator = new SMAIndicator(closePriceIndicator, barCount);
        StandardDeviationIndicator sDev = new StandardDeviationIndicator(closePriceIndicator, barCount);

        BollingerBandsMiddleIndicator bbMiddle = new BollingerBandsMiddleIndicator(smaIndicator);
        BollingerBandsUpperIndicator bbUp = new BollingerBandsUpperIndicator(bbMiddle, sDev);
        BollingerBandsLowerIndicator bbLower = new BollingerBandsLowerIndicator(bbMiddle, sDev);

        BollingerBandWidthIndicator bbWidth = new BollingerBandWidthIndicator(bbUp, bbMiddle, bbLower);

        HighestValueIndicator highestValueIndicator = new HighestValueIndicator(bbWidth, 125);
        LowestValueIndicator lowestValueIndicator = new LowestValueIndicator(bbWidth,125);

        PercentBIndicator percentBIndicator = new PercentBIndicator(closePriceIndicator, barCount, 2);

        for (int i = 0; i < series.getBarData().size()-1; i++) {
            if (i < series.getBarData().size() - 10){
                continue;
            }
            BigDecimal bbMiddleVal = new BigDecimal(bbMiddle.getValue(i).toString()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bbUpVal = new BigDecimal(bbUp.getValue(i).toString()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bbLowerVal = new BigDecimal(bbLower.getValue(i).toString()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bbWidthVal = new BigDecimal(bbWidth.getValue(i).toString()).setScale(2, RoundingMode.HALF_UP);

            BigDecimal highestVal = new BigDecimal(highestValueIndicator.getValue(i).toString()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal lowestVal = new BigDecimal(lowestValueIndicator.getValue(i).toString()).setScale(2, RoundingMode.HALF_UP);

            //BollingerBands
            System.out.println(bbMiddleVal);
            System.out.println(bbUpVal);
            System.out.println(bbLowerVal);

            //BollingerBandWidthIndicator
            System.out.println(bbWidthVal);
            System.out.println(highestVal);
            System.out.println(lowestVal);

            //percentBIndicator
            BigDecimal percentBVal = new BigDecimal(percentBIndicator.getValue(i).toString()).setScale(2, RoundingMode.HALF_UP);
            System.out.println(percentBVal);


            System.out.println();


        }
    }

}
