/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan, 2017-2021 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ta4jexamples.barSeries;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.ConvertibleBaseBarBuilder;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.DoubleNum;

public class BuildBarSeries {

    /**
     * Calls different functions that shows how a BaseBarSeries could be created and how Bars could be added
     * * 调用显示如何创建 BaseBarSeries 以及如何添加 Bars 的不同函数
     *
     * @param args command line arguments (ignored)
     *             命令行参数（忽略）
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
//        BarSeries a = buildAndAddData();
//        System.out.println("a: " + a.getBar(0).getClosePrice().getName());
//        BaseBarSeriesBuilder.setDefaultFunction(DoubleNum::valueOf);
//        a = buildAndAddData();
//        System.out.println("a: " + a.getBar(0).getClosePrice().getName());
//        BarSeries b = buildWithDouble();
//        BarSeries c = buildWithBigDecimal();
//        BarSeries d = buildManually();
//        BarSeries e = buildManuallyDoubleNum();
//        BarSeries f = buildManuallyAndAddBarManually();
//        BarSeries g = buildAndAddBarsFromList();
        BarSeries barSeries = buildBinanceData(1000);
        System.out.println(barSeries);
    }

    public static BarSeries buildBinanceData(int limit)  {
        int num = 0;
        BarSeries series = new BaseBarSeriesBuilder().withName("BTC/USDT").build();
        String fileName = "2024-06-15_28_2000.json";
//        String fileName = "2024-06-13_18.json";
        String filePath = "D:\\Program Files\\Code\\XChange\\xchange-examples\\src\\main\\resources\\dataFile\\"+fileName; // 文件的路径
        ObjectMapper objectMapper = new ObjectMapper(); // 可以重用此实例
        try {
            List<LinkedHashMap<String,Object>> klines = objectMapper.readValue(new File(filePath), List.class);
            for (LinkedHashMap<String,Object> kline : klines) {
                if (limit > 0 && num == limit){
                    break;
                }
                num++;
                String instrument = (String) kline.get("instrument");
                String interval = (String) kline.get("interval");
                ZonedDateTime openTime = timestampToZonedDateTime((long) kline.get("openTime"));
                ZonedDateTime closeTime = timestampToZonedDateTime((long) kline.get("closeTime"));
                double open = (double) kline.get("open");
                double high = (double) kline.get("high");
                double low = (double) kline.get("low");
                double close = (double) kline.get("close");
                double volume = (double) kline.get("volume");
//                double quoteAssetVolume = (double) kline.get("quoteAssetVolume");
//                long numberOfTrades = (long) kline.get("numberOfTrades");
//                double takerBuyBaseAssetVolume = (double) kline.get("takerBuyBaseAssetVolume");
//                double takerBuyQuoteAssetVolume = (double) kline.get("takerBuyQuoteAssetVolume");
//                boolean closed = (boolean) kline.get("closed");

                series.addBar(closeTime,open,high,low,close,volume);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return series;
    }

    public static ZonedDateTime timestampToZonedDateTime( long timestamp) {
            // 假设我们有一个时间戳，这里以毫秒为单位（从1970-01-01T00:00:00Z开始）
//            long timestamp = 1609459200000L; // 例如，这是一个UTC时间戳

            // 首先，我们将时间戳转换为Instant对象
            Instant instant = Instant.ofEpochMilli(timestamp);

            // 然后，我们可以将Instant转换为ZonedDateTime。这里我们使用UTC时区作为示例
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));

//            // 如果你想要使用其他时区，只需更改ZoneId即可
//            // 例如，使用纽约时区
//            ZonedDateTime zonedDateTimeNewYork = instant.atZone(ZoneId.of("America/New_York"));
//
//            // 打印结果
//            System.out.println("UTC Time: " + zonedDateTime);
//            System.out.println("New York Time: " + zonedDateTimeNewYork);\
        return zonedDateTime;
        }

        private static BarSeries buildAndAddData() {
        BarSeries series = new BaseBarSeriesBuilder().withName("mySeries").build();

        ZonedDateTime endTime = ZonedDateTime.now();
        series.addBar(endTime, 105.42, 112.99, 104.01, 111.42, 1337);
        series.addBar(endTime.plusDays(1), 111.43, 112.83, 107.77, 107.99, 1234);
        series.addBar(endTime.plusDays(2), 107.90, 117.50, 107.90, 115.42, 4242);
        // ...
        return series;
    }

    private static BarSeries buildWithDouble() {
        BarSeries series = new BaseBarSeriesBuilder().withName("mySeries").withNumTypeOf(DoubleNum.class).build();

        ZonedDateTime endTime = ZonedDateTime.now();
        series.addBar(endTime, 105.42, 112.99, 104.01, 111.42, 1337);
        series.addBar(endTime.plusDays(1), 111.43, 112.83, 107.77, 107.99, 1234);
        series.addBar(endTime.plusDays(2), 107.90, 117.50, 107.90, 115.42, 4242);
        // ...

        return series;
    }

    private static BarSeries buildWithBigDecimal() {
        BarSeries series = new BaseBarSeriesBuilder().withName("mySeries").withNumTypeOf(DecimalNum.class).build();

        ZonedDateTime endTime = ZonedDateTime.now();
        series.addBar(endTime, 105.42, 112.99, 104.01, 111.42, 1337);
        series.addBar(endTime.plusDays(1), 111.43, 112.83, 107.77, 107.99, 1234);
        series.addBar(endTime.plusDays(2), 107.90, 117.50, 107.90, 115.42, 4242);
        // ...

        return series;
    }

    private static BarSeries buildManually() {
        BarSeries series = new BaseBarSeries("mySeries"); // uses BigDecimalNum 使用 BigDecimalNum

        ZonedDateTime endTime = ZonedDateTime.now();
        series.addBar(endTime, 105.42, 112.99, 104.01, 111.42, 1337);
        series.addBar(endTime.plusDays(1), 111.43, 112.83, 107.77, 107.99, 1234);
        series.addBar(endTime.plusDays(2), 107.90, 117.50, 107.90, 115.42, 4242);
        // ...

        return series;
    }

    private static BarSeries buildManuallyDoubleNum() {
        BarSeries series = new BaseBarSeries("mySeries", DoubleNum::valueOf); // uses DoubleNum // 使用双数
        ZonedDateTime endTime = ZonedDateTime.now();
        series.addBar(endTime, 105.42, 112.99, 104.01, 111.42, 1337);
        series.addBar(endTime.plusDays(1), 111.43, 112.83, 107.77, 107.99, 1234);
        series.addBar(endTime.plusDays(2), 107.90, 117.50, 107.90, 115.42, 4242);
        // ...

        return series;
    }

    private static BarSeries buildManuallyAndAddBarManually() {
        BarSeries series = new BaseBarSeries("mySeries", DoubleNum::valueOf); // uses DoubleNum // 使用双数

        // create bars and add them to the series. The bars must have the same Num type as the series
        // 创建条形并将它们添加到系列中。 条形必须具有与系列相同的 Num 类型
        ZonedDateTime endTime = ZonedDateTime.now();
        Bar b1 = BaseBar.builder(DoubleNum::valueOf, Double.class).timePeriod(Duration.ofDays(1)).endTime(endTime)
                .openPrice(105.42).highPrice(112.99).lowPrice(104.01).closePrice(111.42).volume(1337.0).build();
        Bar b2 = BaseBar.builder(DoubleNum::valueOf, Double.class).timePeriod(Duration.ofDays(1))
                .endTime(endTime.plusDays(1)).openPrice(111.43).highPrice(112.83).lowPrice(107.77).closePrice(107.99)
                .volume(1234.0).build();
        Bar b3 = BaseBar.builder(DoubleNum::valueOf, Double.class).timePeriod(Duration.ofDays(1))
                .endTime(endTime.plusDays(2)).openPrice(107.90).highPrice(117.50).lowPrice(107.90).closePrice(115.42)
                .volume(4242.0).build();
        // ...

        series.addBar(b1);
        series.addBar(b2);
        series.addBar(b3);

        return series;
    }

    private static BarSeries buildAndAddBarsFromList() {
        // Store Bars in a list and add them later. The bars must have the same Num type as the series
        // 将 Bars 存储在列表中并稍后添加。 条形必须具有与系列相同的 Num 类型
        ZonedDateTime endTime = ZonedDateTime.now();
        Bar b1 = barBuilderFromString().timePeriod(Duration.ofDays(1)).endTime(endTime).openPrice("105.42")
                .highPrice("112.99").lowPrice("104.01").closePrice("111.42").volume("1337").build();
        Bar b2 = barBuilderFromString().timePeriod(Duration.ofDays(1)).endTime(endTime.plusDays(1)).openPrice("111.43")
                .highPrice("112.83").lowPrice("107.77").closePrice("107.99").volume("1234").build();
        Bar b3 = barBuilderFromString().timePeriod(Duration.ofDays(1)).endTime(endTime.plusDays(2)).openPrice("107.90")
                .highPrice("117.50").lowPrice("107.90").closePrice("115.42").volume("4242").build();
        List<Bar> bars = Arrays.asList(b1, b2, b3);

        return new BaseBarSeriesBuilder().withName("mySeries").withNumTypeOf(DoubleNum::valueOf).withMaxBarCount(5)
                .withBars(bars).build();
    }

    private static ConvertibleBaseBarBuilder<String> barBuilderFromString() {
        return BaseBar.builder(DoubleNum::valueOf, String.class);
    }
}
