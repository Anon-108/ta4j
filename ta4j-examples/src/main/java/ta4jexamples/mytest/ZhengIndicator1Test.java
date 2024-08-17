package ta4jexamples.mytest;

import org.ta4j.core.*;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.utils.BarSeriesUtils;

import java.time.Instant;

/**
 * 郑总提供指标1
 */
public class ZhengIndicator1Test {
    public static void main(String[] args) {
//        String filePath = "D:\\Program Files\\Code\\ta4j\\ta4j-core\\src\\main\\resources\\Klines\\BTCUSDT\\2024_08_13\\Binance_BTCUSDT_2024-08-13_m1_1_0801-0813.json";
//        String filePath = "D:\\Program Files\\Code\\ta4j\\ta4j-core\\src\\main\\resources\\Klines\\BTCUSDT\\2024_08_14\\Binance_BTCUSDT_2024-08-14_m1_1.json";
        String filePath = "D:\\Program Files\\Code\\ta4j\\ta4j-core\\src\\main\\resources\\Klines\\BTCUSDT\\2024_08_14\\Binance_BTCUSDT_2024-08-14_m15_1.json";
        BarSeries series = BarSeriesUtils.buildBinanceData(-1, filePath);
        Bar firstBar = series.getFirstBar();
        Bar lastBar = series.getLastBar();

        ClosePriceIndicator close = new ClosePriceIndicator(series);
        OpenPriceIndicator open = new OpenPriceIndicator(series);
//        new Ind

//        _atr(type, src, len) =>
//        v2 = ema(src, len),v3 = 2 * v2 - ema(v2, len),v4 = 3 * (v2 - ema(v2, len)) + ema(ema(v2, len), len),v5 = wma(src, len),v7 = 0.0,v7 := na(v7[1]) ? sma(src, len) : (v7[1] * (len - 1) + src) / len
//        type=="MA1"?v2 : type=="MA2"?v3 : type=="MA3"?v4 : type=="MA4"?v5: v7

        EMAIndicator v2 = new EMAIndicator(close, 8);
        EMAIndicator v3 = new EMAIndicator(v2, 8);

//        _CSeries = _atr(_Type, close, _Len),_OSeries  = _atr(_Type, open, _Len)
        int len = 8;
        EmaV3Indicator closeEma = new EmaV3Indicator(close, len);
        EmaV3Indicator openEma = new EmaV3Indicator(open, len);
        CrossedUpIndicatorRule upIndicatorRule = new CrossedUpIndicatorRule(closeEma, openEma);
        CrossedDownIndicatorRule downIndicatorRule = new CrossedDownIndicatorRule(closeEma, openEma);

        Strategy sellStrategy = new BaseStrategy(upIndicatorRule, downIndicatorRule);
        Strategy shortStrategy = new BaseStrategy(downIndicatorRule,upIndicatorRule);
        TradingRecord tradingRecord = new BaseTradingRecord();

        for (int i = 0; i < series.getBarData().size(); i++) {
            Bar bar = series.getBarData().get(i);
            Instant instant = bar.getBeginTime().toInstant();
            long epochMilli = instant.toEpochMilli();
            if (i >= 4000 && epochMilli >= 1717217100000l){
//                boolean satisfied = upIndicatorRule.isSatisfied(i, null);
//                if (satisfied){
//                    System.out.println();
//                }
                double closeEmaVal = closeEma.getValue(i).doubleValue(); //67405.11  67488.0
                double openEmaVal = openEma.getValue(i).doubleValue(); //67474.21   67631.97
//                2 * v2 - ema(v2, len)
                Num minus = DecimalNum.valueOf(2).multipliedBy(v2.getValue(i)).minus(v3.getValue(i));
                Num v2Value = v2.getValue(i);
                System.out.println(bar.toString()+minus);


                if (closeEmaVal > openEmaVal ){
                    System.out.println();
                }

                if (sellStrategy.shouldEnter(i)) {
                    // Our sellStrategy should enter
                    // 我们的策略应该进入
//                    System.out.println("Strategy should ENTER on 策略应进入 " + i);
                    boolean entered = tradingRecord.enter(i, bar.getClosePrice(), DecimalNum.valueOf(10));
                    if (entered) {
                        Trade entry = tradingRecord.getLastEntry();
                        System.out.println("Entered on  开多时间" + bar.getBeginTime() + " (price= （价格=" + entry.getNetPrice().doubleValue()
                                + ", amount= 金额=" + entry.getAmount().doubleValue() + ")");
                        System.out.println();
                    }
                }else if (sellStrategy.shouldExit(i)) {
                    // Our sellStrategy should exit
                    // 我们的策略应该退出
//                    System.out.println("Strategy should EXIT on  策略应退出" + i);
                    boolean exited = tradingRecord.exit(i, bar.getClosePrice(), DecimalNum.valueOf(10));
                    if (exited) {
                        Trade exit = tradingRecord.getLastExit();
                        System.out.println("Exited on 平多时间" + bar.getBeginTime() + " (price= 价格=" + exit.getNetPrice().doubleValue()
                                + ", amount= 金额=" + exit.getAmount().doubleValue() + ")");
                        System.out.println();
                    }
                }

            }

        }


    }
}
