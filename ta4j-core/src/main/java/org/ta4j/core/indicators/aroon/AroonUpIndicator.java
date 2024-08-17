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

import static org.ta4j.core.num.NaN.NaN;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.HighestValueIndicator;
import org.ta4j.core.num.Num;

/**
 * Aroon up indicator.
 * * 阿隆向上指标。
 *
 *AroonUpIndicator 是一种技术分析工具，用于衡量市场在特定时间周期内达到高点的频率和强度。它是 Aroon 指标的一部分，Aroon 指标由 Tushar Chande 于 1995 年开发，
 * 用于识别市场趋势的变化和趋势的强度。
 *
 * ### AroonUpIndicator 的定义
 *
 * AroonUpIndicator 主要用于识别价格在特定时间周期内达到最高点的频率。它通过计算从最近一次最高价到当前时间的天数来反映市场趋势的强弱。
 *
 * ### 计算公式
 *
 * \[
 * \text{AroonUp} = \frac{n - \text{Days Since Highest}}{n} \times 100
 * \]
 *
 * 其中：
 * - \(n\) 是选定的时间周期（例如 14 天）。
 * - \(\text{Days Since Highest}\) 是从最近一次最高价到当前时间的天数。
 *
 * ### 计算步骤
 *
 * 1. **选择时间周期**：决定用于计算 AroonUp 的时间周期（例如 14 天）。
 * 2. **收集数据**：对于每一天，收集前 n 天的价格数据。
 * 3. **找出最高价**：在每个时间点 t，找出前 n 天内的最高价格，并计算从最高价到当前时间的天数。
 * 4. **计算 AroonUp**：根据公式计算 AroonUp 指标。
 *
 * ### 计算实例
 *
 * 假设选择的时间周期是 14 天，并且我们要计算第 15 天的 AroonUpIndicator：
 *
 * 1. **时间周期**：14 天
 * 2. **数据**：前 14 天的价格数据，例如：
 *    \[
 *    [50, 52, 48, 47, 51, 53, 49, 46, 50, 55, 54, 48, 45, 47]
 *    \]
 * 3. **找出最高价**：在这 14 天内，最高价是 55，出现在第 10 天。
 * 4. **计算天数**：从第 10 天到第 15 天的天数是 5 天。
 * 5. **计算 AroonUp**：根据公式计算：
 *    \[
 *    \text{AroonUp} = \frac{14 - 5}{14} \times 100 \approx 64.29
 *    \]
 *
 * 因此，第 15 天的 AroonUpIndicator 值约为 64.29。
 *
 * ### 用途
 *
 * 1. **趋势识别**：
 *    - 高 AroonUp 值（接近 100）表明价格最近达到最高点，市场可能处于上升趋势中。
 *    - 低 AroonUp 值（接近 0）表明价格在较长时间内没有达到最高点，市场可能处于下降趋势中。
 *
 * 2. **交易信号**：
 *    - **买入信号**：当 AroonUp 上升并接近 100，表明市场可能进入上升趋势，可以考虑买入。
 *    - **卖出信号**：当 AroonUp 下降并接近 0，表明市场可能进入下降趋势，可以考虑卖出。
 *
 * ### 实际应用
 *
 * 在实际应用中，交易者可以将 AroonUpIndicator 与 AroonDownIndicator 结合使用，以更全面地分析市场趋势。
 * AroonDownIndicator 衡量市场在特定时间周期内达到最低点的频率，两者结合可以更准确地识别市场的趋势变化。
 *
 * ### 总结
 *
 * AroonUpIndicator 是一种有效的技术分析工具，通过衡量市场在特定时间周期内达到最高点的频率，帮助交易者识别市场的上升趋势和潜在的交易机会。
 * 结合其他技术指标，交易者可以更全面地分析市场动态，制定更有效的交易策略。
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon">chart_school:technical_indicators:aroon</a>
 */
public class AroonUpIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final HighestValueIndicator highestHighPriceIndicator;
    private final Indicator<Num> highPriceIndicator;
    private final Num hundred;
    private final Num barCountNum;

    /**
     * Constructor.
     *
     * @param highPriceIndicator the indicator for the high price (default  {@link HighPriceIndicator})
     *                           * @param highPriceIndicator 高价指标（默认 {@link HighPriceIndicator}）
     * @param barCount           the time frame 時間範圍
     */
    public AroonUpIndicator(Indicator<Num> highPriceIndicator, int barCount) {
        super(highPriceIndicator);
        this.barCount = barCount;
        this.highPriceIndicator = highPriceIndicator;
        this.hundred = hundred();
        this.barCountNum = numOf(barCount);
        // + 1 needed for last possible iteration in loop
        // + 1 循环中最后一次可能的迭代需要
        this.highestHighPriceIndicator = new HighestValueIndicator(highPriceIndicator, barCount + 1);
    }

    /**
     * Default Constructor with {@code highPriceIndicator} =
     * {@link HighPriceIndicator}.
     *
     * @param series   the bar series
     *                 酒吧系列
     * @param barCount the time frame
     *                 时间范围
     */
    public AroonUpIndicator(BarSeries series, int barCount) {
        this(new HighPriceIndicator(series), barCount);
    }

    @Override
    protected Num calculate(int index) {
        if (getBarSeries().getBar(index).getHighPrice().isNaN())
            return NaN;

        // Getting the number of bars since the highest close price
        // 获取自最高收盘价以来的柱数
        int endIndex = Math.max(0, index - barCount);
        int nbBars = 0;
        for (int i = index; i > endIndex; i--) {
            if (highPriceIndicator.getValue(i).isEqual(highestHighPriceIndicator.getValue(index))) {
                break;
            }
            nbBars++;
        }

        return numOf(barCount - nbBars).dividedBy(barCountNum).multipliedBy(hundred);
    }

    @Override
    public int getUnstableBars() {
        return barCount;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }

}
