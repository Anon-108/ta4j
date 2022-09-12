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
package org.ta4j.core.aggregator;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.num.Num;

/**
 * Bar aggregator basing on duration.
 * 基于持续时间的条形聚合器。
 */
public class DurationBarAggregator implements BarAggregator {

    /**
     * Target time period to aggregate
     * 要聚合的目标时间段
     */
    private final Duration timePeriod;
    private final boolean onlyFinalBars;

    /**
     * Duration basing bar aggregator. Only bars with elapsed time (final bars) will be created.
     * 基于持续时间的条形聚合器。 只会创建经过时间的柱（最终柱）。
     *
     * @param timePeriod time period to aggregate
     *                   @param timePeriod 要聚合的时间段
     */
    public DurationBarAggregator(Duration timePeriod) {
        this(timePeriod, true);
    }

    /**
     * Duration basing bar aggregator
     * 基于持续时间的条形聚合器
     *
     * @param timePeriod    time period to aggregate
     *                      @param timePeriod 要聚合的时间段
     * @param onlyFinalBars if true only bars with elapsed time (final bars) will be  created, otherwise also pending bars
     *                      @param onlyFinalBars 如果为 true，则只会创建经过时间的柱（最终柱），否则也会创建待处理柱
     */
    public DurationBarAggregator(Duration timePeriod, boolean onlyFinalBars) {
        this.timePeriod = timePeriod;
        this.onlyFinalBars = onlyFinalBars;
    }

    /**
     * Aggregates a list of bars by <code>timePeriod</code>.The new
     * <code>timePeriod</code> must be a multiplication of the actual time period.
     ** 按 <code>timePeriod</code> 聚合柱形列表。新的
     *       * <code>timePeriod</code> 必须是实际时间段的乘积。
     *
     * @param bars the actual bars
     *             @param 酒吧实际酒吧
     * @return the aggregated bars with new <code>timePeriod</code>
     * @return 带有新 <code>timePeriod</code> 的聚合柱
     */
    @Override
    public List<Bar> aggregate(List<Bar> bars) {
        final List<Bar> aggregated = new ArrayList<>();
        if (bars.isEmpty()) {
            return aggregated;
        }
        final Bar firstBar = bars.get(0);
        // get the actual time period
        // 获取实际时间段
        final Duration actualDur = firstBar.getTimePeriod();
        // check if new timePeriod is a multiplication of actual time period
        // 检查新的 timePeriod 是否是实际时间段的乘积
        final boolean isMultiplication = timePeriod.getSeconds() % actualDur.getSeconds() == 0;
        if (!isMultiplication) {
            throw new IllegalArgumentException(
                    "Cannot aggregate bars: the new timePeriod must be a multiplication of the actual timePeriod. 无法聚合条形：新时间段必须是实际时间段的乘积。");
        }

        int i = 0;
        final Num zero = firstBar.getOpenPrice().numOf(0);
        while (i < bars.size()) {
            Bar bar = bars.get(i);
            final ZonedDateTime beginTime = bar.getBeginTime();
            final Num open = bar.getOpenPrice();
            Num high = bar.getHighPrice();
            Num low = bar.getLowPrice();

            Num close = null;
            Num volume = zero;
            Num amount = zero;
            long trades = 0;
            Duration sumDur = Duration.ZERO;

            while (sumDur.compareTo(timePeriod) < 0) {
                if (i < bars.size()) {
                    bar = bars.get(i);

                    if (high == null || bar.getHighPrice().isGreaterThan(high)) {
                        high = bar.getHighPrice();
                    }
                    if (low == null || bar.getLowPrice().isLessThan(low)) {
                        low = bar.getLowPrice();
                    }
                    close = bar.getClosePrice();

                    if (bar.getVolume() != null) {
                        volume = volume.plus(bar.getVolume());
                    }
                    if (bar.getAmount() != null) {
                        amount = amount.plus(bar.getAmount());
                    }
                    if (bar.getTrades() != 0) {
                        trades = trades + bar.getTrades();
                    }

                }
                sumDur = sumDur.plus(actualDur);
                i++;
            }

            if (!onlyFinalBars || i <= bars.size()) {
                final Bar aggregatedBar = new BaseBar(timePeriod, beginTime.plus(timePeriod), open, high, low, close,
                        volume, amount, trades);
                aggregated.add(aggregatedBar);
            }
        }

        return aggregated;
    }
}
