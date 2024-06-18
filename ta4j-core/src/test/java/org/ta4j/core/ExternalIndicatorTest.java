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
package org.ta4j.core;

import org.ta4j.core.num.Num;

public interface ExternalIndicatorTest {

    /**
     * Gets the BarSeries used by an external indicator calculator.
     * * 获取外部指标计算器使用的 BarSeries。
     * 
     * @return BarSeries from the external indicator calculator
     * @return BarSeries 从外部指标计算器
     *
     * @throws Exception if the external calculator throws an Exception
     * * @throws Exception 如果外部计算器抛出异常
     */
    BarSeries getSeries() throws Exception;

    /**
     * Sends indicator parameters to an external indicator calculator and returns the externally calculated indicator.
     * * 将指标参数发送到外部指标计算器并返回外部计算的指标。
     * 
     * @param params indicator parameters
     *               指标参数
     *
     * @return Indicator<Num> from the external indicator calculator
     * * @return Indicator<Num> 来自外部指标计算器
     *
     * @throws Exception if the external calculator throws an Exception
     * * @throws Exception 如果外部计算器抛出异常
     */
    Indicator<Num> getIndicator(Object... params) throws Exception;

}
