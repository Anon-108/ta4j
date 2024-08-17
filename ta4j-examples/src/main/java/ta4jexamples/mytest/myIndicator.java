package ta4jexamples.mytest;

import org.ta4j.core.*;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.WMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

import java.time.Duration;
import java.time.ZonedDateTime;

public class myIndicator {
        // 使用 ta4j 实现不同类型均线的示例
        public static void main(String[] args) {
            // 创建一个 K 线序列
            BarSeries series = new BaseBarSeries("15MinBarSeries");

            // 向序列中添加一些示例数据（这里需要使用你的实际价格数据）
            series.addBar(new BaseBar(Duration.ofMinutes(15), ZonedDateTime.now(), 1.0, 2.0, 1.5, 1.8, 100));
            // 根据需要添加更多的 K 线...

            // 选择均线类型和长度
            String maType = "MA2"; // MA2 对应双重 EMA
            int maLength = 8;

            // 计算均线
            Indicator<Num> movingAverage = computeMovingAverage(series, maType, maLength);

            // 可选地打印或使用均线值
            for (int i = maLength; i < series.getBarCount(); i++) {
                System.out.println("K线: " + i + " 均线: " + movingAverage.getValue(i));
            }

            // 如需处理不同的时间框架，可以将 BarSeries 重采样为不同的持续时间
            BarSeries resampledSeries = resampleSeries(series, Duration.ofHours(1));
            Indicator<Num> resampledMovingAverage = computeMovingAverage(resampledSeries, maType, maLength);

            // 可选地打印或使用重采样的均线值
            for (int i = maLength; i < resampledSeries.getBarCount(); i++) {
                System.out.println("重采样 K线: " + i + " 重采样均线: " + resampledMovingAverage.getValue(i));
            }
        }

        // 根据类型计算不同均线的函数
        public static Indicator<Num> computeMovingAverage(BarSeries series, String maType, int length) {
            ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

            switch (maType) {
                case "MA1":
                    return new EMAIndicator(closePrice, length);
                case "MA2":
                    return new EMAIndicator(new EMAIndicator(closePrice, length), length);
                case "MA3":
                    return new EMAIndicator(new EMAIndicator(new EMAIndicator(closePrice, length), length), length);
                case "MA4":
                    return new WMAIndicator(closePrice, length);
                case "MA5":
                    return new SMAIndicator(closePrice, length);
                default:
                    throw new IllegalArgumentException("不支持的均线类型: " + maType);
            }
        }

        // 将 K 线序列重采样为不同持续时间的函数
        public static BarSeries resampleSeries(BarSeries originalSeries, Duration newDuration) {
            BarSeries resampledSeries = new BaseBarSeries("ResampledSeries");
            ZonedDateTime endTime = null;
            Num openPrice = null;
            Num highPrice = null;
            Num lowPrice = null;
            Num closePrice = null;
            Num volume = null;

            for (Bar bar : originalSeries.getBarData()) {
                if (endTime == null || !bar.getEndTime().isBefore(endTime)) {
                    // 如果我们有之前的 K 线，则提交
                    if (endTime != null) {
//                        resampledSeries.addBar(new BaseBar(newDuration, endTime, openPrice, highPrice, lowPrice, closePrice, volume));
                    }

                    // 重置新 K 线
                    endTime = bar.getEndTime().plus(newDuration);
                    openPrice = bar.getOpenPrice();
                    highPrice = bar.getHighPrice();
                    lowPrice = bar.getLowPrice();
                    closePrice = bar.getClosePrice();
                    volume = bar.getVolume();
                } else {
                    // 更新当前 K 线
                    highPrice = highPrice.max(bar.getHighPrice());
                    lowPrice = lowPrice.min(bar.getLowPrice());
                    closePrice = bar.getClosePrice();
                    volume = volume.plus(bar.getVolume());
                }
            }

            // 提交最后一根 K 线
            if (endTime != null) {
//                resampledSeries.addBar(new BaseBar(newDuration, endTime, openPrice, highPrice, lowPrice, closePrice, volume));
            }

            return resampledSeries;
        }
    }


