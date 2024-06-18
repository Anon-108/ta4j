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
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

/**
 * Ichimoku clouds: Senkou Span B (Leading Span B) indicator
 * Ichimoku 云： Senkou Span B （Leading Span B） 指标
 *
 * Senkou Span B（先行线B）是一目均衡云指标的一个重要组成部分，用于显示将来的支撑和阻力水平。与 Senkou Span A 不同，Senkou Span B 是在一定时间段内的最高价和最低价之间的中点，并且向前移动到未来一定时间段内。
 *
 * Senkou Span B 的计算方式是将最高价和最低价的中点作为当前线，然后将这个值移动到未来一定时间段内。通常情况下，一目均衡云中的标准设置是将这个值移动到未来的 26 个周期。
 *
 * Senkou Span B 的计算公式如下：
 *
 * Senkou Span B = (Highest High + Lowest Low ) / 2
 *
 * 其中，“Highest High” 是一定时间段内的最高价，“Lowest Low” 是一定时间段内的最低价。
 *
 * Senkou Span B 在一目均衡云中通常被用作未来的支撑和阻力水平。当 Senkou Span B 处于价格图表的上方时，它可能作为一个未来的阻力水平；而当 Senkou Span B 处于价格图表的下方时，则可能作为一个未来的支撑水平。
 *
 * 总的来说，Senkou Span B 是一目均衡云中的一个重要指标，它用于显示未来的支撑和阻力水平，并提供了一些未来价格走势的参考，有助于交易者制定更明智的交易策略。
 *
 * ============================================================================
 *
 * Senkou Span B Indicator（先行B线指标）是Ichimoku云图（Ichimoku Kinko Hyo）中的一个关键组成部分，用于确定市场的中长期趋势。与Senkou Span A一起，Senkou Span B形成了Ichimoku云图中的“云”（Kumo），帮助识别支撑和阻力区域以及市场趋势。
 *
 * ### Senkou Span B的计算方法
 *
 * Senkou Span B的计算公式如下：
 *
 * Senkou Span B = Highest High + Lowest Low / 2-*
 *
 * 其中，Highest High和Lowest Low是指定周期内的最高价和最低价。默认的计算周期为52个交易日。然后将结果向前平移26个周期（默认设置，可以根据需要调整）。
 *
 * ### 应用
 *
 * 1. **趋势判断**：
 *    - 当价格位于云的上方时，市场处于上升趋势。
 *    - 当价格位于云的下方时，市场处于下降趋势。
 *    - 当价格位于云的内部时，市场处于震荡或过渡阶段。
 *
 * 2. **支撑和阻力**：
 *    - 云的上边界（通常是Senkou Span A）和下边界（Senkou Span B）作为动态支撑和阻力水平。
 *    - 在上升趋势中，Senkou Span A和Senkou Span B之间的区域可以作为支撑区域。
 *    - 在下降趋势中，Senkou Span A和Senkou Span B之间的区域可以作为阻力区域。
 *
 * 3. **强度和弱度**：
 *    - 云的厚度反映了市场的波动性和强度。较厚的云表示市场波动较大，较薄的云表示市场波动较小。
 *    - 当Senkou Span A从下向上穿越Senkou Span B时，形成一个看涨信号，称为“黄金交叉”。
 *    - 当Senkou Span A从上向下穿越Senkou Span B时，形成一个看跌信号，称为“死亡交叉”。
 *
 * ### 实际意义
 *
 * - **市场结构**：Senkou Span B与Senkou Span A一起形成了Ichimoku云图中的云，帮助交易者识别市场结构和潜在的支撑/阻力区域。
 * - **趋势强度**：云的厚度和相对位置可以帮助交易者评估当前趋势的强度和可能的反转点。
 * - **信号确认**：结合Ichimoku云图的其他指标，Senkou Span B可以用来确认交易信号，增加交易决策的准确性。
 *
 * 总的来说，Senkou Span B Indicator 是Ichimoku云图中的关键组成部分，通过计算更长周期内的最高价和最低价的平均值并向前平移，帮助交易者识别市场趋势、支撑和阻力水平以及市场的整体结构。
 *
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ichimoku_cloud">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ichimoku_cloud</a>
 */
public class IchimokuSenkouSpanBIndicator extends CachedIndicator<Num> {

    // ichimoku avg line indicator
    // ichimoku 平均线指标
    IchimokuLineIndicator lineIndicator;

    /**
     * Displacement on the chart (usually 26)
     * 图表上的位移（通常为 26）
     */
    private final int offset;

    /**
     * Constructor.
     * 
     * @param series the series
     */
    public IchimokuSenkouSpanBIndicator(BarSeries series) {

        this(series, 52, 26);
    }

    /**
     * Constructor.
     * 
     * @param series   the series
     * @param barCount the time frame (usually 52)
     *                 时间范围（通常为 52）
     */
    public IchimokuSenkouSpanBIndicator(BarSeries series, int barCount) {

        this(series, barCount, 26);
    }

    /**
     * Constructor.
     * 
     * @param series   the series
     * @param barCount the time frame (usually 52)
     *                 时间范围（通常为 52）
     * @param offset   displacement on the chart
     *                 图表上的位移
     */
    public IchimokuSenkouSpanBIndicator(BarSeries series, int barCount, int offset) {

        super(series);
        lineIndicator = new IchimokuLineIndicator(series, barCount);
        this.offset = offset;
    }

    @Override
    protected Num calculate(int index) {
        int spanIndex = index - offset + 1;
        if (spanIndex >= getBarSeries().getBeginIndex()) {
            return lineIndicator.getValue(spanIndex);
        } else {
            return NaN.NaN;
        }
    }
}
