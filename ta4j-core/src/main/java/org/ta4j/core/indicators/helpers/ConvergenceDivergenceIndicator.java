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
package org.ta4j.core.indicators.helpers;

import org.ta4j.core.Indicator;
import org.ta4j.core.Rule;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.statistics.CorrelationCoefficientIndicator;
import org.ta4j.core.indicators.statistics.SimpleLinearRegressionIndicator;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.IsFallingRule;
import org.ta4j.core.rules.IsRisingRule;

/**
 * Convergence-Divergence indicator.
 * Indicator-convergence-divergence.
 * * 指标-收敛-发散。  TODO ?? 是否为MACD??
 *
 * “Indicator convergence divergence”可能是指MACD（Moving Average Convergence Divergence）指标，这是一种广泛用于技术分析的指标。
 *
 * MACD指标由两条线组成：快速线（MACD线）和慢速线（信号线）。快速线是两个移动平均线之间的差异，通常是12天指数移动平均线减去26天指数移动平均线。慢速线是快速线的9天指数移动平均线。MACD的计算方法如下：
 *
 * 1. 计算12天指数移动平均线（EMA）。
 * 2. 计算26天指数移动平均线。
 * 3. 计算快速线（MACD线）：12天EMA减去26天EMA。
 * 4. 计算慢速线（信号线）：MACD线的9天EMA。
 *
 * MACD指标的主要应用包括：
 * - **趋势确认**：当MACD线交叉信号线向上时，可能表示买入信号；当MACD线交叉信号线向下时，可能表示卖出信号。
 * - **趋势强度**：MACD柱状图的高度可以反映价格趋势的强度。柱状图的增长表示价格加速上涨或下跌的动力增加，柱状图的缩小则表示价格动力减弱。
 * - **背离**：价格和MACD之间的背离可能暗示着价格趋势的转变。
 *
 * MACD指标在技术分析中是非常常用的工具之一，但在使用时，需要结合其他指标和分析方法，并且根据具体市场情况进行调整和确认。
 *
 */
public class ConvergenceDivergenceIndicator extends CachedIndicator<Boolean> {

    /**
     * Select the type of convergence or divergence.
     * * 选择收敛或发散的类型。
     */
    public enum ConvergenceDivergenceType {
        /**
         * Returns true for <b>"positiveConvergent"</b> when the values of the ref-{@link Indicator indicator} and the values of the other-{@link Indicator indicator}
           increase within the barCount. In short: "other" and "ref" makes   higher highs.
         当 ref-{@link Indicator indicator} 的值和 other-{@link Indicator indicator} 的值时，<b>"positiveConvergent"</b> 返回 true
         在 barCount 内增加。 简而言之：“其他”和“参考”创造了更高的高点。
         */
        positiveConvergent,

        /**
         * Returns true for <b>"negativeConvergent"</b> when the values of the
          ref-{@link Indicator indicator} and the values of the other-{@link Indicator
          indicator} decrease within the barCount. In short: "other" and "ref" makes
         lower lows.
         当 <b>"negativeConvergent"</b> 的值为
         ref-{@link Indicator indicator} 和 other-{@link Indicator} 的值
         指标} 在 barCount 内减少。 简而言之：“其他”和“参考”使
         较低的低点。
         */
        negativeConvergent,

        /**
         * Returns true for <b>"positiveDivergent"</b> when the values of the
          ref-{@link Indicator indicator} increase and the values of the
          other-{@link Indicator indicator} decrease within a barCount. In short:
          "other" makes lower lows while "ref" makes higher highs.
         当 <b>"positiveDivergent"</b> 的值为
         ref-{@link Indicator indicator} 增加并且值
         other-{@link Indicator indicator} 在 barCount 内减少。 简而言之：
         “其他”产生较低的低点，而“参考”产生较高的高点。
         */
        positiveDivergent,

        /**
         * Returns true for <b>"negativeDivergent"</b> when the values of the
          ref-{@link Indicator indicator} decrease and the values of the
          other-{@link Indicator indicator} increase within a barCount. In short:
          "other" makes higher highs while "ref" makes lower lows.
         当 <b>"negativeDivergent"</b> 的值为
         ref-{@link Indicator indicator} 减少并且
         other-{@link Indicator indicator} 在 barCount 内增加。 简而言之：
         “其他”创造更高的高点，而“参考”创造更低的低点。
         */
        negativeDivergent
    }

    /**
     * Select the type of strict convergence or divergence.
     * 选择严格收敛或发散的类型。
     */
    public enum ConvergenceDivergenceStrictType {

        /**
         * Returns true for <b>"positiveConvergentStrict"</b> when the values of the
          ref-{@link Indicator indicator} and the values of the other-{@link Indicator
          indicator} increase consecutively within a barCount. In short: "other" and
          "ref" makes strict higher highs.
         当 <b>"positiveConvergentStrict"</b> 的值为
         ref-{@link Indicator indicator} 和 other-{@link Indicator} 的值
         指标} 在 barCount 内连续增加。 简而言之：“其他”和
         "ref" 产生严格的更高的高点。
         */
        positiveConvergentStrict,

        /**
         * Returns true for <b>"negativeConvergentStrict"</b> when the values of the
          ref-{@link Indicator indicator} and the values of the other-{@link Indicator
          indicator} decrease consecutively within a barCount. In short: "other" and
          "ref" makes strict lower lows.
         当 <b>"negativeConvergentStrict"</b> 的值为
         ref-{@link Indicator indicator} 和 other-{@link Indicator} 的值
         指标} 在 barCount 内连续减少。 简而言之：“其他”和
         "ref" 形成严格的低点。
         */
        negativeConvergentStrict,

        /**
         * Returns true for <b>"positiveDivergentStrict"</b> when the values of the
          ref-{@link Indicator indicator} increase consecutively and the values of the
          other-{@link Indicator indicator} decrease consecutively within a barCount.
          In short: "other" makes strict higher highs and "ref" makes strict lower  lows.

         当 <b>"positiveDivergentStrict"</b> 的值为
         ref-{@link Indicator indicator} 连续增加，
         other-{@link Indicator indicator} 在 barCount 内连续减少。
         简而言之：“other”产生严格的高点，“ref”产生严格的低点。
         */
        positiveDivergentStrict,

        /**
         * Returns true for <b>"negativeDivergentStrict"</b> when the values of the
         ref-{@link Indicator indicator} decrease consecutively and the values of the
          other-{@link Indicator indicator} increase consecutively within a barCount.
         In short: "other" makes strict lower lows and "ref" makes strict higher  highs.

         当 <b>"negativeDivergentStrict"</b> 的值为
         ref-{@link Indicator indicator} 连续递减，
         other-{@link Indicator indicator} 在 barCount 内连续增加。
         简而言之：“other”是严格的低点，“ref”是严格的高点。
         */
        negativeDivergentStrict
    }

    /** The actual indicator.  实际(現存)指标。*/
    private final Indicator<Num> ref;

    /** The other indicator.  另一个指标。*/
    private final Indicator<Num> other;

    /** The barCount. 酒吧计数。 */
    private final int barCount;

    /** The type of the convergence or divergence
     * 收敛或发散的类型 **/
    private final ConvergenceDivergenceType type;

    /** The type of the strict convergence or strict divergence
     *  严格收敛或严格发散的类型 **/
    private final ConvergenceDivergenceStrictType strictType;

    /** The minimum strength for convergence or divergence. **/
    private final Num minStrength;

    /** The minimum slope for convergence or divergence.
     * 收敛或发散的最小斜率。 **/
    private final Num minSlope;

    /**
     * Constructor. <br/>
     * <br/>
     *
     * The <b>"minStrength"</b> is the minimum required strength for convergence or
     * divergence and must be a number between "0.1" and "1.0": <br/>
     * <br/>
     * 0.1: very weak <br/>
     * 0.8: strong (recommended) <br/>
     * 1.0: very strong <br/>
     *
     * <br/>
     *
     * The <b>"minSlope"</b> is the minimum required slope for convergence or
     * divergence and must be a number between "0.1" and "1.0": <br/>
     * <br/>
     * 0.1: very unstrict<br/>
     * 0.3: strict (recommended) <br/>
     * 1.0: very strict <br/>
     *
     * @param ref         the indicator
     *                    指标
     * @param other       the other indicator
     *                    另一个指标
     * @param barCount    the time frame
     *                    时间范围
     * @param type        of convergence or divergence
     *                    收敛或发散的
     * @param minStrength the minimum required strength for convergence or  divergence
     *                    收敛或发散所需的最小强度
     * @param minSlope    the minimum required slope for convergence or divergence
     *                    收敛或发散所需的最小斜率
     */
    public ConvergenceDivergenceIndicator(Indicator<Num> ref, Indicator<Num> other, int barCount,
            ConvergenceDivergenceType type, double minStrength, double minSlope) {
        super(ref);
        this.ref = ref;
        this.other = other;
        this.barCount = barCount;
        this.type = type;
        this.strictType = null;
        this.minStrength = numOf(Math.min(1, Math.abs(minStrength)));
        this.minSlope = numOf(minSlope);
    }

    /**
     * Constructor for strong convergence or divergence.
     * 强收敛或发散的构造函数。
     *
     * @param ref      the indicator
     *                 指标
     * @param other    the other indicator
     *                 另一个指标
     * @param barCount the time frame
     *                 时间范围
     * @param type     of convergence or divergence
     *                 收敛或发散的
     */
    public ConvergenceDivergenceIndicator(Indicator<Num> ref, Indicator<Num> other, int barCount,
            ConvergenceDivergenceType type) {
        super(ref);
        this.ref = ref;
        this.other = other;
        this.barCount = barCount;
        this.type = type;
        this.strictType = null;
        this.minStrength = numOf(0.8).abs();
        this.minSlope = numOf(0.3);
    }

    /**
     * Constructor for strict convergence or divergence.
     * 严格收敛或发散的构造函数。
     *
     * @param ref        the indicator
     *                   指标
     * @param other      the other indicator
     *                   另一个指标
     * @param barCount   the time frame
     *                   时间范围
     * @param strictType of strict convergence or divergence
     *                   严格收敛或发散的
     */
    public ConvergenceDivergenceIndicator(Indicator<Num> ref, Indicator<Num> other, int barCount,
            ConvergenceDivergenceStrictType strictType) {
        super(ref);
        this.ref = ref;
        this.other = other;
        this.barCount = barCount;
        this.type = null;
        this.strictType = strictType;
        this.minStrength = null;
        this.minSlope = null;
    }

    @Override
    protected Boolean calculate(int index) {

        if (minStrength != null && minStrength.isZero()) {
            return false;
        }

        if (type != null) {
            switch (type) {
            case positiveConvergent:
                return calculatePositiveConvergence(index);
            case negativeConvergent:
                return calculateNegativeConvergence(index);
            case positiveDivergent:
                return calculatePositiveDivergence(index);
            case negativeDivergent:
                return calculateNegativeDivergence(index);
            default:
                return false;
            }
        }

        else if (strictType != null) {
            switch (strictType) {
            case positiveConvergentStrict:
                return calculatePositiveConvergenceStrict(index);
            case negativeConvergentStrict:
                return calculateNegativeConvergenceStrict(index);
            case positiveDivergentStrict:
                return calculatePositiveDivergenceStrict(index);
            case negativeDivergentStrict:
                return calculateNegativeDivergenceStrict(index);
            default:
                return false;
            }
        }

        return false;
    }

    /** @return {@link #barCount} */
    @Override
    public int getUnstableBars() {
        return barCount;
    }

    /**
     * @param index the actual index
     *              实际指数
     * @return true, if strict positive convergent
     * 真，如果严格正收敛
     */
    private Boolean calculatePositiveConvergenceStrict(int index) {
        Rule refIsRising = new IsRisingRule(ref, barCount);
        Rule otherIsRising = new IsRisingRule(ref, barCount);

        return (refIsRising.and(otherIsRising)).isSatisfied(index);
    }

    /**
     * @param index the actual index
     *              实际指数
     * @return true, if strict negative convergent
     *          真，如果严格负收敛
     */
    private Boolean calculateNegativeConvergenceStrict(int index) {
        Rule refIsFalling = new IsFallingRule(ref, barCount);
        Rule otherIsFalling = new IsFallingRule(ref, barCount);

        return (refIsFalling.and(otherIsFalling)).isSatisfied(index);
    }

    /**
     * @param index the actual index
     *              实际指数
     * @return true, if positive divergent
     *          真，如果正发散
     */
    private Boolean calculatePositiveDivergenceStrict(int index) {
        Rule refIsRising = new IsRisingRule(ref, barCount);
        Rule otherIsFalling = new IsFallingRule(ref, barCount);

        return (refIsRising.and(otherIsFalling)).isSatisfied(index);
    }

    /**
     * @param index the actual index
     *              实际指数
     * @return true, if negative divergent
     * @return true，如果为负发散
     */
    private Boolean calculateNegativeDivergenceStrict(int index) {
        Rule refIsFalling = new IsFallingRule(ref, barCount);
        Rule otherIsRising = new IsRisingRule(ref, barCount);

        return (refIsFalling.and(otherIsRising)).isSatisfied(index);
    }

    /**
     * @param index the actual index
     *              实际指数
     * @return true, if positive convergent
     * 真，如果正收敛
     */
    private Boolean calculatePositiveConvergence(int index) {
        CorrelationCoefficientIndicator cc = new CorrelationCoefficientIndicator(ref, other, barCount);
        boolean isConvergent = cc.getValue(index).isGreaterThanOrEqual(minStrength);

        Num slope = calculateSlopeRel(index);
        boolean isPositive = slope.isGreaterThanOrEqual(minSlope.abs());

        return isConvergent && isPositive;
    }

    /**
     * @param index the actual index
     *              实际指数
     * @return true, if negative convergent
     * @return true，如果为负收敛
     */
    private Boolean calculateNegativeConvergence(int index) {
        CorrelationCoefficientIndicator cc = new CorrelationCoefficientIndicator(ref, other, barCount);
        boolean isConvergent = cc.getValue(index).isGreaterThanOrEqual(minStrength);

        Num slope = calculateSlopeRel(index);
        boolean isNegative = slope.isLessThanOrEqual(minSlope.abs().multipliedBy(numOf(-1)));

        return isConvergent && isNegative;
    }

    /**
     * @param index the actual index
     *              实际指数
     * @return true, if positive divergent
     * 真，如果正发散
     */
    private Boolean calculatePositiveDivergence(int index) {

        CorrelationCoefficientIndicator cc = new CorrelationCoefficientIndicator(ref, other, barCount);
        boolean isDivergent = cc.getValue(index).isLessThanOrEqual(minStrength.multipliedBy(numOf(-1)));

        if (isDivergent) {
            // If "isDivergent" and "ref" is positive, then "other" must be negative.
            // 如果 "isDivergent" 和 "ref" 是正数，那么 "other" 一定是负数。
            Num slope = calculateSlopeRel(index);
            return slope.isGreaterThanOrEqual(minSlope.abs());
        }

        return false;
    }

    /**
     * @param index the actual index
     *              实际指数
     * @return true, if negative divergent
     * * @return true，如果为负发散
     */
    private Boolean calculateNegativeDivergence(int index) {

        CorrelationCoefficientIndicator cc = new CorrelationCoefficientIndicator(ref, other, barCount);
        boolean isDivergent = cc.getValue(index).isLessThanOrEqual(minStrength.multipliedBy(numOf(-1)));

        if (isDivergent) {
            // If "isDivergent" and "ref" is positive, then "other" must be negative.
            // 如果 "isDivergent" 和 "ref" 是正数，那么 "other" 一定是负数。
            Num slope = calculateSlopeRel(index);
            return slope.isLessThanOrEqual(minSlope.abs().multipliedBy(numOf(-1)));
        }

        return false;
    }

    /**
     * @param index the actual index
     *              实际指数
     * @return the relative slope
     * 相对斜率
     */
    private Num calculateSlopeRel(int index) {
        SimpleLinearRegressionIndicator slrRef = new SimpleLinearRegressionIndicator(ref, barCount);
        int firstIndex = Math.max(0, index - barCount + 1);
        return (slrRef.getValue(index).minus(slrRef.getValue(firstIndex))).dividedBy(slrRef.getValue(index));
    }

}
