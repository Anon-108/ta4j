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

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Returns the previous (n-th) value of an indicator
 * 返回指标的前一个（第 n 个）值
 *
 * Previous Value Indicator（前值指标）是一种用于获取某个技术指标在之前某个周期的数值的工具。它广泛用于技术分析中，帮助交易者比较当前指标值和之前某个周期的指标值，以确定市场趋势和交易信号。
 * 主要应用
 * 趋势分析：
 *
 * 通过比较当前指标值和之前某个周期的指标值，可以识别价格或指标的趋势。
 * 例如，如果当前的相对强弱指数（RSI）值高于之前的RSI值，可能表明市场处于上升趋势。
 * 信号生成：
 *
 * Previous Value Indicator可以用于生成交易信号。
 * 例如，当当前价格超过前一个周期的高点时，可能是一个买入信号；当当前价格低于前一个周期的低点时，可能是一个卖出信号。
 * 均线策略：
 *
 * 在均线策略中，Previous Value Indicator可以用于比较当前均线值和之前均线值，以识别趋势和反转信号。
 * 例如，如果当前的移动平均线（MA）值高于之前的MA值，可能表明市场处于上升趋势。
 * 计算方法
 * 选择周期：选择要回溯的周期数。例如，前一天、前两天等。
 * 获取前值：根据选择的周期数，获取相应的技术指标的前值。
 * 实际意义
 * 前值的比较：通过比较当前值和前值，交易者可以更好地理解市场的动态，并做出更明智的交易决策。
 * 增强信号的可靠性：结合前值指标，可以增强其他技术指标生成的信号的可靠性。例如，结合移动平均线的前值和当前值，可以更准确地识别市场趋势。
 * 总之，Previous Value Indicator是一个简单而实用的技术分析工具，通过获取和比较技术指标的前值，帮助交易者识别市场趋势、生成交易信号并制定有效的交易策略。
 *
 */
public class PreviousValueIndicator extends CachedIndicator<Num> {

    private final int n;
    private Indicator<Num> indicator;

    /**
     * Constructor.
     *
     * @param indicator the indicator of which the previous value should be   calculated
     *                       *
     */
    public PreviousValueIndicator(Indicator<Num> indicator) {
        this(indicator, 1);
    }

    /**
     * Constructor.
     * 构造函数
     *
     * @param indicator the indicator of which the previous value should be   calculated
     *                  应计算其先前值的指标
     * @param n         parameter defines the previous n-th value
     *                  参数定义前第 n 个值
     */
    public PreviousValueIndicator(Indicator<Num> indicator, int n) {
        super(indicator);
        if (n < 1) {
            throw new IllegalArgumentException("n must be positive number, but was n 必须是正数，但是是: " + n);
        }
        this.n = n;
        this.indicator = indicator;
    }

    protected Num calculate(int index) {
        int previousValue = Math.max(0, (index - n));
        return this.indicator.getValue(previousValue);
    }

    @Override
    public String toString() {
        final String nInfo = n == 1 ? "" : "(" + n + ")";
        return getClass().getSimpleName() + nInfo + "[" + this.indicator + "]";
    }
}