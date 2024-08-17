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
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.HighestValueIndicator;
import org.ta4j.core.num.Num;

/**
 * * https://www.investopedia.com/terms/d/donchianchannels.asp
 *
 * 好的，下面详细解释 DonchianChannelUpperIndicator（Donchian 通道上轨指标）。
 *
 * ### 什么是 Donchian Channel？
 *
 * Donchian Channel 是一种技术分析工具，用于识别市场趋势和潜在交易机会。它由三个部分组成：
 *
 * 1. **上轨（Upper Band）**：某一特定时间周期内的最高价。
 * 2. **中线（Middle Line）**：上轨和下轨的平均值。
 * 3. **下轨（Lower Band）**：某一特定时间周期内的最低价。
 *
 * ### DonchianChannelUpperIndicator 的定义
 *
 * DonchianChannelUpperIndicator 是 Donchian Channel 的上轨，用于显示特定时间周期内的最高价。
 *
 * ### 计算公式
 *
 * \[
 * \text{Donchian Channel Upper}_t = \max(\text{Price}_{t-n+1}, \text{Price}_{t-n+2}, ..., \text{Price}_t)
 * \]
 *
 * 其中：
 * - \(\text{Price}_t\) 是第 t 天的价格
 * - \(n\) 是选定的时间周期（例如 20 天）
 * - \(\max\) 表示在给定周期内的最高价格
 *
 * ### 计算步骤
 *
 * 1. **选择时间周期**：决定用于计算上轨的时间周期（例如 20 天）。
 * 2. **收集数据**：对于每一天，收集前 n 天的价格数据。
 * 3. **计算最高价**：在每个时间点 t，计算前 n 天内的最高价格。
 * 4. **绘制上轨**：将这些最高价格连接起来，形成 Donchian 通道的上轨。
 *
 * ### 计算实例
 *
 * 假设选择的时间周期是 20 天，并且我们要计算第 21 天的 DonchianChannelUpperIndicator：
 *
 * 1. **时间周期**：20 天
 * 2. **数据**：前 20 天的价格数据，例如：
 *    \[
 *    [50, 52, 48, 47, 51, 53, 49, 46, 50, 55, 54, 48, 45, 47, 49, 50, 51, 53, 52, 50]
 *    \]
 * 3. **最高价**：在这 20 天内的最高价是 55。
 *
 * 因此，第 21 天的 DonchianChannelUpperIndicator 值为 55。
 *
 * ### 用途
 *
 * 1. **趋势识别**：
 *    - 上轨的上升趋势表明市场可能处于上升趋势中。
 *    - 上轨的下降趋势表明市场可能处于下降趋势中。
 *
 * 2. **交易信号**：
 *    - **卖出信号**：如果价格突破上轨，这可能表明市场超买，可能会出现回调。
 *    - **持有信号**：如果价格持续在上轨附近运行，可能表明市场强劲，适合持有多头头寸。
 *
 * ### 实际应用
 *
 * 在实际应用中，交易者可以将 Donchian Channel 的上轨与其他技术分析工具结合使用，以提高交易信号的准确性。例如，可以结合相对强弱指标（RSI）或移动平均线（MA）来确认买入或卖出信号。
 *
 * ### 总结
 *
 * DonchianChannelUpperIndicator 是一种有效的技术分析工具，通过显示特定时间周期内的最高价，帮助交易者识别市场趋势和潜在的卖出机会。结合其他技术指标，交易者可以更全面地分析市场动态，制定更有效的交易策略。
 *
 */
public class DonchianChannelUpperIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final HighPriceIndicator highPrice;
    private final HighestValueIndicator highestPrice;

    /**
     * Constructor.
     * 
     * @param series   the bar series
     * @param barCount the time frame
     */
    public DonchianChannelUpperIndicator(BarSeries series, int barCount) {
        super(series);
        this.barCount = barCount;
        this.highPrice = new HighPriceIndicator(series);
        this.highestPrice = new HighestValueIndicator(this.highPrice, barCount);
    }

    @Override
    protected Num calculate(int index) {
        return this.highestPrice.getValue(index);
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
