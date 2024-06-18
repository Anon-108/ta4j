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
package org.ta4j.core.indicators.pivotpoints;

import static org.ta4j.core.num.NaN.NaN;

import java.util.List;

import org.ta4j.core.Bar;
import org.ta4j.core.indicators.RecursiveCachedIndicator;
import org.ta4j.core.num.Num;

/**
 * DeMark Reversal Indicator.
 * DeMark 反转指标。
 *
 * DeMark Reversal Indicator是一种用于技术分析的指标，它用于识别资产价格可能发生趋势反转的信号。这个指标是由技术分析家汤姆·德马克（Tom DeMark）开发的。
 *
 * DeMark Reversal Indicator的工作原理基于价格序列中的特定形态和序列的出现。它通常会标识出潜在的价格反转点，帮助交易者预测市场趋势的转变。
 *
 * 该指标的计算方法和特定形态的识别依赖于复杂的算法，其中包含了一系列的价格动态、波峰和波谷，以及对价格序列的一些限定条件。
 *  德马克使用了自己的一套独特的数学模型来确定这些特定的价格模式和信号。
 *
 * 尽管DeMark Reversal Indicator的具体细节和计算方法相对复杂，但它通常会生成几种不同类型的信号，例如“顺序13”和“顺序9”。
 *  这些信号可以提供有关市场可能反转的重要信息，有助于交易者做出更明智的交易决策。
 *
 * 总的来说，DeMark Reversal Indicator是一种强大的技术分析工具，可以帮助交易者识别价格趋势反转的信号，有助于制定更有效的交易策略。
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:pivot_points">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:pivot_points</a>
 */
public class DeMarkReversalIndicator extends RecursiveCachedIndicator<Num> {

    private final DeMarkPivotPointIndicator pivotPointIndicator;
    private final DeMarkPivotLevel level;
    private final Num two;

    public enum DeMarkPivotLevel {
        RESISTANCE, SUPPORT,
    }

    /**
     * Constructor.
     *
     * Calculates the DeMark reversal for the corresponding pivot level
     * * 计算相应枢轴水平的 DeMark 反转
     * 
     * @param pivotPointIndicator the {@link DeMarkPivotPointIndicator} for this    reversal  此反转的 {@link DeMarkPivotPointIndicator}
     * @param level               the {@link DeMarkPivotLevel} for this reversal   (RESISTANT, SUPPORT)  此逆转的 {@link DeMarkPivotLevel} (RESISTANT, SUPPORT)
     */
    public DeMarkReversalIndicator(DeMarkPivotPointIndicator pivotPointIndicator, DeMarkPivotLevel level) {
        super(pivotPointIndicator);
        this.pivotPointIndicator = pivotPointIndicator;
        this.level = level;
        this.two = numOf(2);
    }

    @Override
    protected Num calculate(int index) {
        Num x = pivotPointIndicator.getValue(index).multipliedBy(numOf(4));
        Num result;

        if (level == DeMarkPivotLevel.SUPPORT) {
            result = calculateSupport(x, index);
        } else {
            result = calculateResistance(x, index);
        }

        return result;

    }

    private Num calculateResistance(Num x, int index) {
        List<Integer> barsOfPreviousPeriod = pivotPointIndicator.getBarsOfPreviousPeriod(index);
        if (barsOfPreviousPeriod.isEmpty()) {
            return NaN;
        }
        Bar bar = getBarSeries().getBar(barsOfPreviousPeriod.get(0));
        Num low = bar.getLowPrice();
        for (int i : barsOfPreviousPeriod) {
            low = getBarSeries().getBar(i).getLowPrice().min(low);
        }

        return x.dividedBy(two).minus(low);
    }

    private Num calculateSupport(Num x, int index) {
        List<Integer> barsOfPreviousPeriod = pivotPointIndicator.getBarsOfPreviousPeriod(index);
        if (barsOfPreviousPeriod.isEmpty()) {
            return NaN;
        }
        Bar bar = getBarSeries().getBar(barsOfPreviousPeriod.get(0));
        Num high = bar.getHighPrice();
        for (int i : barsOfPreviousPeriod) {
            high = getBarSeries().getBar(i).getHighPrice().max(high);
        }

        return x.dividedBy(two).minus(high);
    }
}
