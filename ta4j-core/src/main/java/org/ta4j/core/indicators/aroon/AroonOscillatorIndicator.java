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
package org.ta4j.core.indicators.aroon;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Aroon Oscillator.
 * * 阿隆指标 振荡器。
 *
 *AroonOscillatorIndicator 是一种技术分析工具，用于衡量市场趋势的强度和方向。它基于 AroonUp 和 AroonDown 指标，通过计算这两个指标之间的差值来判断市场的趋势。
 *
 * ### AroonOscillatorIndicator 的定义
 *
 * AroonOscillatorIndicator 是 AroonUpIndicator 和 AroonDownIndicator 之间的差值，用于表示市场趋势的强弱和方向。
 *
 * ### 计算公式
 *
 * \[
 * \text{Aroon Oscillator} = \text{AroonUp} - \text{AroonDown}
 * \]
 *
 * 其中：
 * - \(\text{AroonUp}\) 表示选定时间周期内市场达到最高点的频率。
 * - \(\text{AroonDown}\) 表示选定时间周期内市场达到最低点的频率。
 *
 * ### 计算步骤
 *
 * 1. **选择时间周期**：决定用于计算 Aroon 指标的时间周期（例如 14 天）。
 * 2. **计算 AroonUp**：
 *    \[
 *    \text{AroonUp} = \frac{n - \text{Days Since Highest}}{n} \times 100
 *    \]
 *    其中 \(n\) 是时间周期，\(\text{Days Since Highest}\) 是从最近一次最高价到当前时间的天数。
 * 3. **计算 AroonDown**：
 *    \[
 *    \text{AroonDown} = \frac{n - \text{Days Since Lowest}}{n} \times 100
 *    \]
 *    其中 \(\text{Days Since Lowest}\) 是从最近一次最低价到当前时间的天数。
 * 4. **计算 Aroon Oscillator**：
 *    \[
 *    \text{Aroon Oscillator} = \text{AroonUp} - \text{AroonDown}
 *    \]
 *
 * ### 计算实例
 *
 * 假设选择的时间周期是 14 天，并且我们要计算第 15 天的 AroonOscillator：
 *
 * 1. **时间周期**：14 天
 * 2. **数据**：前 14 天的价格数据，例如：
 *    \[
 *    [50, 52, 48, 47, 51, 53, 49, 46, 50, 55, 54, 48, 45, 47]
 *    \]
 * 3. **计算 AroonUp**：假设最近一次最高价 55 出现在第 10 天，从第 10 天到第 15 天的天数是 5 天。
 *    \[
 *    \text{AroonUp} = \frac{14 - 5}{14} \times 100 \approx 64.29
 *    \]
 * 4. **计算 AroonDown**：假设最近一次最低价 45 出现在第 13 天，从第 13 天到第 15 天的天数是 2 天。
 *    \[
 *    \text{AroonDown} = \frac{14 - 2}{14} \times 100 \approx 85.71
 *    \]
 * 5. **计算 Aroon Oscillator**：
 *    \[
 *    \text{Aroon Oscillator} = 64.29 - 85.71 \approx -21.43
 *    \]
 *
 * 因此，第 15 天的 AroonOscillator 值约为 -21.43。
 *
 * ### 用途
 *
 * 1. **趋势识别**：
 *    - 正值（AroonUp > AroonDown）：表明市场可能处于上升趋势。
 *    - 负值（AroonUp < AroonDown）：表明市场可能处于下降趋势。
 *
 * 2. **趋势强度**：
 *    - 越高的正值表示上升趋势越强。
 *    - 越低的负值表示下降趋势越强。
 *
 * 3. **交易信号**：
 *    - **买入信号**：当 Aroon Oscillator 从负值上升到正值，表明市场可能从下降趋势转为上升趋势。
 *    - **卖出信号**：当 Aroon Oscillator 从正值下降到负值，表明市场可能从上升趋势转为下降趋势。
 *
 * ### 实际应用
 *
 * 在实际应用中，交易者可以将 AroonOscillatorIndicator 与其他技术分析工具结合使用，以提高交易信号的准确性。
 * 例如，可以结合移动平均线（MA）、相对强弱指标（RSI）或布林带（Bollinger Bands）来确认买入或卖出信号。
 *
 * ### 总结
 *
 * AroonOscillatorIndicator 是一种有效的技术分析工具，通过计算 AroonUp 和 AroonDown 之间的差值，帮助交易者识别市场趋势的强度和方向。
 * 结合其他技术指标，交易者可以更全面地分析市场动态，制定更有效的交易策略。
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon_oscillator">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon_oscillator</a>
 */
public class AroonOscillatorIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final AroonUpIndicator aroonUpIndicator;
    private final AroonDownIndicator aroonDownIndicator;

    /**
     * Constructor.
     * 
     * @param series   the bar series
     * @param barCount the number of periods used for the indicators
     */
    public AroonOscillatorIndicator(BarSeries series, int barCount) {
        super(series);
        this.barCount = barCount;
        this.aroonUpIndicator = new AroonUpIndicator(series, barCount);
        this.aroonDownIndicator = new AroonDownIndicator(series, barCount);
    }

    @Override
    protected Num calculate(int index) {
        return aroonUpIndicator.getValue(index).minus(aroonDownIndicator.getValue(index));
    }

    @Override
    public int getUnstableBars() {
        return barCount;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }

    /** @return the {@link #aroonUpIndicator} */
    public AroonUpIndicator getAroonUpIndicator() {
        return aroonUpIndicator;
    }

    /** @return the {@link #aroonDownIndicator} */
    public AroonDownIndicator getAroonDownIndicator() {
        return aroonDownIndicator;
    }

}
