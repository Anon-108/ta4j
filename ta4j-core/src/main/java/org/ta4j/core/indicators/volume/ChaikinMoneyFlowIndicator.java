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
package org.ta4j.core.indicators.volume;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.CloseLocationValueIndicator;
import org.ta4j.core.indicators.helpers.VolumeIndicator;
import org.ta4j.core.num.Num;

/**
 * Chaikin Money Flow (CMF) indicator.
 * 柴金资金流向 (CMF) 指标。
 *
 * 查金资金流指标（Chaikin Money Flow，CMF）是一种用于衡量资金流动性的技术指标，旨在揭示资产价格背后的资金流入和流出情况。它由马克·查金（Marc Chaikin）开发，结合了价格和成交量的信息，以评估买卖压力和价格走势的持续性。
 *
 * CMF指标的计算过程如下：
 *
 * 1. 计算每个交易周期的成交量加权平均价格（Typical Price）：
 *     TP = ( 高 + 低 + 收 ) / 3
 *
 * 2. 计算每个交易周期的成交量流入（Money Flow）：
 *      MF = TP  * 成交量
 *
 * 3. 计算每个交易周期的资金流动指标（Money Flow Multiplier）：
 *    - 如果当日的典型价格高于前一日的典型价格，则资金流动指标为当日的成交量。
 *    - 如果当日的典型价格低于前一日的典型价格，则资金流动指标为负值，其绝对值为当日的成交量。
 *
 * 4. 计算累积资金流指标（Accumulation Distribution Line，ADL）：
 *    - 第一个周期的累积资金流指标等于第一个周期的资金流动指标。
 *    - 后续周期的累积资金流指标等于前一个周期的累积资金流指标加上当期的资金流动指标。
 *
 * CMF指标的数值范围在-1到1之间。正值表示资金流入，负值表示资金流出。较高的CMF值表明资金流入较多，可能与价格上涨相关；而较低的CMF值可能与价格下跌相关。
 *
 * CMF指标常用于确认价格趋势的持续性。例如，当CMF指标与价格走势形成背离时，可能意味着价格趋势的强度正在减弱，可能发生价格反转。因此，交易者可以利用CMF指标来辅助他们做出买卖决策，特别是结合其他技术分析工具一起使用。
 *
 * =============================================================
 *
 * `ChaikinMoneyFlowIndicator`（蔡金资金流量指标，简称CMF指标）是一种技术分析工具，用于衡量一定时间内的资金流量，以评估买卖压力。CMF指标通过结合价格和交易量，来确定市场的强弱和潜在趋势的变化。
 *
 * ### 蔡金资金流量指标的计算步骤
 *
 * 1. **计算货币流量乘数（Money Flow Multiplier, MFM）**：
 *    MFM} = (Close - Low) - (High - Close) / High - Low
 *    该乘数反映了当期收盘价在价格范围中的相对位置。
 *
 * 2. **计算货币流量量（Money Flow Volume, MFV）**：
 *    MFV} = MFM} * Volume}
 *    通过将货币流量乘数与交易量相乘，得到交易期内的资金流量。
 *
 * 3. **计算CMF值**：
 *    CMF  = \sum MFV _ n  /  sum Volume _ n
 *    在给定的时间窗口（通常为20天）内，将所有交易期的货币流量量求和，并除以同一时期内的总交易量。
 *
 * ### 使用场景
 *
 * 1. **趋势确认**：CMF指标可以用来确认价格趋势。如果价格上升且CMF为正值，表明买方力量强劲，趋势可能持续。如果价格上升而CMF为负值，可能预示着趋势反转。
 *
 * 2. **识别买卖信号**：当CMF指标从负值转为正值时，可能是买入信号；反之，当CMF从正值转为负值时，可能是卖出信号。
 *
 * 3. **判断市场强弱**：CMF指标为正值时，表示资金流入市场，市场相对强势；CMF指标为负值时，表示资金流出市场，市场相对弱势。
 *
 * ### 总结
 *
 * `ChaikinMoneyFlowIndicator` 是一种结合价格和交易量的技术分析工具，通过计算和分析CMF指标，投资者和交易者可以更好地评估市场的买卖压力和趋势强度。该指标对于确认趋势、识别买卖信号以及判断市场强弱非常有用。
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_money_flow_cmf">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_money_flow_cmf"</a>
 * @see <a href=
 *      "http://www.fmlabs.com/reference/default.htm?url=ChaikinMoneyFlow.htm">
 *      http://www.fmlabs.com/reference/default.htm?url=ChaikinMoneyFlow.htm</a>
 */
public class ChaikinMoneyFlowIndicator extends CachedIndicator<Num> {

    private final CloseLocationValueIndicator clvIndicator;
    private final VolumeIndicator volumeIndicator;
    private final int barCount;

    public ChaikinMoneyFlowIndicator(BarSeries series, int barCount) {
        super(series);
        this.barCount = barCount;
        this.clvIndicator = new CloseLocationValueIndicator(series);
        this.volumeIndicator = new VolumeIndicator(series, barCount);
    }

    @Override
    protected Num calculate(int index) {
        int startIndex = Math.max(0, index - barCount + 1);
        Num sumOfMoneyFlowVolume = numOf(0);
        for (int i = startIndex; i <= index; i++) {
            sumOfMoneyFlowVolume = sumOfMoneyFlowVolume.plus(getMoneyFlowVolume(i));
        }
        Num sumOfVolume = volumeIndicator.getValue(index);

        return sumOfMoneyFlowVolume.dividedBy(sumOfVolume);
    }

    /**
     * @param index the bar index
     *              条形索引
     * @return the money flow volume for the i-th period/bar
     * @return 第 i 个周期/柱的资金流量
     */
    private Num getMoneyFlowVolume(int index) {
        return clvIndicator.getValue(index).multipliedBy(getBarSeries().getBar(index).getVolume());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }
}
