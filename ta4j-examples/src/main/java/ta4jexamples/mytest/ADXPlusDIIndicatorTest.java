package ta4jexamples.mytest;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.adx.ADXAndDiIndicator;
import org.ta4j.core.indicators.adx.ADXIndicator;
import org.ta4j.core.indicators.adx.MinusDIIndicator;
import org.ta4j.core.indicators.adx.PlusDIIndicator;
import org.ta4j.core.utils.BarSeriesUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ADXPlusDIIndicatorTest {
    public static void main(String[] args) {
        String filePath = "D:\\Program Files\\Code\\ta4j\\ta4j-core\\src\\main\\resources\\Klines\\BTCUSDT\\2024_08_12\\Binance_BTCUSDT_2024-08-13_s1_98.json";
//        BarSeries series = BarSeriesUtils.buildBinanceData(-1, "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-core\\src\\main\\resources\\Klines\\BTCUSDT\\Binance_BTCUSDT_2024-07-30_h1.json");
        BarSeries series = BarSeriesUtils.buildBinanceData(-1, filePath);
        Bar bar1 = series.getBar(0);
//        2024-05-18T21:20:59.999+08:00[Asia/Shanghai]
        Bar bar2 = series.getBar(series.getEndIndex());//
//        2024-08-12T21:34+08:00[Asia/Shanghai]
//        2024-08-12T21:34:59.999+08:00[Asia/Shanghai]
        Bar bar3 = series.getBar(series.getEndIndex()-1);//
        Bar bar4 = series.getBar(series.getEndIndex()-2);//
        int diCount = 14;
        int AdxCount = 14;
        ADXIndicator adxIndicator = new ADXIndicator(series,AdxCount);
        ADXAndDiIndicator adxAndDiIndicator = new ADXAndDiIndicator(series, AdxCount);
        MinusDIIndicator minusDIIndicator = new MinusDIIndicator(series, diCount);
        PlusDIIndicator plusDIIndicator = new PlusDIIndicator(series, diCount);
        for (int i = 0; i < series.getBarData().size(); i++) {
            if (i < series.getBarData().size() - 10){
                continue;
            }
            Bar bar = series.getBar(i);//
            BigDecimal adxIndicatorValue = new BigDecimal(adxIndicator.getValue(i).toString()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal minusDIIndicatorValue = new BigDecimal(minusDIIndicator.getValue(i).toString()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal plusDIIndicatorValue = new BigDecimal(plusDIIndicator.getValue(i).toString()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal adxAndDiValue = new BigDecimal(adxAndDiIndicator.getValue(i).toString()).setScale(2, RoundingMode.HALF_UP);


            System.out.println();
        }

    }
}
