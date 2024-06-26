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
package org.ta4j.core.indicators.pivotpoints;

import static org.ta4j.core.num.NaN.NaN;

import java.util.List;

import org.ta4j.core.Bar;
import org.ta4j.core.indicators.RecursiveCachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Pivot Reversal Indicator.
 * *枢轴反转指标。
 *
 * Pivot Reversal Indicator是一种技术分析工具，用于识别资产价格可能发生趋势反转的信号。它结合了Pivot Point指标和其他技术指标，帮助交易者确定市场的转折点。
 *
 * Pivot Reversal Indicator的工作原理基于先前周期的价格行为和可能的支撑/阻力水平。它通常会识别出潜在的价格反转点，以及价格趋势可能变化的迹象。
 *
 * 具体的计算方法和特征取决于所选用的Pivot Reversal Indicator版本和参数设置。一般来说，它可能会考虑到以下因素：
 *
 * 1. Pivot Point：计算出来的主要Pivot Point水平，作为价格的中心点。
 * 2. 支撑和阻力水平：根据主要Pivot Point水平和先前周期的价格高低点计算出来的可能的支撑和阻力水平。
 * 3. 其他技术指标：可能会结合其他技术指标，如移动平均线、相对强弱指标等，来进一步确认价格反转的信号。
 *
 * Pivot Reversal Indicator的信号可以用于确定买入或卖出的时机。例如，当价格触及支撑水平并且出现了其他确认信号时，可能会出现买入信号；反之，当价格触及阻力水平并且出现了其他确认信号时，可能会出现卖出信号。
 *
 * 总的来说，Pivot Reversal Indicator是一种有用的技术分析工具，可以帮助交易者识别价格可能发生趋势反转的信号，有助于制定更有效的交易策略。
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:pivot_points">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:pivot_points</a>
 */
public class StandardReversalIndicator extends RecursiveCachedIndicator<Num> {

    private final PivotPointIndicator pivotPointIndicator;
    private final PivotLevel level;
    private final Num two;

    /**
     * Constructor.
     *
     * Calculates the (standard) reversal for the corresponding pivot level
     * * 计算相应枢轴水平的（标准）反转
     *
     * @param pivotPointIndicator the {@link PivotPointIndicator} for this reversal
     *                           此反转的 {@link PivotPointIndicator}
     * @param level               the {@link PivotLevel} for this reversal
     *                            此反转的 {@link PivotLevel}
     */
    public StandardReversalIndicator(PivotPointIndicator pivotPointIndicator, PivotLevel level) {
        super(pivotPointIndicator);
        this.pivotPointIndicator = pivotPointIndicator;
        this.level = level;
        this.two = pivotPointIndicator.numOf(2);
    }

    @Override
    protected Num calculate(int index) {
        List<Integer> barsOfPreviousPeriod = pivotPointIndicator.getBarsOfPreviousPeriod(index);
        if (barsOfPreviousPeriod.isEmpty()) {
            return NaN;
        }
        switch (level) {
        case RESISTANCE_3:
            return calculateR3(barsOfPreviousPeriod, index);
        case RESISTANCE_2:
            return calculateR2(barsOfPreviousPeriod, index);
        case RESISTANCE_1:
            return calculateR1(barsOfPreviousPeriod, index);
        case SUPPORT_1:
            return calculateS1(barsOfPreviousPeriod, index);
        case SUPPORT_2:
            return calculateS2(barsOfPreviousPeriod, index);
        case SUPPORT_3:
            return calculateS3(barsOfPreviousPeriod, index);
        default:
            return NaN;
        }
    }

    @Override
    public int getUnstableBars() {
        return 0;
    }

    private Num calculateR3(List<Integer> barsOfPreviousPeriod, int index) {
        Bar bar = getBarSeries().getBar(barsOfPreviousPeriod.get(0));
        Num low = bar.getLowPrice();
        Num high = bar.getHighPrice();
        for (int i : barsOfPreviousPeriod) {
            Bar iBar = getBarSeries().getBar(i);
            low = iBar.getLowPrice().min(low);
            high = iBar.getHighPrice().max(high);
        }
        return high.plus(two.multipliedBy((pivotPointIndicator.getValue(index).minus(low))));
    }

    private Num calculateR2(List<Integer> barsOfPreviousPeriod, int index) {
        Bar bar = getBarSeries().getBar(barsOfPreviousPeriod.get(0));
        Num low = bar.getLowPrice();
        Num high = bar.getHighPrice();
        for (int i : barsOfPreviousPeriod) {
            Bar iBar = getBarSeries().getBar(i);
            low = iBar.getLowPrice().min(low);
            high = iBar.getHighPrice().max(high);
        }
        return pivotPointIndicator.getValue(index).plus((high.minus(low)));
    }

    private Num calculateR1(List<Integer> barsOfPreviousPeriod, int index) {
        Num low = getBarSeries().getBar(barsOfPreviousPeriod.get(0)).getLowPrice();
        for (int i : barsOfPreviousPeriod) {
            low = (getBarSeries().getBar(i).getLowPrice()).min(low);
        }
        return two.multipliedBy(pivotPointIndicator.getValue(index)).minus(low);
    }

    private Num calculateS1(List<Integer> barsOfPreviousPeriod, int index) {
        Num high = getBarSeries().getBar(barsOfPreviousPeriod.get(0)).getHighPrice();
        for (int i : barsOfPreviousPeriod) {
            high = (getBarSeries().getBar(i).getHighPrice()).max(high);
        }
        return two.multipliedBy(pivotPointIndicator.getValue(index)).minus(high);
    }

    private Num calculateS2(List<Integer> barsOfPreviousPeriod, int index) {
        Bar bar = getBarSeries().getBar(barsOfPreviousPeriod.get(0));
        Num high = bar.getHighPrice();
        Num low = bar.getLowPrice();
        for (int i : barsOfPreviousPeriod) {
            Bar iBar = getBarSeries().getBar(i);
            high = iBar.getHighPrice().max(high);
            low = iBar.getLowPrice().min(low);
        }
        return pivotPointIndicator.getValue(index).minus((high.minus(low)));
    }

    private Num calculateS3(List<Integer> barsOfPreviousPeriod, int index) {
        Bar bar = getBarSeries().getBar(barsOfPreviousPeriod.get(0));
        Num high = bar.getHighPrice();
        Num low = bar.getLowPrice();
        for (int i : barsOfPreviousPeriod) {
            Bar iBar = getBarSeries().getBar(i);
            high = iBar.getHighPrice().max(high);
            low = iBar.getLowPrice().min(low);
        }
        return low.minus(two.multipliedBy((high.minus(pivotPointIndicator.getValue(index)))));
    }
}
