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

import java.io.Serializable;
import java.util.Objects;

import org.ta4j.core.analysis.cost.CostModel;
import org.ta4j.core.analysis.cost.ZeroCostModel;
import org.ta4j.core.num.Num;

/**
 * A {@code Trade} is defined by:
 *
 * <ul>
 * <li>the index (in the {@link BarSeries bar series}) on which the trade is
 * executed
 * <li>a {@link TradeType type} (BUY or SELL)
 * <li>a pricePerAsset (optional)
 * <li>a trade amount (optional)
 * </ul>
 *
 * A {@link Position position} is a pair of complementary trades.
 * * {@link Position position} 是一对互补交易。
 */
public class Trade implements Serializable {

    private static final long serialVersionUID = -905474949010114150L;

    /** The type of a {@link Trade trade}. */
    public enum TradeType {

        /** A BUY corresponds to a <i>BID</i> trade. */
        BUY {
            @Override
            public TradeType complementType() {
                return SELL;
            }
        },

        /** A SELL corresponds to an <i>ASK</i> trade. */
        SELL {
            @Override
            public TradeType complementType() {
                return BUY;
            }
        };

        /**
         * @return the complementary trade type
         * * @return 互补交易类型
         */
        public abstract TradeType complementType();
    }

    /** The type of the trade. */
    private final TradeType type;

    /** The index the trade was executed. */
    private final int index;

    /** The trade price per asset. */
    private Num pricePerAsset;

    /**
     * The net price per asset for the trade (i.e. {@link #pricePerAsset} with
     * {@link #cost}).
     */
    private Num netPrice;

    /** The trade amount. */
    private final Num amount;

    /** The cost for executing the trade. */
    private Num cost;

    /** The cost model for trade execution. */
    private transient CostModel costModel;

    /**
     * Constructor.
     *
     * @param index  the index the trade is executed
     *               交易执行的指数
     * @param series the bar series
     *               酒吧系列
     *
     * @param type   the trade type
     *               交易类型
     */
    protected Trade(int index, BarSeries series, TradeType type) {
        this(index, series, type, series.one());
    }

    /**
     * Constructor.
     *
     * @param index  the index the trade is executed
     *               交易执行的指数
     *
     * @param series the bar series
     *               酒吧系列
     *
     * @param type   the trade type
     *               交易类型
     *
     * @param amount the trade amount
     *               交易金额
     */
    protected Trade(int index, BarSeries series, TradeType type, Num amount) {
        this(index, series, type, amount, new ZeroCostModel());
    }

    /**
     * Constructor.
     *
     * @param index                the index the trade is executed
     *                             交易执行的指数
     *
     * @param series               the bar series
     *                             酒吧系列
     *
     * @param type                 the trade type
     *                             交易类型
     *
     * @param amount               the trade amount
     *                             交易金额
     *
     * @param transactionCostModel the cost model for trade execution cost
     *                             交易执行成本的成本模型
     */
    protected Trade(int index, BarSeries series, TradeType type, Num amount, CostModel transactionCostModel) {
        this.type = type;
        this.index = index;
        this.amount = amount;
        setPricesAndCost(series.getBar(index).getClosePrice(), amount, transactionCostModel);
    }

    /**
     * Constructor.
     *
     * @param index         the index the trade is executed
     *                      交易执行的指数
     *
     * @param type          the trade type
     *                      交易类型
     *
     * @param pricePerAsset the trade price per asset
     *                      每项资产的交易价格
     */
    protected Trade(int index, TradeType type, Num pricePerAsset) {
        this(index, type, pricePerAsset, pricePerAsset.one());
    }

    /**
     * Constructor.
     *
     * @param index         the index the trade is executed
     *                      交易执行的指数
     *
     * @param type          the trade type
     *                      交易类型
     *
     * @param pricePerAsset the trade price per asset
     *                      每项资产的交易价格
     *
     * @param amount        the trade amount
     *                      交易金额
     */
    protected Trade(int index, TradeType type, Num pricePerAsset, Num amount) {
        this(index, type, pricePerAsset, amount, new ZeroCostModel());
    }

    /**
     * Constructor.
     *
     * @param index                the index the trade is executed
     *                             交易执行的指数
     *
     * @param type                 the trade type
     *                             交易类型
     *
     * @param pricePerAsset        the trade price per asset
     *                             每项资产的交易价格
     *
     * @param amount               the trade amount
     *                             交易金额
     *
     * @param transactionCostModel the cost model for trade execution
     *                             交易执行的成本模型
     */
    protected Trade(int index, TradeType type, Num pricePerAsset, Num amount, CostModel transactionCostModel) {
        this.type = type;
        this.index = index;
        this.amount = amount;

        setPricesAndCost(pricePerAsset, amount, transactionCostModel);
    }

    /**
     * @return the trade type (BUY or SELL)
     * * @return 交易类型（买入或卖出）
     */
    public TradeType getType() {
        return type;
    }

    /**
     * @return the costs of the trade
     * * @return 交易成本
     */
    public Num getCost() {
        return cost;
    }

    /**
     * @return the index the trade is executed
     * * @return 交易执行的索引
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the trade price per asset
     * * @return 每个资产的交易价格
     */
    public Num getPricePerAsset() {
        return pricePerAsset;
    }

    /**
     * @return the trade price per asset, or, if {@code NaN}, the close price from
     *         the supplied {@link BarSeries}.
     */
    public Num getPricePerAsset(BarSeries barSeries) {
        if (pricePerAsset.isNaN()) {
            return barSeries.getBar(index).getClosePrice();
        }
        return pricePerAsset;
    }

    /**
     * @return the net price per asset for the trade (i.e. {@link #pricePerAsset}
     *         with {@link #cost})
     */
    public Num getNetPrice() {
        return netPrice;
    }

    /**
     * @return the trade amount
     * @return 交易金额
     */
    public Num getAmount() {
        return amount;
    }

    /**
     * @return the cost model for trade execution
     * * @return 交易执行的成本模型
     */
    public CostModel getCostModel() {
        return costModel;
    }

    /**
     * Sets the raw and net prices of the trade.
     *
     * @param pricePerAsset        the raw price of the asset
     *                             资产的原始价格
     *
     * @param amount               the amount of assets ordered
     *                             订购的资产数量
     *
     * @param transactionCostModel the cost model for trade execution
     *                             交易执行的成本模型
     */
    private void setPricesAndCost(Num pricePerAsset, Num amount, CostModel transactionCostModel) {
        this.costModel = transactionCostModel;
        this.pricePerAsset = pricePerAsset;
        this.cost = transactionCostModel.calculate(this.pricePerAsset, amount);

        Num costPerAsset = cost.dividedBy(amount);
        // add transaction costs to the pricePerAsset at the trade
        // 将交易成本添加到交易的 pricePerAsset
        if (type.equals(TradeType.BUY)) {
            this.netPrice = this.pricePerAsset.plus(costPerAsset);
        } else {
            this.netPrice = this.pricePerAsset.minus(costPerAsset);
        }
    }

    /**
     * @return true if this is a BUY trade, false otherwise
     * * @return 如果这是买入交易，则返回 true，否则返回 false
     */
    public boolean isBuy() {
        return type == TradeType.BUY;
    }

    /**
     * @return true if this is a SELL trade, false otherwise
     * * @return 如果这是卖出交易，则返回 true，否则返回 false
     */
    public boolean isSell() {
        return type == TradeType.SELL;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, index, pricePerAsset, amount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final Trade other = (Trade) obj;
        return Objects.equals(type, other.type) && Objects.equals(index, other.index)
                && Objects.equals(pricePerAsset, other.pricePerAsset) && Objects.equals(amount, other.amount);
    }

    @Override
    public String toString() {
        return "Trade{" + "type=" + type + ", index=" + index + ", price=" + pricePerAsset + ", amount=" + amount + '}';
    }

    /**
     * @param index  the index the trade is executed
     *               交易执行的指数
     *
     * @param series the bar series
     *               酒吧系列
     *
     * @return a BUY trade
     *          买入交易
     */
    public static Trade buyAt(int index, BarSeries series) {
        return new Trade(index, series, TradeType.BUY);
    }

    /**
     * @param index                the index the trade is executed
     * @param price                the trade price per asset
     * @param amount               the trade amount
     *                             交易金额
     *
     * @param transactionCostModel the cost model for trade execution
     *                             交易执行的成本模型
     * @return a BUY trade
     *                       @return 买入交易
     */
    public static Trade buyAt(int index, Num price, Num amount, CostModel transactionCostModel) {
        return new Trade(index, TradeType.BUY, price, amount, transactionCostModel);
    }

    /**
     * @param index  the index the trade is executed
     * @param price  the trade price per asset
     * @param amount the trade amount
     *               交易金额
     *
     * @return a BUY trade
     *  * @return 买入交易
     */
    public static Trade buyAt(int index, Num price, Num amount) {
        return new Trade(index, TradeType.BUY, price, amount);
    }

    /**
     * @param index  the index the trade is executed
     *               交易执行的指数
     *
     * @param series the bar series
     *               酒吧系列
     *
     * @param amount the trade amount
     *               交易金额
     *
     * @return a BUY trade
     *          * @return 买入交易
     */
    public static Trade buyAt(int index, BarSeries series, Num amount) {
        return new Trade(index, series, TradeType.BUY, amount);
    }

    /**
     * @param index                the index the trade is executed
     *                             交易执行的指数
     *
     * @param series               the bar series
     *                             酒吧系列
     *
     * @param amount               the trade amount
     *                             交易金额
     *
     * @param transactionCostModel the cost model for trade execution
     *                             交易执行的成本模型
     * @return a BUY trade
     *          买入交易
     */
    public static Trade buyAt(int index, BarSeries series, Num amount, CostModel transactionCostModel) {
        return new Trade(index, series, TradeType.BUY, amount, transactionCostModel);
    }

    /**
     * @param index  the index the trade is executed
     *               交易执行的指数
     *
     * @param series the bar series
     *               酒吧系列
     *
     * @return a SELL trade
     *          卖出交易
     */
    public static Trade sellAt(int index, BarSeries series) {
        return new Trade(index, series, TradeType.SELL);
    }

    /**
     * @param index  the index the trade is executed
     * @param price  the trade price per asset
     * @param amount the trade amount
     *               交易金额
     *
     * @return a SELL trade
     *          卖出交易
     */
    public static Trade sellAt(int index, Num price, Num amount) {
        return new Trade(index, TradeType.SELL, price, amount);
    }

    /**
     * @param index                the index the trade is executed
     * @param price                the trade price per asset
     * @param amount               the trade amount
     *                             交易金额
     *
     * @param transactionCostModel the cost model for trade execution
     *                             交易执行的成本模型
     * @return a SELL trade
     *      * @return 卖出交易
     */
    public static Trade sellAt(int index, Num price, Num amount, CostModel transactionCostModel) {
        return new Trade(index, TradeType.SELL, price, amount, transactionCostModel);
    }

    /**
     * @param index  the index the trade is executed
     *               交易执行的指数
     *
     * @param series the bar series
     *               酒吧系列
     *
     * @param amount the trade amount
     *               交易金额
     *
     * @return a SELL trade
     * @return 卖出交易
     */
    public static Trade sellAt(int index, BarSeries series, Num amount) {
        return new Trade(index, series, TradeType.SELL, amount);
    }

    /**
     * @param index                the index the trade is executed
     *                             交易执行的指数
     *
     * @param series               the bar series
     *                             酒吧系列
     *
     * @param amount               the trade amount
     *                             * @param amount 交易金额
     * @param transactionCostModel the cost model for trade execution
     *                             交易执行的成本模型
     *
     * @return a SELL trade
     * @return 卖出交易
     */
    public static Trade sellAt(int index, BarSeries series, Num amount, CostModel transactionCostModel) {
        return new Trade(index, series, TradeType.SELL, amount, transactionCostModel);
    }

    /**
     * @return the value of a trade (without transaction cost)
     * * @return 交易的价值（不含交易成本）
     */
    public Num getValue() {
        return pricePerAsset.multipliedBy(amount);
    }
}
