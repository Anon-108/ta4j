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
package org.ta4j.core.indicators.donchian;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * * https://www.investopedia.com/terms/d/donchianchannels.asp
 *
 * 好的，下面详细解释 DonchianChannelMiddleIndicator（Donchian 通道中线指标）。
 *
 * ### 什么是 Donchian Channel？
 *
 * Donchian Channel 是一种技术分析工具，用于识别市场趋势和潜在交易机会。它由三个部分组成：
 *
 * 1. **上轨（Upper Band）**：某一特定时间周期内的最高价。
 * 2. **中线（Middle Line）**：上轨和下轨的平均值。
 * 3. **下轨（Lower Band）**：某一特定时间周期内的最低价。
 *
 * ### DonchianChannelMiddleIndicator 的定义
 *
 * DonchianChannelMiddleIndicator 是 Donchian Channel 的中线。它表示特定时间周期内最高价和最低价的平均值。
 *
 * ### 计算公式
 *
 * \[
 * \text{Donchian Channel Middle}_t = \frac{\text{Donchian Channel Upper}_t + \text{Donchian Channel Lower}_t}{2}
 * \]
 *
 * 其中：
 * - \(\text{Donchian Channel Upper}_t\) 是第 t 天的上轨（在选定时间周期内的最高价）。
 * - \(\text{Donchian Channel Lower}_t\) 是第 t 天的下轨（在选定时间周期内的最低价）。
 *
 * ### 计算步骤
 *
 * 1. **选择时间周期**：决定用于计算 Donchian Channel 的时间周期（例如 20 天）。
 * 2. **收集数据**：对于每一天，收集前 n 天的价格数据。
 * 3. **计算上轨和下轨**：
 *    - 上轨（最高价）：在前 n 天内的最高价。
 *    - 下轨（最低价）：在前 n 天内的最低价。
 * 4. **计算中线**：将上轨和下轨的平均值作为中线。
 *
 * ### 计算实例
 *
 * 假设选择的时间周期是 20 天，并且我们要计算第 21 天的 DonchianChannelMiddleIndicator：
 *
 * 1. **时间周期**：20 天
 * 2. **数据**：前 20 天的价格数据，例如：
 *    \[
 *    [50, 52, 48, 47, 51, 53, 49, 46, 50, 55, 54, 48, 45, 47, 49, 50, 51, 53, 52, 50]
 *    \]
 * 3. **上轨**：在这 20 天内的最高价是 55。
 * 4. **下轨**：在这 20 天内的最低价是 45。
 * 5. **中线**：根据公式计算：
 *    \[
 *    \text{Donchian Channel Middle}_{21} = \frac{55 + 45}{2} = 50
 *    \]
 *
 * 因此，第 21 天的 DonchianChannelMiddleIndicator 值为 50。
 *
 * ### 用途
 *
 * 1. **趋势识别**：
 *    - 中线可以帮助交易者识别市场的中期趋势。如果价格持续在中线上方运行，表明市场可能处于上升趋势中；如果价格持续在中线下方运行，表明市场可能处于下降趋势中。
 *
 * 2. **交易信号**：
 *    - **买入信号**：如果价格从下轨反弹并突破中线，这可能表明市场可能开始上升，可以考虑买入。
 *    - **卖出信号**：如果价格从上轨回落并跌破中线，这可能表明市场可能开始下跌，可以考虑卖出。
 *
 * ### 实际应用
 *
 * 在实际应用中，交易者可以将 Donchian Channel 的中线与其他技术指标结合使用，以提高交易信号的准确性。例如，可以结合移动平均线（MA）、相对强弱指标（RSI）或布林带（Bollinger Bands）来确认买入或卖出信号。
 *
 * ### 总结
 *
 * DonchianChannelMiddleIndicator 是 Donchian Channel 的中线，通过显示特定时间周期内最高价和最低价的平均值，帮助交易者识别市场的中期趋势和潜在的交易机会。结合其他技术指标，交易者可以更全面地分析市场动态，制定更有效的交易策略。
 */
public class DonchianChannelMiddleIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final DonchianChannelLowerIndicator lower;
    private final DonchianChannelUpperIndicator upper;

    /**
     * Constructor.
     * 
     * @param series   the bar series
     * @param barCount the time frame
     */
    public DonchianChannelMiddleIndicator(BarSeries series, int barCount) {
        super(series);
        this.barCount = barCount;
        this.lower = new DonchianChannelLowerIndicator(series, barCount);
        this.upper = new DonchianChannelUpperIndicator(series, barCount);
    }

    @Override
    protected Num calculate(int index) {
        return (this.lower.getValue(index).plus(this.upper.getValue(index))).dividedBy(numOf(2));
    }

    @Override
    public int getUnstableBars() {
        return barCount;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "barCount: " + barCount;
    }

}
