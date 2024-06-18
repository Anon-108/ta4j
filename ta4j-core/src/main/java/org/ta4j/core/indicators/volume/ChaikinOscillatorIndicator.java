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
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.num.Num;

/**
 * Chaikin Oscillator.
 * 柴金振荡器。
 *
 * 查金振荡器（Chaikin Oscillator）是一种基于资金流动性的技术指标，用于衡量资产价格的动量和趋势的变化。它是由马克·查金（Marc Chaikin）开发的，结合了价格和成交量的信息，旨在帮助识别买卖信号和价格趋势的转折点。
 *
 * 查金振荡器的计算过程如下：
 *
 * 1. 计算累积分布线（Accumulation Distribution Line，ADL）：
 *    - 使用成交量加权平均价格（Typical Price）来计算每个交易周期的成交量流入。
 *    - 累积分布线是根据成交量流入的累积值计算得到的。
 *
 * 2. 计算快速移动平均线（Fast Moving Average）：
 *    - 使用ADL计算快速移动平均线，通常是使用较短的时间周期。
 *
 * 3. 计算慢速移动平均线（Slow Moving Average）：
 *    - 使用ADL计算慢速移动平均线，通常是使用较长的时间周期。
 *
 * 4. 计算查金振荡器值：
 *    - 查金振荡器值等于快速移动平均线减去慢速移动平均线的差值。
 *
 * 查金振荡器的数值可正可负。当查金振荡器值为正时，表示快速移动平均线高于慢速移动平均线，可能暗示着价格上涨的动量正在增强；当查金振荡器值为负时，表示快速移动平均线低于慢速移动平均线，可能暗示着价格下跌的动量正在增强。
 *
 * 交易者可以利用查金振荡器来识别价格趋势的转折点和交易信号。例如，当查金振荡器从负值转变为正值时，可能意味着价格上涨的趋势即将开始；相反，当查金振荡器从正值转变为负值时，可能意味着价格下跌的趋势即将开始。
 *
 * ==========================================================
 * `Chaikin Oscillator Indicator`（柴金震荡指标）是一种技术分析工具，用于衡量市场中的买卖压力和潜在的趋势变化。它结合了价格和交易量数据，通过对累积/派发线（Accumulation/Distribution Line, ADL）进行快速和慢速的指数移动平均线（EMA）处理，旨在识别资金流动的变化。
 *
 * ### 柴金震荡指标的计算步骤
 *
 * 1. **计算累积/派发线（ADL）**：
 *    - ADL反映了市场中资金的累计进出情况，通常通过每个交易期的货币流量量（Money Flow Volume, MFV）累加得到。
 *
 * 2. **计算ADL的快速EMA和慢速EMA**：
 *    - 快速EMA（通常为3天）和慢速EMA（通常为10天）的计算步骤如下：
 *      \[ \text{Fast EMA} = \text{EMA}(ADL, \text{fast period}) \]
 *      \[ \text{Slow EMA} = \text{EMA}(ADL, \text{slow period}) \]
 *
 * 3. **计算柴金震荡指标**：
 *    - 柴金震荡指标是快速EMA与慢速EMA之差：
 *      \[ \text{Chaikin Oscillator} = \text{Fast EMA} - \text{Slow EMA} \]
 *
 * ### 使用场景
 *
 * 1. **识别趋势变化**：柴金震荡指标可以帮助识别市场中的趋势变化。如果指标从负值变为正值，表明买方力量增强，可能是买入信号；如果指标从正值变为负值，表明卖方力量增强，可能是卖出信号。
 *
 * 2. **确认交易信号**：柴金震荡指标可以用来确认其他技术分析工具发出的买卖信号。例如，如果某一技术指标发出买入信号，而柴金震荡指标也从负值转为正值，则信号的可靠性更高。
 *
 * 3. **分析市场动量**：柴金震荡指标可以用于分析市场动量和资金流动，识别市场中的买卖压力变化。
 *
 * ### 总结
 *
 * `Chaikin Oscillator Indicator` 是一种结合价格和交易量的技术分析工具，通过对累积/派发线（ADL）进行快速和慢速的指数移动平均线（EMA）处理来衡量市场中的买卖压力。通过计算和分析柴金震荡指标，投资者和交易者可以更好地理解市场趋势，识别潜在的趋势变化信号，并制定有效的交易策略。该指标对于确认交易信号和分析市场动量特别有用。
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_oscillator">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_oscillator</a>
 */
public class ChaikinOscillatorIndicator extends CachedIndicator<Num> {

    private final EMAIndicator emaShort;
    private final EMAIndicator emaLong;

    /**
     * Constructor.
     *
     * @param series        the {@link BarSeries}
     *                      {@link BarSeries}
     * @param shortBarCount (usually 3)
     *                      （通常是 3 个）
     * @param longBarCount  (usually 10)
     *                      （通常为 10 个）
     */
    public ChaikinOscillatorIndicator(BarSeries series, int shortBarCount, int longBarCount) {
        super(series);
        this.emaShort = new EMAIndicator(new AccumulationDistributionIndicator(series), shortBarCount);
        this.emaLong = new EMAIndicator(new AccumulationDistributionIndicator(series), longBarCount);
    }

    /**
     * Constructor.
     *
     * @param series the {@link BarSeries}
     *               {@link BarSeries}
     */
    public ChaikinOscillatorIndicator(BarSeries series) {
        this(series, 3, 10);
    }

    @Override
    protected Num calculate(int index) {
        return emaShort.getValue(index).minus(emaLong.getValue(index));
    }
}
