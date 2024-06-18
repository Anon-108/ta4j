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
import org.ta4j.core.indicators.adx.MinusDIIndicator;
import org.ta4j.core.indicators.adx.PlusDIIndicator;
import org.ta4j.core.num.Num;

/**
 * DX indicator.
 * DX 指标。
 *
 * DX指标（Directional Movement Index，趋向运动指数）是用于衡量市场趋势方向强度的技术分析指标。它是Wilder的一部分，也被称为平均趋向指数（Average Directional Index，ADX）系统。
 *
 * ADX指标通常由三条线组成：
 * 1. **+DI（Positive Directional Indicator，正向趋向指标）**：用于测量上升趋势的强度。
 * 2. **-DI（Negative Directional Indicator，负向趋向指标）**：用于测量下降趋势的强度。
 * 3. **ADX**：用于衡量市场趋势的强度，而不考虑其方向。
 *
 * ADX的计算是通过对+DI和-DI之间的差异进行指数平滑移动平均来实现的。它的取值范围通常在0到100之间，较高的数值表示趋势较强，而较低的数值则表示趋势较弱。
 *
 * ADX指标的主要应用包括：
 * - **趋势确认**：当ADX高于某个阈值（通常为25或30）时，表示市场处于趋势状态，可以使用+DI和-DI来确定趋势的方向。
 * - **趋势强度**：ADX的数值可以用来衡量趋势的强度，较高的ADX值表示趋势较强，而较低的ADX值则表示趋势较弱。
 * - **趋势反转**：当ADX开始下降时，可能表示趋势即将结束或发生反转。
 *
 * 在使用ADX指标时，通常需要结合其他技术指标和分析方法，以便更全面地评估市场状况，并做出相应的交易决策。
 *
 *============================================
 * DX指标（Directional Movement Index），也称为方向运动指数，是由Welles Wilder开发的一种技术分析工具，用于衡量市场趋势的强度。它是平均方向指数（ADX）的一部分，用于识别价格是否在强趋势中。
 *
 * DX的计算步骤
 * 计算方向运动（Directional Movement，DM）：
 *
 * 如果当前最高价高于前一天最高价且当前最低价低于前一天最低价，则记作+DM和-DM。
 * +DM = 当前最高价 - 前一天最高价。
 * -DM = 前一天最低价 - 当前最低价。
 * 计算真实波动幅度（True Range，TR）：
 *
 * TR = max(当前最高价 - 当前最低价, 当前最高价 - 前一天收盘价, 前一天收盘价 - 当前最低价)。
 * 计算+DI和-DI：
 *
 * +DI = 100 * (+DM的N天指数移动平均值 / TR的N天指数移动平均值)。
 * -DI = 100 * (-DM的N天指数移动平均值 / TR的N天指数移动平均值)。
 * 计算DX：
 *
 * DX = 100 * (|+DI - -DI| / (+DI + -DI))。
 *
 * DX指标的应用
 * 趋势强度识别：DX值越高，表示趋势强度越大，反之亦然。
 * 交易信号：一般与其他指标结合使用，例如与ADX结合，以确定交易信号和市场趋势。
 * 总的来说，DX指标是一个用于衡量市场趋势强度的有用工具，有助于交易者更好地理解市场的动向并制定相应的交易策略。
 */
public class DXIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final PlusDIIndicator plusDIIndicator;
    private final MinusDIIndicator minusDIIndicator;

    public DXIndicator(BarSeries series, int barCount) {
        super(series);
        this.barCount = barCount;
        plusDIIndicator = new PlusDIIndicator(series, barCount);
        minusDIIndicator = new MinusDIIndicator(series, barCount);
    }

    @Override
    protected Num calculate(int index) {
        Num pdiValue = plusDIIndicator.getValue(index);
        Num mdiValue = minusDIIndicator.getValue(index);
        if (pdiValue.plus(mdiValue).equals(numOf(0))) {
            return numOf(0);
        }
        return pdiValue.minus(mdiValue).abs().dividedBy(pdiValue.plus(mdiValue)).multipliedBy(numOf(100));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }
}
