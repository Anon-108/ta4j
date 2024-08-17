package ta4jexamples.mytest._20240816;//package ta4jexamples.mytest._2024_08_16;
//
//import org.ta4j.core.*;
//import org.ta4j.core.backtest.BarSeriesManager;
//import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
//import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
//import org.ta4j.core.num.Num;
//import org.ta4j.core.rules.CrossedDownIndicatorRule;
//import org.ta4j.core.rules.CrossedUpIndicatorRule;
//import org.ta4j.core.utils.BarSeriesUtils;
//import ta4jexamples.mytest.EmaV3Indicator;
//
//import java.io.IOException;
//
//public class Test2 {
//
//    public static void main(String[] args) throws IOException {
//        String filePath = "C:\\Users\\Administrator\\Desktop\\backtest\\Binance_BTCUSDT_2024-08-15_m1_1.json";
//        BarSeries series = BarSeriesUtils.buildBinanceData(-1, filePath);
//
//        // 创建策略
//        Strategy strategy = buildStrategy(series);
//
//        // 创建回测
//        BarSeriesManager seriesManager = new BarSeriesManager(series);
//        TradingRecord tradingRecord = seriesManager.run(strategy);
//
//        // 打印交易信号
//        printTradingSignals(series, tradingRecord);
//    }
//
//    private static Strategy buildStrategy(EmaV3Indicator openPrice, EmaV3Indicator closePrice) {
//
//        // 创建规则
//        Rule enterLong = new CrossedUpIndicatorRule(closePrice, openPrice);
//        Rule exitLong = new CrossedDownIndicatorRule(closePrice, openPrice);
//
//        Rule enterShort = new CrossedDownIndicatorRule(closePrice, openPrice);
//        Rule exitShort = new CrossedUpIndicatorRule(closePrice, openPrice);
//
//        // 策略
//        return new BaseStrategy("MyStrategy",
//                enterLong,
//                exitLong
//        ).and(new BaseStrategy("ShortStrategy",
//                enterShort,
//                exitShort
//        ));
//    }
//
//    private static void printTradingSignals(BarSeries series, TradingRecord tradingRecord) {
//        System.out.println(tradingRecord);
////        for (Trade trade : tradingRecord.getLastEntry()) {
////            int index = trade.getIndex();
////            System.out.println("Trade Signal at index " + index + ": "
////                    + (trade.getType() == Trade.TradeType.BUY ? "BUY" : "SELL")
////                    + " - Price: " + series.getBar(index).getClosePrice());
////        }
//    }
//}
//
