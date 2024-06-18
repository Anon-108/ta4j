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
import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import org.ta4j.core.num.Num;

/**
 * A {@code BarSeries} is a sequence of {@link Bar bars} separated by a
 * predefined period (e.g. 15 minutes, 1 day, etc.).
 * 
 * Notably, it can be:
 * 
 * <ul>
 * <li>the base of {@link Indicator indicator} calculations
 * <li>constrained between beginning and ending indices (e.g. for some
 * backtesting cases)
 * <li>limited to a fixed number of bars (e.g. for actual trading)
 * * <li>{@link Indicator indicator} 计算的基础
 *   * <li>限制在开始和结束索引之间（例如，对于一些回测案例）
 *   * <li>仅限于固定数量的柱（例如，用于实际交易）
 * </ul>
 */
public interface BarSeries extends Serializable {

    /**
     * @return the name of the series
     * * @return 系列名称
     */
    String getName();

    /**
     * @return any instance of Num to determine its Num type and function.
     */
    Num num();

    /**
     * Returns the underlying function to transform a Number into the Num
     * implementation used by this bar series
     *
     * @return a function Number -> Num
     */
    default Function<Number, Num> function() {
        return num().function();
    }

    /**
     * @return the Num of 0
     */
    default Num zero() {
        return num().zero();
    }

    /**
     * @return the Num of 1
     */
    default Num one() {
        return num().one();
    }

    /**
     * @return the Num of 100
     */
    default Num hundred() {
        return num().hundred();
    }

    /**
     * Transforms a {@link Number} into the {@link Num implementation} used by this
     * bar series
     *
     * @param number a {@link Number} implementing object.
     * @return the corresponding value as a Num implementing object
     */
    default Num numOf(Number number) {
        return num().function().apply(number);
    }

    /**
     * Gets the bar from {@link #getBarData()} with index {@code i}.
     * 
     * <p>
     * The given {@code i} can return the same bar within the first range of indices
     * due to {@link #setMaximumBarCount(int)}, for example: If you fill a BarSeries
     * with 30 bars and then apply a {@code maximumBarCount} of 10 bars, the first
     * 20 bars will be removed from the BarSeries. The indices going further from 0
     * to 29 remain but return the same bar from 0 to 20. The remaining 9 bars are
     * returned from index 21.
     * 
     * @param i the index
     * @return the bar at the i-th position
     * * @return 第 i 个位置的条形图
     */
    Bar getBar(int i);

    /**
     * @return the first bar of the series
     * * @return 系列的第一个柱
     */
    default Bar getFirstBar() {
        return getBar(getBeginIndex());
    }

    /**
     * @return the last bar of the series
     * * @return 该系列的最后一个小节
     */
    default Bar getLastBar() {
        return getBar(getEndIndex());
    }

    /**
     * @return the number of bars in the series
     * * @return 序列中的柱数
     */
    int getBarCount();

    /**
     * @return true if the series is empty, false otherwise
     * * @return 如果序列为空，则返回 true，否则返回 false
     */
    default boolean isEmpty() {
        return getBarCount() == 0;
    }

    /**
     * Returns the raw bar data, i.e. it returns the current list object, which is
     * used internally to store the {@link Bar bars}. It may be:
     *
     * <ul>
     * <li>a shortened bar list if a {@code maximumBarCount} has been set.
     * <li>an extended bar list if it is a constrained bar series.
     * </ul>
     * 
     * <p>
     * <b>Warning:</b> This method should be used carefully!
     *
     * @return the raw bar data
     * * @return 原始柱数据
     */
    List<Bar> getBarData();

    /**
     * @return the begin index of the series
     * * @return 系列的开始索引
     */
    int getBeginIndex();

    /**
     * @return the end index of the series
     * * @return 系列的结束索引
     */
    int getEndIndex();

    /**
     * @return the description of the series period (e.g. "from 12:00 21/01/2014 to 12:15 21/01/2014")
     * * @return 系列周期的描述（例如“从 12:00 21/01/2014 到 12:15 21/01/2014”）
     */
    default String getSeriesPeriodDescription() {
        StringBuilder sb = new StringBuilder();
        if (!getBarData().isEmpty()) {
            Bar firstBar = getFirstBar();
            Bar lastBar = getLastBar();
            sb.append(firstBar.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME))
                    .append(" - ")
                    .append(lastBar.getEndTime().format(DateTimeFormatter.ISO_DATE_TIME));
        }
        return sb.toString();
    }

    /**
     * @return the maximum number of bars
     * * @return 最大柱数
     */
    int getMaximumBarCount();

    /**
     * Sets the maximum number of bars that will be retained in the series.
     * * 设置将在系列中保留的最大柱数。
     *
     * If a new bar is added to the series such that the number of bars will exceed
     * the maximum bar count, then the FIRST bar in the series is automatically
     * removed, ensuring that the maximum bar count is not exceeded. The indices of
     * the bar series do not change.
     * 
     * @param maximumBarCount the maximum bar count
     *                        最大条数
     */
    void setMaximumBarCount(int maximumBarCount);

    /**
     * @return the number of removed bars
     *      * @return the number of removed bars
     */
    int getRemovedBarsCount();

    /**
     * Adds the {@code bar} at the end of the series.
     *
     * <p>
     * The {@code beginIndex} is set to {@code 0} if not already initialized.<br>
     * The {@code endIndex} is set to {@code 0} if not already initialized, or
     * incremented if it matches the end of the series.<br>
     * Exceeding bars are removed.
     *
     * @param bar the bar to be added
     * @apiNote to add bar data directly you can use
     *          {@link #addBar(Duration, ZonedDateTime, Num, Num, Num, Num, Num)}
     * @see BarSeries#setMaximumBarCount(int)
     */
    default void addBar(Bar bar) {
        addBar(bar, false);
    }

    /**
     * Adds the {@code bar} at the end of the series.
     *
     * <p>
     * The {@code beginIndex} is set to {@code 0} if not already initialized.<br>
     * The {@code endIndex} is set to {@code 0} if not already initialized, or
     * incremented if it matches the end of the series.<br>
     * Exceeding bars are removed.
     *
     * @param bar     the bar to be added
     * @param replace true to replace the latest bar. Some exchanges continuously
     *                provide new bar data in the respective period, e.g. 1 second
     *                in 1 minute duration.
     * @apiNote to add bar data directly you can use
     *          {@link #addBar(Duration, ZonedDateTime, Num, Num, Num, Num, Num)}
     * @see BarSeries#setMaximumBarCount(int)
     */
    void addBar(Bar bar, boolean replace);

    /**
     * Adds the {@code bar} at the end of the series.
     *
     * <p>
     * The {@code beginIndex} is set to {@code 0} if not already initialized.<br>
     * The {@code endIndex} is set to {@code 0} if not already initialized, or
     * incremented if it matches the end of the series.<br>
     * Exceeding bars are removed.
     *
     * @param timePeriod the {@link Duration} of this bar
     *                   此栏的 {@link Duration}
     * @param endTime    the {@link ZonedDateTime end time} of this bar
     * @apiNote to add bar data directly you can use
     *          {@link #addBar(Duration, ZonedDateTime, Num, Num, Num, Num, Num)}
     * @see BarSeries#setMaximumBarCount(int)
     */
    void addBar(Duration timePeriod, ZonedDateTime endTime);

    default void addBar(ZonedDateTime endTime, Number openPrice, Number highPrice, Number lowPrice, Number closePrice) {
        this.addBar(endTime, numOf(openPrice), numOf(highPrice), numOf(lowPrice), numOf(closePrice), zero(), zero());
    }

    default void addBar(ZonedDateTime endTime, Number openPrice, Number highPrice, Number lowPrice, Number closePrice,
            Number volume) {
        this.addBar(endTime, numOf(openPrice), numOf(highPrice), numOf(lowPrice), numOf(closePrice), numOf(volume));
    }

    default void addBar(ZonedDateTime endTime, Number openPrice, Number highPrice, Number lowPrice, Number closePrice,
            Number volume, Number amount) {
        this.addBar(endTime, numOf(openPrice), numOf(highPrice), numOf(lowPrice), numOf(closePrice), numOf(volume),
                numOf(amount));
    }

    default void addBar(Duration timePeriod, ZonedDateTime endTime, Number openPrice, Number highPrice, Number lowPrice,
            Number closePrice, Number volume) {
        this.addBar(timePeriod, endTime, numOf(openPrice), numOf(highPrice), numOf(lowPrice), numOf(closePrice),
                numOf(volume), zero());
    }

    default void addBar(Duration timePeriod, ZonedDateTime endTime, Number openPrice, Number highPrice, Number lowPrice,
            Number closePrice, Number volume, Number amount) {
        this.addBar(timePeriod, endTime, numOf(openPrice), numOf(highPrice), numOf(lowPrice), numOf(closePrice),
                numOf(volume), numOf(amount));
    }

    default void addBar(ZonedDateTime endTime, String openPrice, String highPrice, String lowPrice, String closePrice) {
        this.addBar(endTime, numOf(new BigDecimal(openPrice)), numOf(new BigDecimal(highPrice)),
                numOf(new BigDecimal(lowPrice)), numOf(new BigDecimal(closePrice)), zero(), zero());
    }

    default void addBar(ZonedDateTime endTime, String openPrice, String highPrice, String lowPrice, String closePrice,
            String volume) {
        this.addBar(endTime, numOf(new BigDecimal(openPrice)), numOf(new BigDecimal(highPrice)),
                numOf(new BigDecimal(lowPrice)), numOf(new BigDecimal(closePrice)), numOf(new BigDecimal(volume)),
                zero());
    }

    default void addBar(ZonedDateTime endTime, String openPrice, String highPrice, String lowPrice, String closePrice,
            String volume, String amount) {
        this.addBar(endTime, numOf(new BigDecimal(openPrice)), numOf(new BigDecimal(highPrice)),
                numOf(new BigDecimal(lowPrice)), numOf(new BigDecimal(closePrice)), numOf(new BigDecimal(volume)),
                numOf(new BigDecimal(amount)));
    }

    default void addBar(ZonedDateTime endTime, Num openPrice, Num highPrice, Num lowPrice, Num closePrice, Num volume) {
        this.addBar(endTime, openPrice, highPrice, lowPrice, closePrice, volume, zero());
    }

    /**
     * Adds a new {@code Bar} to the bar series.
     *
     * @param endTime    end time of the bar
     *                   酒吧的结束时间
     *
     * @param openPrice  the open price
     *                   开盘价
     *
     * @param highPrice  the high/max price
     *                   最高/最高价格
     *
     * @param lowPrice   the low/min price
     *                   最低/最低价格
     *
     * @param closePrice the last/close price
     *                   最后/收盘价
     *
     * @param volume     the volume (default zero)
     *                   音量（默认为零）
     *
     * @param amount     the amount (default zero)
     *                   金额（默认为零）
     */
    void addBar(ZonedDateTime endTime, Num openPrice, Num highPrice, Num lowPrice, Num closePrice, Num volume,
            Num amount);

    /**
     * Adds a new {@code Bar} to the bar series.
     *
     * @param endTime    end time of the bar
     *                   酒吧的结束时间
     *
     * @param openPrice  the open price
     *                   开盘价
     *
     * @param highPrice  the high/max price
     *                   最高/最高价格
     *
     * @param lowPrice   the low/min price
     *                   最低/最低价格
     *
     * @param closePrice the last/close price
     *                   最后/收盘价
     *
     * @param volume     the volume (default zero)
     *                   音量（默认为零）
     */
    void addBar(Duration timePeriod, ZonedDateTime endTime, Num openPrice, Num highPrice, Num lowPrice, Num closePrice,
            Num volume);

    /**
     * Adds a new {@code Bar} to the bar series.
     *
     * @param timePeriod the time period of the bar
     *                   酒吧的时间段
     *
     * @param endTime    end time of the bar
     *                   酒吧的结束时间
     *
     * @param openPrice  the open price
     *                   开盘价
     *
     * @param highPrice  the high/max price
     *                   最高/最高价格
     *
     * @param lowPrice   the low/min price
     *                   最低/最低价格
     *
     * @param closePrice the last/close price
     *                   最后/收盘价
     *
     * @param volume     the volume (default zero)
     *                   音量（默认为零）
     *
     * @param amount     the amount (default zero)
     *                   金额（默认为零）
     */
    void addBar(Duration timePeriod, ZonedDateTime endTime, Num openPrice, Num highPrice, Num lowPrice, Num closePrice,
            Num volume, Num amount);

    /**
     * Adds a trade and updates the close price of the last bar.
     *
     * @param tradeVolume the traded volume
     *                    成交量
     *
     * @param tradePrice  the price
     * @see Bar#addTrade(Num, Num)
     */
    default void addTrade(Number tradeVolume, Number tradePrice) {
        addTrade(numOf(tradeVolume), numOf(tradePrice));
    }

    /**
     * Adds a trade and updates the close price of the last bar.
     *
     * @param tradeVolume the traded volume
     *                    成交量
     *
     * @param tradePrice  the price
     * @see Bar#addTrade(Num, Num)
     */
    default void addTrade(String tradeVolume, String tradePrice) {
        addTrade(numOf(new BigDecimal(tradeVolume)), numOf(new BigDecimal(tradePrice)));
    }

    /**
     * Adds a trade and updates the close price of the last bar.
     *
     * @param tradeVolume the traded volume
     *                    成交量
     * @param tradePrice  the price
     * @see Bar#addTrade(Num, Num)
     */
    void addTrade(Num tradeVolume, Num tradePrice);

    /**
     * Updates the close price of the last bar. The open, high and low prices are
     * also updated as needed.
     *
     * @param price the price for the bar
     * @see Bar#addPrice(Num)
     */
    void addPrice(Num price);

    /**
     * Updates the close price of the last bar. The open, high and low prices are
     * also updated as needed.
     *
     * @param price the price for the bar
     * @see Bar#addPrice(Num)
     */
    default void addPrice(String price) {
        addPrice(new BigDecimal(price));
    }

    /**
     * Updates the close price of the last bar. The open, high and low prices are
     * also updated as needed.
     *
     * @param price the price for the bar
     * @see Bar#addPrice(Num)
     */
    default void addPrice(Number price) {
        addPrice(numOf(price));
    }

    /**
     * Returns a new {@link BarSeries} instance (= "subseries") that is a subset of
     * {@code this} BarSeries instance. It contains a copy of all {@link Bar bars}
     * between {@code startIndex} (inclusive) and {@code endIndex} (exclusive) of
     * {@code this} instance. The indices of {@code this} and its subseries can be
     * different, i. e. index 0 of the subseries will be the {@code startIndex} of
     * {@code this}. If {@code startIndex} < this.seriesBeginIndex, then the
     * subseries will start with the first available bar of {@code this}. If
     * {@code endIndex} > this.seriesEndIndex, then the subseries will end at the
     * last available bar of {@code this}.
     *
     * @param startIndex the startIndex (inclusive)
     *                   startIndex（含）
     *
     * @param endIndex   the endIndex (exclusive)
     *                   endIndex（不包括）
     *
     * @return a new BarSeries with Bars from startIndex to endIndex-1
     * * @return 一个新的 BarSeries，从 startIndex 到 endIndex-1
     *
     * @throws IllegalArgumentException if endIndex <= startIndex or startIndex < 0
     * * @throws IllegalArgumentException 如果 endIndex <= startIndex 或 startIndex < 0
     */
    BarSeries getSubSeries(int startIndex, int endIndex);

}
