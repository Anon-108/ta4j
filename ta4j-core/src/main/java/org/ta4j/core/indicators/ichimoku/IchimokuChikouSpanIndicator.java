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
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

/**
 * Ichimoku clouds: Chikou Span indicator
 * 一目(均衡线) 云:赤口跨度指标
 *
 * 一般而言，在一目均衡云（Ichimoku Cloud）中，Chikou Span（或称为延迟线）是指当前价格移动到过去某一特定周期的一部分。Chikou Span 用于帮助识别价格走势的方向和确定趋势的强度。
 * Chikou Span 的计算方式很简单：就是将当前价格移动到过去某一特定周期的一部分。例如，如果我们设置 Chikou Span 为 26 期（在一目均衡云中通常是这样），那么 Chikou Span 就是将当前价格移动到过去 26 个周期之前的位置。
 * Chikou Span 可以用来帮助判断市场的趋势。当 Chikou Span 位于价格图表上方时，这可能表明市场处于上升趋势；而当 Chikou Span 位于价格图表下方时，则可能表明市场处于下降趋势。另外，一些交易者也会观察 Chikou Span 是否穿越价格图表，这可能被视为潜在的买入或卖出信号。
 * 总之，Chikou Span 是一目均衡云中的一个重要指标，它的位置可以提供有关价格走势和趋势方向的重要信息，有助于交易者做出更明智的交易决策。
 *
 * =======================================================
 *
 * Chikou Span Indicator（迟行线指标）是Ichimoku云图（Ichimoku Kinko Hyo）的一部分。它是通过将当前价格向后平移一定的周期数来绘制的，通常默认周期数为26个交易日。Chikou Span用于确认趋势和预测潜在的反转点。
 *
 * ### Chikou Span的计算方法
 *
 * Chikou Span的计算相对简单，它只是将当前的收盘价向后平移指定的周期数。公式如下：
 *
 * Chikou Span  = Current Close Price
 *
 * ### 应用
 *
 * 1. **趋势确认**：
 *    - 当Chikou Span在价格K线图上方时，表明当前市场处于上升趋势。
 *    - 当Chikou Span在价格K线图下方时，表明当前市场处于下降趋势。
 *
 * 2. **支撑和阻力**：
 *    - Chikou Span可以用来识别潜在的支撑和阻力水平。
 *    - 当Chikou Span接近过去的价格高点或低点时，这些点可以被视为潜在的支撑或阻力。
 *
 * 3. **交叉信号**：
 *    - 当Chikou Span从下方穿过过去的价格K线图时，这可能是一个买入信号。
 *    - 当Chikou Span从上方穿过过去的价格K线图时，这可能是一个卖出信号。
 *
 * ### 实际意义
 *
 * - **趋势分析**：Chikou Span通过将当前价格向后平移，提供了一种独特的方式来确认市场趋势，并帮助交易者更好地理解市场的整体趋势。
 * - **市场情绪**：通过观察Chikou Span的位置，交易者可以获得市场参与者的情绪和行为的线索。例如，当Chikou Span在价格上方时，市场情绪可能较为乐观，反之则较为悲观。
 * - **信号确认**：结合Ichimoku云图的其他指标，Chikou Span可以用来确认交易信号，提高交易决策的准确性。
 *
 * 总的来说，Chikou Span Indicator 是Ichimoku云图中的一个重要组成部分，通过将当前价格向后平移一定的周期数，帮助交易者确认趋势、识别支撑和阻力，并做出更明智的交易决策。
 *
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ichimoku_cloud">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ichimoku_cloud</a>
 */
public class IchimokuChikouSpanIndicator extends CachedIndicator<Num> {

    /**
     * The close price
     * * 收盘价
     */
    private final ClosePriceIndicator closePriceIndicator;

    /**
     * The time delay
     * * 时间延迟
     */
    private final int timeDelay;

    /**
     * Constructor.
     * 构造函数。
     *
     * @param series the series
     *               该系列
     */
    public IchimokuChikouSpanIndicator(BarSeries series) {
        this(series, 26);
    }

    /**
     * Constructor.
     * 构造函数
     *
     * @param series    the series
     *                  该系列
     * @param timeDelay the time delay (usually 26)
     *                  时间延迟（通常为 26）
     */
    public IchimokuChikouSpanIndicator(BarSeries series, int timeDelay) {
        super(series);
        this.closePriceIndicator = new ClosePriceIndicator(series);
        this.timeDelay = timeDelay;
    }

    @Override
    protected Num calculate(int index) {
        int spanIndex = index + timeDelay;
        if (spanIndex <= getBarSeries().getEndIndex()) {
            return closePriceIndicator.getValue(spanIndex);
        } else {
            return NaN.NaN;
        }
    }

}
