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
package org.ta4j.core.criteria;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.pnl.ReturnCriterion;
import org.ta4j.core.num.Num;

/**
 * A linear transaction cost criterion.
 * 线性交易成本标准。
 *
 * <p>
 * Calculates the transaction cost according to an initial traded amount and a
 * linear function defined by a and b (a * x + b).
 */
public class LinearTransactionCostCriterion extends AbstractAnalysisCriterion {

    private final double initialAmount;

    private final double a;
    private final double b;

    private final ReturnCriterion grossReturn;

    /**
     * Constructor. (a * x)
     * * 构造函数。 (a * x)
     *
     * @param initialAmount the initially traded amount
     *                      * @param initialAmount 初始交易金额
     * @param a             the a coefficient (e.g. 0.005 for 0.5% per {@link Trade  trade})
     *                      * @param a a 系数（例如，每 {@link Trade trade} 0.5% 为 0.005）
     */
    public LinearTransactionCostCriterion(double initialAmount, double a) {
        this(initialAmount, a, 0);
    }

    /**
     * Constructor. (a * x + b)
     * * 构造函数。 (a * x + b)
     *
     * @param initialAmount the initially traded amount
     *                      * @param initialAmount 初始交易金额
     * @param a             the a coefficient (e.g. 0.005 for 0.5% per {@link Trade    trade})
     *                      * @param a a 系数（例如，每 {@link Trade trade} 0.5% 为 0.005）
     * @param b             the b constant (e.g. 0.2 for $0.2 per {@link Trade   trade})
     *                      @param b b 常数（例如，每 {@link Trade trade} 0.2 美元为 0.2）
     */
    public LinearTransactionCostCriterion(double initialAmount, double a, double b) {
        this.initialAmount = initialAmount;
        this.a = a;
        this.b = b;
        grossReturn = new ReturnCriterion();
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        return getTradeCost(series, position, series.numOf(initialAmount));
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        Num totalCosts = series.zero();
        Num tradedAmount = series.numOf(initialAmount);

        for (Position position : tradingRecord.getPositions()) {
            Num tradeCost = getTradeCost(series, position, tradedAmount);
            totalCosts = totalCosts.plus(tradeCost);
            // To calculate the new traded amount:
            // 计算新的交易量：
            // - Remove the cost of the *first* trade
            // - 移除 *first* 交易的成本
            // - Multiply by the profit ratio
            // - 乘以利润率
            // - Remove the cost of the *second* trade
            // - 移除*第二*交易的成本
            tradedAmount = tradedAmount.minus(getTradeCost(position.getEntry(), tradedAmount));
            tradedAmount = tradedAmount.multipliedBy(grossReturn.calculate(series, position));
            tradedAmount = tradedAmount.minus(getTradeCost(position.getExit(), tradedAmount));
        }

        // Special case: if the current position is open
        // 特殊情况：如果当前持仓未平仓
        Position currentPosition = tradingRecord.getCurrentPosition();
        if (currentPosition.isOpened()) {
            totalCosts = totalCosts.plus(getTradeCost(currentPosition.getEntry(), tradedAmount));
        }

        return totalCosts;
    }

    /** The lower the criterion value, the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isLessThan(criterionValue2);
    }

    /**
     * @param trade        the trade
     *                     交易
     * @param tradedAmount the amount of the trade
     *                     交易金额
     * @return the absolute trade cost
     * @return 绝对交易成本
     */
    private Num getTradeCost(Trade trade, Num tradedAmount) {
        Num tradeCost = tradedAmount.zero();
        if (trade != null) {
            return tradedAmount.numOf(a).multipliedBy(tradedAmount).plus(tradedAmount.numOf(b));
        }
        return tradeCost;
    }

    /**
     * @param series        the bar series
     *                      bar系列
     * @param position      the position
     *                      位置
     * @param initialAmount the initially traded amount for the position
     *                      头寸的初始交易金额
     * @return the absolute total cost of all trades in the position
     * * @return 持仓所有交易的绝对总成本
     */
    private Num getTradeCost(BarSeries series, Position position, Num initialAmount) {
        Num totalTradeCost = series.zero();
        if (position != null && position.getEntry() != null) {
            totalTradeCost = getTradeCost(position.getEntry(), initialAmount);
            if (position.getExit() != null) {
                // To calculate the new traded amount:
                // - Remove the cost of the first trade
                // - Multiply by the profit ratio
                Num newTradedAmount = initialAmount.minus(totalTradeCost)
                        .multipliedBy(grossReturn.calculate(series, position));
                totalTradeCost = totalTradeCost.plus(getTradeCost(position.getExit(), newTradedAmount));
            }
        }
        return totalTradeCost;
    }
}
