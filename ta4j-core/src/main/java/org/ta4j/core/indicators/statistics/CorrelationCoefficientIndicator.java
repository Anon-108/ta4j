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
package org.ta4j.core.indicators.statistics;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Correlation coefficient indicator.
 * 相关系数指标。
 *
 * CorrelationCoefficientIndicator 是一种技术分析指标，用于测量两个不同金融资产或两个时间序列之间的线性相关性。相关系数的值介于 -1 和 1 之间：
 *
 * 1 表示完全正相关，即当一个资产价格上涨时，另一个资产价格也上涨。
 * -1 表示完全负相关，即当一个资产价格上涨时，另一个资产价格下跌。
 * 0 表示没有线性相关性。
 *
 * 在金融市场中，相关系数指标可以用于：
 * - 衡量不同资产价格之间的相关性，例如股票价格、货币对、商品价格等。
 * - 分析资产价格与其他因素（如利率、通胀率、公司财务指标等）之间的关联程度。
 * - 辅助构建投资组合，通过选择相关性较低的资产来实现分散化。
 *
 * 使用场景
 * 投资组合管理：通过分析资产之间的相关性，可以构建更好的投资组合，以减少风险和增加回报。
 * 对冲策略：在对冲策略中，了解两个资产之间的相关性有助于制定有效的对冲方案。
 * 交易策略：某些交易策略依赖于相关性，例如统计套利等。
 *
 * 相关系数指标的计算通常使用统计软件或函数进行，它可以帮助交易者了解市场中不同资产之间的关联程度，从而指导投资决策和风险管理。
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:correlation_coeffici">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:correlation_coeffici</a>
 */
public class CorrelationCoefficientIndicator extends CachedIndicator<Num> {

    private final VarianceIndicator variance1;
    private final VarianceIndicator variance2;
    private final CovarianceIndicator covariance;

    /**
     * Constructor.
     *
     * @param indicator1 the first indicator
     *                   第一个指标
     * @param indicator2 the second indicator
     *                   第二个指标
     * @param barCount   the time frame
     *                   时间范围
     */
    public CorrelationCoefficientIndicator(Indicator<Num> indicator1, Indicator<Num> indicator2, int barCount) {
        super(indicator1);
        variance1 = new VarianceIndicator(indicator1, barCount);
        variance2 = new VarianceIndicator(indicator2, barCount);
        covariance = new CovarianceIndicator(indicator1, indicator2, barCount);
    }

    @Override
    protected Num calculate(int index) {
        Num cov = covariance.getValue(index);
        Num var1 = variance1.getValue(index);
        Num var2 = variance2.getValue(index);
        Num multipliedSqrt = var1.multipliedBy(var2).sqrt();
        return cov.dividedBy(multipliedSqrt);

    }
}
