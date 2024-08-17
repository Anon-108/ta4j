//package ta4jexamples.mytest;
//
//import org.ta4j.core.*;
//import org.ta4j.core.backtest.BarSeriesManager;
//import org.ta4j.core.num.DecimalNum;
//import org.ta4j.core.num.Num;
////import org.ta4j.core.rules.CrossoverRule;
////import org.ta4j.core.rules.CrossUnderRule;
////import org.ta4j.core.rules.IsGreaterThanRule;
////import org.ta4j.core.rules.IsLessThanRule;
//import org.ta4j.core.indicators.SMAIndicator;
//import org.ta4j.core.indicators.EMAIndicator;
//import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
//import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
//import org.ta4j.core.rules.BooleanRule;
//import org.ta4j.core.rules.OverIndicatorRule;
//import org.ta4j.core.rules.UnderIndicatorRule;
//
//import java.time.ZonedDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class TradingViewJava {
//
//    public static void main(String[] args) {
//        // 示例数据：请用实际数据替换
//        List<Bar> bars = new ArrayList<>();
////        bars.add(new BaseBar(ZonedDateTime.now(),DecimalNum.valueOf(100), DecimalNum.valueOf(105), DecimalNum.valueOf(95), DecimalNum.valueOf(100), Num.valueOf(1000)));
////        bars.add(new BaseBar(ZonedDateTime.now().plusMinutes(1), Num.valueOf(101), Num.valueOf(106), Num.valueOf(96), Num.valueOf(101), Num.valueOf(1500)));
//        // 根据需要添加更多的 Bar 数据
//
//        // 创建时间序列
//        BarSeries series = new BaseBarSeriesBuilder().withName("Sample Series").withBars(bars).build();
//        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
//        OpenPriceIndicator openPrice = new OpenPriceIndicator(series);
//        BarSeriesManager seriesManager = new BarSeriesManager(series);
//        // 设置参数
//        int length = 8; // 示例长度
//        boolean ifShort = true; // 示例开空仓标志
//
//        // 创建移动平均线指标
//        EMAIndicator closeLine = new EMAIndicator(closePrice, length);
//        EMAIndicator openLine = new EMAIndicator(openPrice, length);
//
//        // 创建策略
//        Strategy strategy = createStrategy(closeLine, openLine, ifShort);
//
//        // 执行回测
//        TradingRecord tradingRecord = seriesManager.run(strategy);
//
//        // 打印交易记录
//        for (int i = 0; i < tradingRecord.getPositions().size(); i++) {
//            System.out.println("交易 " + i + ": " + tradingRecord.getPositions().get(i));
//        }
//    }
//
//    // 创建策略的方法
//    private static Strategy createStrategy(EMAIndicator closeLine, EMAIndicator openLine, boolean ifShort) {
//        // 开多仓规则：当 closeLine 上穿 openLine 时
//        Rule entryLongRule = new OverIndicatorRule(closeLine, openLine);
//        // 平多仓规则：当 closeLine 下穿 openLine 且 ifShort 为 false 时
////        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new IndicatorRule(closeLine, openLine).isLessThan(Num.valueOf(ifShort ? 1 : 0)));
//        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new BooleanRule(!ifShort));
//
//
//        // 开空仓规则：当 closeLine 下穿 openLine 且 ifShort 为 true 时
//        Rule entryShortRule = new UnderIndicatorRule(closeLine, openLine).and(new BooleanRule(ifShort));
//        // 平空仓规则：当 closeLine 上穿 openLine 且 ifShort 为 true 时
//        Rule exitShortRule = new OverIndicatorRule(closeLine, openLine).and(new BooleanRule(ifShort));
//
//        return new BaseStrategy(entryLongRule, exitLongRule, entryShortRule, exitShortRule);
//    }
//}
