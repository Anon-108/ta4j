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
import org.ta4j.core.indicators.helpers.LowPriceIndicator;
import org.ta4j.core.indicators.helpers.LowestValueIndicator;
import org.ta4j.core.num.Num;

/***
 * https://www.investopedia.com/terms/d/donchianchannels.asp
 *
 * 当然，我可以详细解释 DonchianChannelLowerIndicator 及其计算方法和应用。
 *
 * ### 什么是 Donchian Channel？
 *
 * Donchian Channel 是由 Richard Donchian 发明的技术分析工具，广泛用于识别市场中的趋势和潜在交易机会。它由三个部分组成：
 *
 * 1. **上轨（Upper Band）**：某一特定时间周期内的最高价。
 * 2. **中线（Middle Line）**：上轨和下轨的平均值。
 * 3. **下轨（Lower Band）**：某一特定时间周期内的最低价。
 *
 * ### DonchianChannelLowerIndicator 的定义
 *
 * DonchianChannelLowerIndicator 指的是 Donchian Channel 的下轨，用于显示特定时间周期内的最低价。
 *
 * ### 计算公式
 *
 * \[
 * \text{Donchian Channel Lower}_t = \min(\text{Price}_{t-n+1}, \text{Price}_{t-n+2}, ..., \text{Price}_t)
 * \]
 *
 * 其中：
 * - \(\text{Price}_t\) 是第 t 天的价格
 * - \(n\) 是选定的时间周期（例如 20 天）
 * - \(\min\) 表示在给定周期内的最低价格
 *
 * ### 计算步骤
 *
 * 1. **选择时间周期**：决定用于计算下轨的时间周期（例如 20 天）。
 * 2. **收集数据**：对于每一天，收集前 n 天的价格数据。
 * 3. **计算最低价**：在每个时间点 t，计算前 n 天内的最低价格。
 * 4. **绘制下轨**：将这些最低价格连接起来，形成 Donchian 通道的下轨。
 *
 * ### 计算实例
 *
 * 假设选择的时间周期是 20 天，并且我们要计算第 21 天的 DonchianChannelLowerIndicator：
 *
 * 1. **时间周期**：20 天
 * 2. **数据**：前 20 天的价格数据，例如：
 *    \[
 *    [50, 52, 48, 47, 51, 53, 49, 46, 50, 55, 54, 48, 45, 47, 49, 50, 51, 53, 52, 50]
 *    \]
 * 3. **最低价**：在这 20 天内的最低价是 45。
 *
 * 因此，第 21 天的 DonchianChannelLowerIndicator 值为 45。
 *
 * ### 用途
 *
 * 1. **趋势识别**：
 *    - 下轨的上升趋势表明市场可能处于上升趋势中。
 *    - 下轨的下降趋势表明市场可能处于下降趋势中。
 *
 * 2. **交易信号**：
 *    - **买入信号**：如果价格跌破下轨，这可能表明市场超卖，可能会反弹。
 *    - **卖出信号**：如果价格远离下轨且上升，可能表明市场强劲，可以持有多头头寸。
 *
 * ### 实际应用
 *
 * 在实际应用中，交易者可以将 Donchian Channel 和其他技术分析工具结合使用，以增加交易信号的准确性。例如，可以结合相对强弱指标（RSI）或移动平均线（MA）来确认买入或卖出信号。
 *
 * ### 总结
 *
 * DonchianChannelLowerIndicator 是一种有效的技术分析工具，通过显示特定时间周期内的最低价，帮助交易者识别市场趋势和潜在的买入机会。结合其他技术指标，交易者可以更全面地分析市场动态，制定更有效的交易策略。
 *
 */
public class DonchianChannelLowerIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final LowPriceIndicator lowPrice;
    private final LowestValueIndicator lowestPrice;

    /**
     * Constructor.
     * 
     * @param series   the bar series
     * @param barCount the time frame
     */
    public DonchianChannelLowerIndicator(BarSeries series, int barCount) {
        super(series);
        this.barCount = barCount;
        this.lowPrice = new LowPriceIndicator(series);
        this.lowestPrice = new LowestValueIndicator(this.lowPrice, barCount);
    }

    @Override
    protected Num calculate(int index) {
        return this.lowestPrice.getValue(index);
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
