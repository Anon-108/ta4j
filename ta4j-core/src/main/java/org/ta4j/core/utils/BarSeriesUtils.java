/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
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
package org.ta4j.core.utils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ta4j.core.*;
import org.ta4j.core.aggregator.BarAggregator;
import org.ta4j.core.aggregator.BarSeriesAggregator;
import org.ta4j.core.aggregator.BaseBarSeriesAggregator;
import org.ta4j.core.aggregator.DurationBarAggregator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.OverIndicatorRule;
import org.ta4j.core.rules.UnderIndicatorRule;

/**
 * Common utilities and helper methods for {@link BarSeries}.
 */
public final class BarSeriesUtils {

    /**
     * Sorts the Bars by {@link Bar#getEndTime()} in ascending sequence (lower values before higher values).
     * * 按 {@link Bar#getEndTime()} 升序（较低值在较高值之前）对条形图进行排序。
     */
    public static final Comparator<Bar> sortBarsByTime = (b1, b2) -> b1.getEndTime().isAfter(b2.getEndTime()) ? 1 : -1;

    private BarSeriesUtils() {
    }


    public static BarSeries buildBinanceData(int limit,String fileName) throws IOException {
        int num = 0;
        BarSeries series = new BaseBarSeriesBuilder().withName("BTC/USDT").build();
//        String fileName = "2024-06-15_28_2000.json";
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
                series.addBar(Duration.ofHours(1),closeTime,open,high,low,close,volume);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return series;
    }

    public static ZonedDateTime timestampToZonedDateTime( long timestamp) {
//        timestamp +=1; // 增加时间，补充时间戳 2017-08-17T11:59:59.999

        // 假设我们有一个时间戳，这里以毫秒为单位（从1970-01-01T00:00:00Z开始）
//            long timestamp = 1609459200000L; // 例如，这是一个UTC时间戳

        // 首先，我们将时间戳转换为Instant对象
        Instant instant = Instant.ofEpochMilli(timestamp);

        // 然后，我们可以将Instant转换为ZonedDateTime。这里我们使用UTC时区作为示例
//        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
//        ZonedDateTime zonedDateTime2 = instant.atZone(ZoneId.of("GMT"));
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Shanghai"));

//            // 如果你想要使用其他时区，只需更改ZoneId即可
//            // 例如，使用纽约时区
//            ZonedDateTime zonedDateTimeNewYork = instant.atZone(ZoneId.of("America/New_York"));!
//
//            // 打印结果
//            System.out.println("UTC Time: " + zonedDateTime);
//            System.out.println("New York Time: " + zonedDateTimeNewYork);


//// 截断到小时，获取当前小时的开始时间
//        ZonedDateTime startOfHourInShanghai = zonedDateTime.truncatedTo(java.time.temporal.ChronoUnit.HOURS);
//
//// 打印结果
//        System.out.println("Current time in Shanghai: " + zonedDateTime);
//        System.out.println("Start of current hour in Shanghai: " + startOfHourInShanghai);


        return zonedDateTime;
    }

    public static Map<String , Strategy> buildStrategyToMap(BarSeries series, Map<String, Indicator> indicatorMap) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null 系列不能为空");
        }
        ClosePriceIndicator closePrice = (ClosePriceIndicator) indicatorMap.get("closePrice");
        Map<String, Strategy> map = new HashMap<>();
        Indicator sma = indicatorMap.get("SMA");
        if (Objects.nonNull(sma)) {
            Strategy buySellSignals = new BaseStrategy(new OverIndicatorRule(sma, closePrice),
                    new UnderIndicatorRule(sma, closePrice));
            map.put("SMA", buySellSignals);
        }
        return map;
    }


    /**
     * Aggregates a list of bars by {@code timePeriod}. The new {@code timePeriod}
     * must be a multiplication of the actual time period.
     *
     * @param barSeries            the barSeries
     * @param timePeriod           the target time period that aggregated bars
     *                             should have
     * @param aggregatedSeriesName the name of the aggregated barSeries
     *                             聚合 barSeries 的名称
     * @return the aggregated barSeries
     * @return 聚合的 barSeries
     */
    public static BarSeries aggregateBars(BarSeries barSeries, Duration timePeriod, String aggregatedSeriesName) {
        final BarAggregator durationAggregator = new DurationBarAggregator(timePeriod, true);
        final BarSeriesAggregator seriesAggregator = new BaseBarSeriesAggregator(durationAggregator);
        return seriesAggregator.aggregate(barSeries, aggregatedSeriesName);
    }

    /**
     * We can assume that finalized bar data will be never changed afterwards by the
     * marketdata provider. It is rare, but depending on the exchange, they reserve
     * the right to make updates to finalized bars. This method finds and replaces
     * potential bar data that was changed afterwards by the marketdata provider. It
     * can also be uses to check bar data equality over different marketdata
     * providers. This method does <b>not</b> add missing bars but replaces an
     * existing bar with its new bar.
     *
     * @param barSeries the barSeries
     *                  酒吧系列
     * @param newBar    the bar which has precedence over the same existing bar
     *                  优先于相同现有栏的栏
     * @return the previous bar replaced by newBar, or null if there was no replacement.
     * @return 用 newBar 替换的前一个 bar，如果没有替换，则返回 null。
     */
    public static Bar replaceBarIfChanged(BarSeries barSeries, Bar newBar) {
        List<Bar> bars = barSeries.getBarData();
        if (bars == null || bars.isEmpty())
            return null;
        for (int i = 0; i < bars.size(); i++) {
            Bar bar = bars.get(i);
            boolean isSameBar = bar.getBeginTime().isEqual(newBar.getBeginTime())
                    && bar.getEndTime().isEqual(newBar.getEndTime())
                    && bar.getTimePeriod().equals(newBar.getTimePeriod());
            if (isSameBar && !bar.equals(newBar))
                return bars.set(i, newBar);
        }
        return null;
    }

    /**
     * Finds possibly missing bars. The returned list contains the {@code endTime}
     * of each missing bar. A bar is possibly missing if: (1) the subsequent bar
     * starts not with the end time of the previous bar or (2) if any open, high,
     * low price is missing.
     *
     * <b>Note:</b> Market closing times (e.g., weekends, holidays) will lead to
     * wrongly detected missing bars and should be ignored by the client.
     *
     * @param barSeries       the barSeries
     *                        酒吧系列
     * @param findOnlyNaNBars find only bars with undefined prices
     *                        仅查找价格未定义的柱
     *
     * @return the list of possibly missing bars
     *              可能缺失的酒吧列表
     */
    public static List<ZonedDateTime> findMissingBars(BarSeries barSeries, boolean findOnlyNaNBars) {
        List<Bar> bars = barSeries.getBarData();
        if (bars == null || bars.isEmpty())
            return new ArrayList<>();
        Duration duration = bars.iterator().next().getTimePeriod();
        List<ZonedDateTime> missingBars = new ArrayList<>();
        for (int i = 0; i < bars.size(); i++) {
            Bar bar = bars.get(i);
            if (!findOnlyNaNBars) {
                Bar nextBar = i + 1 < bars.size() ? bars.get(i + 1) : null;
                Duration incDuration = Duration.ZERO;
                if (nextBar != null) {
                    // market closing times are also treated as missing bars
                    // 收市时间也被视为缺失柱
                    while (nextBar.getBeginTime().minus(incDuration).isAfter(bar.getEndTime())) {
                        missingBars.add(bar.getEndTime().plus(incDuration).plus(duration));
                        incDuration = incDuration.plus(duration);
                    }
                }
            }
            boolean noFullData = bar.getOpenPrice().isNaN() || bar.getHighPrice().isNaN() || bar.getLowPrice().isNaN();
            if (noFullData) {
                missingBars.add(bar.getEndTime());
            }
        }
        return missingBars;
    }

    /**
     * Gets a new BarSeries cloned from the provided barSeries with bars converted
     * by conversionFunction. The returned barSeries inherits {@code beginIndex},
     * {@code endIndex} and {@code maximumBarCount} from the provided barSeries.
     *
     * @param barSeries the BarSeries
     * @param num       any instance of Num to determine its Num function; with
     *                  this, we can convert a {@link Number} to a {@link Num Num
     *                  implementation}
     * @return new cloned BarSeries with bars converted by the Num function of num
     */
    public static BarSeries convertBarSeries(BarSeries barSeries, Num num) {
        List<Bar> bars = barSeries.getBarData();
        if (bars == null || bars.isEmpty())
            return barSeries;
        List<Bar> convertedBars = new ArrayList<>();
        for (int i = barSeries.getBeginIndex(); i <= barSeries.getEndIndex(); i++) {
            Bar bar = bars.get(i);
            Function<Number, Num> conversionFunction = num.function();
            Bar convertedBar = new BaseBarConvertibleBuilder<>(conversionFunction::apply)
                    .timePeriod(bar.getTimePeriod())
                    .endTime(bar.getEndTime())
                    .openPrice(bar.getOpenPrice().getDelegate())
                    .highPrice(bar.getHighPrice().getDelegate())
                    .lowPrice(bar.getLowPrice().getDelegate())
                    .closePrice(bar.getClosePrice().getDelegate())
                    .volume(bar.getVolume().getDelegate())
                    .amount(bar.getAmount().getDelegate())
                    .trades(bar.getTrades())
                    .build();
            convertedBars.add(convertedBar);
        }
        BarSeries convertedBarSeries = new BaseBarSeries(barSeries.getName(), convertedBars, num);
        if (barSeries.getMaximumBarCount() > 0) {
            convertedBarSeries.setMaximumBarCount(barSeries.getMaximumBarCount());
        }

        return convertedBarSeries;
    }

    /**
     * Finds overlapping bars within barSeries.
     * 查找 barSeries 中的重叠条。
     *
     * @param barSeries the bar series with bar data
     *                  带有条形数据的条形系列
     * @return overlapping bars
     * @return 重叠条
     */
    public static List<Bar> findOverlappingBars(BarSeries barSeries) {
        List<Bar> bars = barSeries.getBarData();
        if (bars == null || bars.isEmpty())
            return new ArrayList<>();
        Duration period = bars.iterator().next().getTimePeriod();
        List<Bar> overlappingBars = new ArrayList<>();
        for (int i = 0; i < bars.size(); i++) {
            Bar bar = bars.get(i);
            Bar nextBar = i + 1 < bars.size() ? bars.get(i + 1) : null;
            if (nextBar != null) {
                if (bar.getEndTime().isAfter(nextBar.getBeginTime())
                        || bar.getBeginTime().plus(period).isBefore(nextBar.getBeginTime())) {
                    overlappingBars.add(nextBar);
                }
            }
        }
        return overlappingBars;
    }

    /**
     * Adds {@code newBars} to {@code barSeries}.
     *
     * @param barSeries the BarSeries
     *                  酒吧系列
     * @param newBars   the new bars to be added
     *                  要添加的新酒吧
     */
    public static void addBars(BarSeries barSeries, List<Bar> newBars) {
        if (newBars != null && !newBars.isEmpty()) {
            sortBars(newBars);
            for (Bar bar : newBars) {
                if (barSeries.isEmpty() || bar.getEndTime().isAfter(barSeries.getLastBar().getEndTime())) {
                    barSeries.addBar(bar);
                }
            }
        }
    }

    /**
     * Sorts the Bars by {@link Bar#getEndTime()} in ascending sequence (lower times
     * before higher times).
     *
     * @param bars the bars
     *             酒吧
     * @return the sorted bars
     *          排序的条形图
     */
    public static List<Bar> sortBars(List<Bar> bars) {
        if (!bars.isEmpty()) {
            Collections.sort(bars, BarSeriesUtils.sortBarsByTime);
        }
        return bars;
    }

}
