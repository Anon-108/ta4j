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
package org.ta4j.core.indicators.ichimoku;

import org.ta4j.core.BarSeries;

/**
 * Ichimoku clouds: Tenkan-sen (Conversion line) indicator
 * Ichimoku 云： Tenkan-sen （转换线） 指标
 *
 * Tenkan-sen（转换线）是一目均衡云指标的一个组成部分，它用于显示短期市场趋势的变化。Tenkan-sen 是一段特定时间内的最高价和最低价的平均值，并且向前移动到未来一定时间段内。
 *
 * Tenkan-sen 的计算方式是将一定时间段内的最高价和最低价的平均值作为当前线，然后将这个值移动到未来一定时间段内。通常情况下，一目均衡云中的标准设置是将这个值移动到未来的 9 个周期。
 *
 * Tenkan-sen 的计算公式如下：
 *
 * Tenkan-sen = (Highest High + Lowest Low ) / 2
 *
 * 其中，“Highest High” 是一定时间段内的最高价，“Lowest Low” 是一定时间段内的最低价。
 *
 * Tenkan-sen 在一目均衡云中通常被用作短期市场趋势的参考线。当 Tenkan-sen 上升时，这可能表示短期趋势为上升；而当 Tenkan-sen 下降时，则可能表示短期趋势为下降。
 *
 * 总的来说，Tenkan-sen 是一目均衡云中的一个重要指标，它用于显示短期市场趋势的变化，并提供了一些未来价格走势的参考，有助于交易者更好地理解市场的动态并制定相应的交易策略。
 *
 * =======================================================================
 *
 * Tenkan-Sen Indicator（转换线指标）是Ichimoku云图（Ichimoku Kinko Hyo）中的一个关键组成部分，用于确定市场的短期趋势。
 *  Tenkan-Sen的计算基于一段时间内的最高价和最低价的平均值。它不仅帮助交易者了解当前趋势，还能提供潜在的交易信号和支撑/阻力水平。
 *
 * ### Tenkan-Sen的计算方法
 *
 * Tenkan-Sen的计算公式如下：
 *
 *  Tenkan-Sen  =  Highest High  +  Lowest Low / 2
 *
 * 其中，Highest High是指定周期内的最高价，Lowest Low是指定周期内的最低价。默认的计算周期为9个交易日。
 *
 * ### 应用
 *
 * 1. **趋势分析**：
 *    - 当价格位于Tenkan-Sen上方时，表明市场处于上升趋势。
 *    - 当价格位于Tenkan-Sen下方时，表明市场处于下降趋势。
 *
 * 2. **支撑和阻力**：
 *    - 在上升趋势中，Tenkan-Sen可以作为动态支撑线。
 *    - 在下降趋势中，Tenkan-Sen可以作为动态阻力线。
 *
 * 3. **交易信号**：
 *    - 当价格从下方穿越Tenkan-Sen时，这是一个潜在的买入信号。
 *    - 当价格从上方穿越Tenkan-Sen时，这是一个潜在的卖出信号。
 *
 * 4. **与其他Ichimoku指标的交叉**：
 *    - Tenkan-Sen与Kijun-Sen的交叉可以产生重要的交易信号。
 *    - 当Tenkan-Sen从下方穿越Kijun-Sen时，这是一个看涨信号，称为“黄金交叉”。
 *    - 当Tenkan-Sen从上方穿越Kijun-Sen时，这是一个看跌信号，称为“死亡交叉”。
 *
 * ### 实际意义
 *
 * - **短期趋势判断**：Tenkan-Sen作为一个短期趋势指标，可以帮助交易者快速判断当前市场的短期趋势。
 * - **市场强度评估**：通过观察价格与Tenkan-Sen的位置关系，交易者可以评估市场的强度和动量。
 * - **辅助决策**：结合其他Ichimoku指标（如Kijun-Sen、Senkou Span A和Senkou Span B），Tenkan-Sen可以为交易者提供更全面的市场分析和交易决策支持。
 *
 * 总的来说，Tenkan-Sen Indicator 是Ichimoku云图中的重要组成部分，通过计算指定周期内的最高价和最低价的平均值，提供了对市场短期趋势的有效判断，帮助交易者识别交易信号和支撑/阻力水平。
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ichimoku_cloud">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ichimoku_cloud</a>
 */
public class IchimokuTenkanSenIndicator extends IchimokuLineIndicator {

    /**
     * Constructor.
     * 
     * @param series the series
     */
    public IchimokuTenkanSenIndicator(BarSeries series) {
        this(series, 9);
    }

    /**
     * Constructor.
     * 
     * @param series   the series
     * @param barCount the time frame (usually 9)
     *                 时间范围（通常为 9）
     */
    public IchimokuTenkanSenIndicator(BarSeries series, int barCount) {
        super(series, barCount);
    }
}
