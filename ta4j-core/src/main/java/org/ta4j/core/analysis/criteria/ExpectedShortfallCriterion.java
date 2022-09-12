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
package org.ta4j.core.analysis.criteria;

import java.util.Collections;
import java.util.List;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.Returns;
import org.ta4j.core.num.Num;

/**
 * Expected Shortfall criterion.
 * * 预期短缺标准。
 *
 * Measures the expected shortfall of the strategy log-return time-series.
 * * 衡量策略日志返回时间序列的预期缺口。
 *
 * @see <a href=
 *      "https://en.wikipedia.org/wiki/Expected_shortfall">https://en.wikipedia.org/wiki/Expected_shortfall</a>
 *
 */
public class ExpectedShortfallCriterion extends AbstractAnalysisCriterion {
    /**
     * Confidence level as absolute value (e.g. 0.95)
     * * 绝对值的置信水平（例如 0.95）
     */
    private final double confidence;

    /**
     * Constructor
     * 构造函数
     *
     * @param confidence the confidence level
     *                   * @param confidence 置信水平
     */
    public ExpectedShortfallCriterion(double confidence) {
        this.confidence = confidence;
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        if (position != null && position.getEntry() != null && position.getExit() != null) {
            Returns returns = new Returns(series, position, Returns.ReturnType.LOG);
            return calculateES(returns, confidence);
        }
        return series.numOf(0);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        Returns returns = new Returns(series, tradingRecord, Returns.ReturnType.LOG);
        return calculateES(returns, confidence);
    }

    /**
     * Calculates the Expected Shortfall on the return series
     * * 计算回报系列的预期缺口
     * 
     * @param returns    the corresponding returns
     *                   * @param 返回对应的返回值
     * @param confidence the confidence level
     *                   * @param confidence 置信水平
     * @return the relative Expected Shortfall
     * * @return 相对预期缺口
     */
    private static Num calculateES(Returns returns, double confidence) {
        // select non-NaN returns
        // 选择非 NaN 返回
        List<Num> returnRates = returns.getValues().subList(1, returns.getSize() + 1);
        Num zero = returns.numOf(0);
        Num expectedShortfall = zero;
        if (!returnRates.isEmpty()) {
            // F(x_var) >= alpha (=1-confidence)
            // F(x_var) >= alpha (=1-置信度)
            int nInBody = (int) (returns.getSize() * confidence);
            int nInTail = returns.getSize() - nInBody;

            // calculate average tail loss
            // 计算平均尾部损失
            Collections.sort(returnRates);
            List<Num> tailEvents = returnRates.subList(0, nInTail);
            Num sum = zero;
            for (int i = 0; i < nInTail; i++) {
                sum = sum.plus(tailEvents.get(i));
            }
            expectedShortfall = sum.dividedBy(returns.numOf(nInTail));

            // ES is non-positive
            // ES 是非正数
            if (expectedShortfall.isGreaterThan(zero)) {
                expectedShortfall = zero;
            }
        }
        return expectedShortfall;
    }

    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }
}
