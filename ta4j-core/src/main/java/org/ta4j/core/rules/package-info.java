/**
 * 麻省理工学院许可证 (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * 特此免费授予任何获得以下文件副本的人的许可
 * 本软件及相关文档文件（以下简称“软件”），用于处理
 * 本软件不受限制，包括但不限于以下权利
 * 使用、复制、修改、合并、发布、分发、再许可和/或出售
 * 软件，并允许向其提供软件的人这样做，
 * 须符合以下条件：
 *
 * 以上版权声明和本许可声明应包含在所有
 * 软件的副本或大部分。
 *
 * 本软件按“原样”提供，不提供任何形式的明示或
 * 暗示，包括但不限于对适销性、适用性的保证
 * 出于特定目的和非侵权。在任何情况下，作者或
 *版权持有人应对任何索赔、损害或其他责任负责，无论是
 * 在合同、侵权或其他方面的诉讼中，由、出于或在
 * 与软件或软件中的使用或其他交易的连接。
 */
/**
 * Trading rules for trading strategies.
 * 交易策略的交易规则。
 *
 * <p>
 * A {@link org.ta4j.core.Rule rule} can be combined and set as entry/exit
 * signals for a {@link org.ta4j.core.Strategy trading strategy}. A trading
 * strategy is designed to achieve a profitable return by going long or short
 * over a {@link org.ta4j.core.BarSeries series}.
 */
package org.ta4j.core.rules;