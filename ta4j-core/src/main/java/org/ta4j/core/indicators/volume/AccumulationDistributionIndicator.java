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
import org.ta4j.core.indicators.RecursiveCachedIndicator;
import org.ta4j.core.indicators.helpers.CloseLocationValueIndicator;
import org.ta4j.core.num.Num;

/**
 * Accumulation-distribution indicator.
 * 累积分布指标。
 *
 * `AccumulationDistributionIndicator`（累积/派发指标，简称A/D指标）是一种技术分析工具，用于评估市场中的买卖压力。通过结合价格和交易量来确定市场趋势，
 *  A/D指标帮助交易者识别资金流入和流出市场的情况，从而判断趋势的强弱和潜在的趋势反转。
 *
 * ### 累积/派发指标的计算步骤
 *
 * 1. **货币流量乘数（Money Flow Multiplier, MFM）**：
 *    MFM = (Close - Low) - (High - Close) / High - Low
 *    这是根据每个交易期的高价、低价和收盘价计算的一个比率，反映了当期收盘价在价格范围中的相对位置。
 *
 * 2. **货币流量量（Money Flow Volume, MFV）**：
 *    MFV = MFM * Volume
 *    这是通过将货币流量乘数与交易量相乘得出的值，表示交易期内的资金流量。
 *
 * 3. **累积/派发线（Accumulation/Distribution Line, ADL）**：
 *    ADL = Previous ADL + Current MFV
 *    通过累积每个交易期的货币流量量，得到累积/派发线。该线显示了市场中资金的累计进出情况。
 *
 * ### 使用场景
 *
 * 1. **趋势识别**：A/D指标有助于识别市场的主要趋势，特别是在价格变动与成交量之间存在背离时。例如，如果价格上升但A/D指标下降，可能预示价格上涨缺乏成交量支持，趋势可能逆转。
 *
 * 2. **确认信号**：可以用来确认其他技术分析工具发出的买卖信号。如果其他指标显示买入信号，而A/D指标也在上升，则信号的可靠性更高。
 *
 * 3. **资金流分析**：通过分析A/D指标，可以了解资金在市场中的流入和流出情况，帮助识别买卖压力。大量资金流入通常意味着买方力量强，而大量资金流出则表示卖方力量强。
 *
 * ### 总结
 *
 * `AccumulationDistributionIndicator` 是一种结合价格和交易量的技术分析工具，通过计算和分析A/D指标，投资者和交易者可以更好地理解市场趋势，识别潜在的趋势反转信号，
 *  TODO 并制定有效的交易策略。该指标对于评估市场买卖压力和确认交易信号特别有用。
 *
 */
public class AccumulationDistributionIndicator extends RecursiveCachedIndicator<Num> {

    private final CloseLocationValueIndicator clvIndicator;

    public AccumulationDistributionIndicator(BarSeries series) {
        super(series);
        this.clvIndicator = new CloseLocationValueIndicator(series);
    }

    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            return numOf(0);
        }

        // Calculating the money flow multiplier
        // 计算资金流量乘数
        Num moneyFlowMultiplier = clvIndicator.getValue(index);

        // Calculating the money flow volume
        // 计算资金流量
        Num moneyFlowVolume = moneyFlowMultiplier.multipliedBy(getBarSeries().getBar(index).getVolume());

        return moneyFlowVolume.plus(getValue(index - 1));
    }
}
