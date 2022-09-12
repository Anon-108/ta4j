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
package org.ta4j.core.rules;

import org.ta4j.core.Indicator;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 * Indicator-falling-indicator rule.
 * * 指标下降指标规则。
 *
 * Satisfied when the values of the {@link Indicator indicator} decrease within the barCount.
 * * 当{@link Indicator indicator} 的值在 barCount 内减小时满足。
 */
public class IsFallingRule extends AbstractRule {

    /** The actual indicator
     * 实际指标*/

    private final Indicator<Num> ref;
    /** The barCount 酒吧计数 */
    private final int barCount;
    /** The minimum required strenght of the falling
     * 下落所需的最低强度 */
    private double minStrenght;

    /**
     * Constructor.
     * 
     * @param ref      the indicator
     *                 指标
     *
     * @param barCount the time frame
     *                 时间范围
     */
    public IsFallingRule(Indicator<Num> ref, int barCount) {
        this(ref, barCount, 1.0);
    }

    /**
     * Constructor.
     * 
     * @param ref         the indicator
     *                    指标
     * @param barCount    the time frame
     *                    时间范围
     * @param minStrenght the minimum required falling strength (between '0' and '1', e.g. '1' for strict falling)
     *                    最低要求的坠落强度（在“0”和“1”之间，例如“1”表示严格坠落）
     */
    public IsFallingRule(Indicator<Num> ref, int barCount, double minStrenght) {
        this.ref = ref;
        this.barCount = barCount;
        this.minStrenght = minStrenght;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (minStrenght >= 1) {
            minStrenght = 0.99;
        }

        int count = 0;
        for (int i = Math.max(0, index - barCount + 1); i <= index; i++) {
            if (ref.getValue(i).isLessThan(ref.getValue(Math.max(0, i - 1)))) {
                count += 1;
            }
        }

        double ratio = count / (double) barCount;

        final boolean satisfied = ratio >= minStrenght;
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }
}
