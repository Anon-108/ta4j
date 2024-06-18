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
 * Gain indicator.
 * 增益指标。
 *
 * LossIndicator是一个用于计算给定时间段内的损失（负变化）的技术指标。在技术分析中，损失指的是价格下降的部分。这个指标通常在计算相对强弱指数（RSI）时使用，因为RSI需要计算平均涨幅和平均跌幅。
 *
 * 计算损失指标
 * 损失指标的计算步骤如下：
 *
 * 计算价格变化：
 * Price Change
 * =
 * Current Close
 * −
 * Previous Close
 * Price Change=Current Close−Previous Close
 *
 * 计算损失：
 * 如果价格变化为负值，则损失为该负值的绝对值，否则损失为0。
 *
 * 计算指定周期内的平均损失：
 * 使用简单移动平均线（SMA）或指数移动平均线（EMA）来平滑损失。
 */
public class LossIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> indicator;

    public LossIndicator(Indicator<Num> indicator) {
        super(indicator);
        this.indicator = indicator;
    }

    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            return numOf(0);
        }
        if (indicator.getValue(index).isLessThan(indicator.getValue(index - 1))) {
            return indicator.getValue(index - 1).minus(indicator.getValue(index));
        } else {
            return numOf(0);
        }
    }
}
