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
package org.ta4j.core.indicators.helpers;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

/**
 * Difference Change Indicator.
 * 差异变化指标。/差异百分比
 *
 * Get the difference in percentage from the last time the threshold was reached.
 * 获取与上次达到阈值时的百分比差异。
 *
 * Or if you don't pass the threshold you will always just get the difference percentage from the precious value.
 * 或者，如果您没有通过阈值，您将始终只能获得宝贵价值的差异百分比。
 *
 *
 * "差异变化指标"（Difference Change Indicator）通常指的是用于衡量两个时间点之间差异变化的指标。这种指标可以帮助分析价格、指标或其他数据在不同时间段内的变化幅度和趋势。
 * 具体的差异变化指标可以有很多种，取决于所比较的数据类型和分析目的。以下是一些可能的示例：
 *
 * 1. **价格变化指标**：用于衡量两个时间点之间价格的变化幅度，例如计算两个交易日收盘价之间的价格差异或百分比变化。
 *
 * 2. **技术指标变化指标**：用于衡量两个时间点之间技术指标数值的变化，例如计算两个交易日MACD线或RSI指标之间的差异或百分比变化。
 *
 * 3. **成交量变化指标**：用于衡量两个时间点之间成交量的变化幅度，例如计算两个交易日成交量之间的差异或百分比变化。
 *
 * 4. **波动率变化指标**：用于衡量价格波动率在不同时间段内的变化，例如计算两个交易日内价格波动范围的差异或百分比变化。
 *
 * 这些差异变化指标可以提供有关市场变化和趋势的重要信息，帮助分析师和交易者更好地理解市场情况并做出相应的决策。在使用差异变化指标时，通常需要结合其他分析方法和指标，以便更全面地评估市场状况。
 *
 */
public class DifferencePercentage extends CachedIndicator<Num> {

    private final Indicator<Num> indicator;
    /**百分比阈值*/
    private final Num percentageThreshold;
    private final Num hundred;
    /**最后通知*/
    private Num lastNotification;

    public DifferencePercentage(Indicator<Num> indicator) {
        this(indicator, indicator.numOf(0));
    }

    public DifferencePercentage(Indicator<Num> indicator, Number percentageThreshold) {
        this(indicator, indicator.numOf(percentageThreshold));
    }

    public DifferencePercentage(Indicator<Num> indicator, Num percentageThreshold) {
        super(indicator);
        this.indicator = indicator;
        this.percentageThreshold = percentageThreshold;
        hundred = numOf(100);
    }

    @Override
    protected Num calculate(int index) {
        Num value = indicator.getValue((index));
        if (lastNotification == null) {
            lastNotification = value;
            return NaN.NaN;
        }

        Num changeFraction = value.dividedBy(lastNotification);
        Num changePercentage = fractionToPercentage(changeFraction);

        if (changePercentage.abs().isGreaterThanOrEqual(percentageThreshold)) {
            lastNotification = value;
        }

        return changePercentage;
    }

    private Num fractionToPercentage(Num changeFraction) {
        return changeFraction.multipliedBy(hundred).minus(hundred);
    }
}
