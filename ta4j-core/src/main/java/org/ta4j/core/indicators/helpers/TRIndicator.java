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
package org.ta4j.core.indicators.helpers;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * True range indicator.
 * 真实范围指示器。
 *
 * True Range Indicator（真实波动幅度指标）是由J. Welles Wilder在他的经典著作《New Concepts in Technical Trading Systems》中引入的一种技术分析工具，用于衡量市场波动性。它
 *  通过考虑当日最高价、最低价和前一日收盘价来计算市场的真实波动范围，反映了市场在一段时间内的真实波动情况。
 *
 * ### True Range Indicator 的计算方法
 *
 * True Range（TR）是通过以下三者之间的最大值来计算的：
 *
 * 1. 当前最高价与当前最低价的差值。
 * 2. 当前最高价与前一日收盘价的差值的绝对值。
 * 3. 当前最低价与前一日收盘价的差值的绝对值。
 *
 * 公式如下：
 * TR = max({High} - {Low}, |{High} - {Previous Close}|, |{Low} - {Previous Close}|)
 *
 * ### 应用
 *
 * 1. **波动性分析**：True Range Indicator 是用于衡量市场波动性的重要工具。当市场波动性增加时，TR 值会变大；当市场波动性减小时，TR 值会变小。
 * 2. **止损设置**：交易者可以使用TR值来设置止损位，以防止市场波动带来的风险。
 * 3. **结合其他指标**：TR常与平均真实波动幅度（ATR）一起使用，以更好地了解市场波动性。
 *
 * ### 真实波动幅度指标的实际意义
 *
 * 1. **波动性度量**：TR 是市场波动性的一个直接度量，可以帮助交易者了解市场的波动情况。
 * 2. **市场情绪**：当TR 值较高时，表示市场波动较大，情绪较为紧张或不稳定；当TR值较低时，表示市场波动较小，情绪较为平静或稳定。
 * 3. **风险管理**：通过了解市场的真实波动幅度，交易者可以更好地进行风险管理和仓位控制。
 *
 * 总之，True Range Indicator 是一个重要的技术分析工具，通过考虑市场价格的最高点、最低点和前一日收盘价，提供了对市场波动性的全面衡量，帮助交易者更好地理解和应对市场风险。
 */
public class TRIndicator extends CachedIndicator<Num> {

    public TRIndicator(BarSeries series) {
        super(series);
    }

    @Override
    protected Num calculate(int index) {
        Num ts = getBarSeries().getBar(index).getHighPrice().minus(getBarSeries().getBar(index).getLowPrice());
        Num ys = index == 0 ? numOf(0)
                : getBarSeries().getBar(index).getHighPrice().minus(getBarSeries().getBar(index - 1).getClosePrice());
        Num yst = index == 0 ? numOf(0)
                : getBarSeries().getBar(index - 1).getClosePrice().minus(getBarSeries().getBar(index).getLowPrice());
        return ts.abs().max(ys.abs()).max(yst.abs());
    }
}
