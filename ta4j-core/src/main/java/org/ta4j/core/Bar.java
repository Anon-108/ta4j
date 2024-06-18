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
import java.util.function.Function;

import org.ta4j.core.num.Num;

/**
 * A {@code Bar} is aggregated open/high/low/close/volume/etc. data over a time
 * period. It represents the "end bar" of a time period.
 */
public interface Bar extends Serializable {

    /**
     * @return the time period of the bar
     * * @return 柱的时间段
     */
    Duration getTimePeriod();

    /**
     * @return the begin timestamp of the bar period
     * * @return 柱周期的开始时间戳
     */
    ZonedDateTime getBeginTime();

    /**
     * @return the end timestamp of the bar period
     * * @return 柱周期的结束时间戳
     */
    ZonedDateTime getEndTime();

    /**
     * @return the open price of the bar period
     */
    Num getOpenPrice();

    /**
     * @return the high price of the bar period
     */
    Num getHighPrice();

    /**
     * @return the low price of the bar period
     */
    Num getLowPrice();

    /**
     * @return the close price of the bar period
     */
    Num getClosePrice();

    /**
     * @return the total traded volume of the bar period
     */
    Num getVolume();

    /**
     * @return the total traded amount (tradePrice x tradeVolume) of the bar period
     */
    Num getAmount();

    /**
     * @return the number of trades of the bar period
     */
    long getTrades();

    /**
     * @param timestamp a timestamp
     *                  时间戳
     * @return true if the provided timestamp is between the begin time and the end  time of the current period, false otherwise
     *          * @return 如果提供的时间戳在当前周期的开始时间和结束时间之间，则返回 true，否则返回 false
     */
    default boolean inPeriod(ZonedDateTime timestamp) {
        return timestamp != null && !timestamp.isBefore(getBeginTime()) && timestamp.isBefore(getEndTime());
    }

    /**
     * @return a human-friendly string of the end timestamp
     * * @return 结束时间戳的人性化字符串
     */
    default String getDateName() {
        return getEndTime().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * @return a even more human-friendly string of the end timestamp
     * * @return 一个更加人性化的结束时间戳字符串
     */
    default String getSimpleDateName() {
        return getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * @return true if this is a bearish bar, false otherwise
     * * @return 如果这是一个看跌柱，则返回 true，否则返回 false
     */
    default boolean isBearish() {
        Num openPrice = getOpenPrice();
        Num closePrice = getClosePrice();
        return (openPrice != null) && (closePrice != null) && closePrice.isLessThan(openPrice);
    }

    /**
     * @return true if this is a bullish bar, false otherwise
     * * @return 如果这是一个看涨柱，则返回 true，否则返回 false
     */
    default boolean isBullish() {
        Num openPrice = getOpenPrice();
        Num closePrice = getClosePrice();
        return (openPrice != null) && (closePrice != null) && openPrice.isLessThan(closePrice);
    }

    /**
     * Adds a trade and updates the close price at the end of the bar period.
     *
     * @param tradeVolume the traded volume
     * @param tradePrice  the actual price per asset
     */
    void addTrade(Num tradeVolume, Num tradePrice);

    /**
     * Updates the close price at the end of the bar period. The open, high and low
     * prices are also updated as needed.
     *
     * @param price       the actual price per asset
     * @param numFunction the numbers precision
     */
    default void addPrice(String price, Function<Number, Num> numFunction) {
        addPrice(numFunction.apply(new BigDecimal(price)));
    }

    /**
     * Updates the close price at the end of the bar period. The open, high and low
     * prices are also updated as needed.
     *
     * @param price       the actual price per asset
     * @param numFunction the numbers precision
     */
    default void addPrice(Number price, Function<Number, Num> numFunction) {
        addPrice(numFunction.apply(price));
    }

    /**
     * Updates the close price at the end of the bar period. The open, high and low
     * prices are also updated as needed.
     *
     * @param price the actual price per asset
     */
    void addPrice(Num price);
}
