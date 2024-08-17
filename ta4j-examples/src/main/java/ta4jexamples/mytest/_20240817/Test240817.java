package ta4jexamples.mytest._20240817;

import com.alibaba.excel.EasyExcel;
import com.google.gson.Gson;
import org.ta4j.core.*;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.OverIndicatorRule;
import org.ta4j.core.rules.UnderIndicatorRule;
import org.ta4j.core.utils.BarSeriesUtils;
import ta4jexamples.mytest.EmaV3Indicator;
import ta4jexamples.mytest._20240816.MyOrder;
import ta4jexamples.mytest._20240816.MyOrderExcel;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

public class Test240817 {
    private static  Map trade = new HashMap<String,Boolean>();
    private static TradingRecord tradingRecord = new BaseTradingRecord();
    private static Map position = new HashMap<String,Num>();
    private static List<MyOrder> orders = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        trade.put("isLong",false);
        trade.put("isShort",false);
        trade.put("start",true);

        String filePath = "D:\\Program Files\\Code\\ta4j\\ta4j-core\\src\\main\\resources\\Klines\\BTCUSDT\\2024_08_15\\Binance_BTCUSDT_2024-08-15_m30_1.json";
//        String filePath = "C:\\Users\\Administrator\\Desktop\\backtest\\Binance_BTCUSDT_2024-08-15_m1_1.json";
        BarSeries series = BarSeriesUtils.buildBinanceData(-1, filePath);

        int timeFrame = 60; //时间框架  60=1小时
        //最后一根bar的开始时间
        ZonedDateTime lastBarBeginTime = null;
        Num lastOpenPrice = null;
        boolean ifShort = false;

        BaseBarSeries myBackTest = new BaseBarSeries("myBackTesting");
        EmaV3Indicator closeLine = new EmaV3Indicator(new ClosePriceIndicator(myBackTest), 8);
        EmaV3Indicator openLine = new EmaV3Indicator(new OpenPriceIndicator(myBackTest), 8);
        List<Bar> timeBars = new ArrayList<>();

        // 开多仓规则：当 closeLine 上穿 openLine 时
//        Rule entryLongRule = new OverIndicatorRule(closeLine, openLine).and(new CrossedUpIndicatorRule(closeLine, openLine));
        Rule entryLongRule = new OverIndicatorRule(closeLine, openLine);
        // 平多仓规则：当 closeLine 下穿 openLine 且 ifShort 为 false 时
//        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new IndicatorRule(closeLine, openLine).isLessThan(Num.valueOf(ifShort ? 1 : 0)));
//        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new BooleanRule(!ifShort));
//        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new CrossedDownIndicatorRule(closeLine, openLine));
        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine);

        // 开空仓规则：当 closeLine 下穿 openLine 且 ifShort 为 true 时
//        Rule entryShortRule = new UnderIndicatorRule(closeLine, openLine).and(new CrossedDownIndicatorRule(closeLine, openLine));
        Rule entryShortRule = new UnderIndicatorRule(closeLine, openLine);
        // 平空仓规则：当 closeLine 上穿 openLine 且 ifShort 为 true 时
//        Rule exitShortRule = new OverIndicatorRule(closeLine, openLine).and(new CrossedUpIndicatorRule(closeLine, openLine));
        Rule exitShortRule = new OverIndicatorRule(closeLine, openLine);


//        CrossedUpIndicatorRule crossedUpIndicatorRule = new CrossedUpIndicatorRule(closeLine, openLine);
//        CrossedDownIndicatorRule downIndicatorRule = new CrossedDownIndicatorRule(closeLine, openLine);

        position.put("opLong",null);
        position.put("opShort",null);

        for (int start = 0; start < series.getBarCount(); start++) {
            Bar bar = series.getBar(start);
            if (myBackTest.getBarData().size()< 100){
                myBackTest.addBar(bar);
                continue;
            }
            myBackTest.addBar(bar);
            backTest(entryLongRule,  exitLongRule,  entryShortRule,  exitShortRule,closeLine, openLine, start);

        }
        Bar lastBar = myBackTest.getLastBar();
//        System.out.println(tradingRecord);
//=========================================================
        // 定义Excel文件路径
        String fileName = "D:\\Program Files\\Code\\ta4j\\ta4j-core\\src\\main\\resources\\Klines\\BTCUSDT\\2024_08_15\\Binance_BTCUSDT_2024-08-15_m30_1.xlsx";

        // 创建示例数据
        List<MyOrderExcel> data = new ArrayList<>();

        Num profit = DecimalNum.ZERO;
        int profitQty = 0;
        int lossesQty = 0;
        Gson gson = new Gson();
        for (MyOrder order : orders) {
            Num num = order.getProfit();
            if (num.isGreaterThan(DecimalNum.ZERO)){
                ++profitQty;
            }else if (num.isLessThan(DecimalNum.ZERO)){
                ++lossesQty;
            }
            profit = profit.plus(num);

//            String json = gson.toJson(order);
//            System.out.println(json);
            System.out.println(order);
//            String tradeType,
//            ZonedDateTime orderTime,
//            Double orderPrice,
//            Double klinePrice,
//            Double profit,
//            Double orderEma,
//            Double openEma,
//            Double closeEma
            MyOrderExcel orderExcel = new MyOrderExcel(order.getTradeType(), order.getOrderTime(), order.getOrderPrice().doubleValue(), order.getKlinePrice().doubleValue()
                    , order.getProfit().doubleValue(), order.getOrderEma().doubleValue(), order.getOpenEma().doubleValue(), order.getCloseEma().doubleValue());
            data.add(orderExcel);
        }
        // 使用EasyExcel将数据写入Excel文件
        EasyExcel.write(fileName, MyOrderExcel.class).sheet("数据表").doWrite(data);

        System.out.println("Excel 文件已成功创建！");
        System.out.println("交易总数:"+orders.size()+",盈利数量:"+profitQty+",亏损数量:"+lossesQty+",利润："+profit);
    }

    /**
     * 回测
     * @param closeLine
     * @param openLine
     * @param index
     *
     */
    private static void backTest(Rule entryLongRule, Rule exitLongRule, Rule entryShortRule, Rule exitShortRule
            ,EmaV3Indicator closeLine, EmaV3Indicator openLine,int index) {

//        // 开多仓规则：当 closeLine 上穿 openLine 时
//        Rule entryLongRule = new OverIndicatorRule(closeLine, openLine);
//        // 平多仓规则：当 closeLine 下穿 openLine 且 ifShort 为 false 时
////        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new IndicatorRule(closeLine, openLine).isLessThan(Num.valueOf(ifShort ? 1 : 0)));
////        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine).and(new BooleanRule(!ifShort));
//        Rule exitLongRule = new UnderIndicatorRule(closeLine, openLine);
//
//        // 开空仓规则：当 closeLine 下穿 openLine 且 ifShort 为 true 时
//        Rule entryShortRule = new UnderIndicatorRule(closeLine, openLine);
//        // 平空仓规则：当 closeLine 上穿 openLine 且 ifShort 为 true 时
//        Rule exitShortRule = new OverIndicatorRule(closeLine, openLine);
//========================================测试 无用逻辑
//        ZonedDateTime time = ZonedDateTime.parse("2024-08-12T10:00+08:00[Asia/Shanghai]");
//        if (bar.getBeginTime().isBefore(time)){
//            return;
//        }
//========================================测试


//        Boolean isLong = (Boolean)trade.get("isLong");
//        Boolean isShort = (Boolean)trade.get("isShort");
//        Boolean isStart = (Boolean)trade.get("start");

        Num openLineValue = openLine.getValue(index);
        Num closeLineValue = closeLine.getValue(index);

        Bar bar = closeLine.getBarSeries().getBar(index);

        Num opLong = (Num)position.get("opLong");
        Num opShort = (Num)position.get("opShort");
        boolean closeLong = false;
        boolean closeShort = false;

        if (Objects.nonNull(opLong)){
            Num plusPrice = opLong.multipliedBy(DecimalNum.valueOf(0.01)).plus(opLong);
            Num minusPrice = opLong.minus(opLong.multipliedBy(DecimalNum.valueOf(0.01)));
            if (bar.getClosePrice().isGreaterThanOrEqual(plusPrice) || bar.getClosePrice().isLessThanOrEqual(minusPrice)){
                closeLong = true;
            }
        }
        if (Objects.nonNull(opShort)){
            Num plusPrice = opShort.multipliedBy(DecimalNum.valueOf(0.01)).plus(opShort);
            Num minusPrice = opShort.minus(opShort.multipliedBy(DecimalNum.valueOf(0.01)));
            if (bar.getClosePrice().isGreaterThanOrEqual(plusPrice) || bar.getClosePrice().isLessThanOrEqual(minusPrice)){
                closeShort = true;
            }
        }
//
//        if (closeLong){
//            System.out.println();
//        }
//        if (closeShort){
//            System.out.println();
//        }

        //第一次开盘必须是第一次 金叉
        if ((Boolean)trade.get("start")){
//            Num emaClose = closeLine.getValue(index - 1);
//            Num emaOpen = openLine.getValue(index - 1);
            if (entryLongRule.isSatisfied(index)){
                //System.out.println("开多, 当前时间: "+bar.getEndTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isLong",true);
                trade.put("start",false);
//                tradingRecord.enter(index, bar.getClosePrice(), DecimalNum.valueOf(100));
//                tradingRecord.enter(index, bar.getClosePrice(), DecimalNum.valueOf(100));
                position.put("opLong",bar.getClosePrice());
                MyOrder order = new MyOrder("开多", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), DecimalNum.ZERO, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);
            }
        }


        if (entryLongRule.isSatisfied(index)){
            //如果当前 有了多单和空则不开
            if (!(Boolean)trade.get("isLong") && !(Boolean)trade.get("start") && !(Boolean)trade.get("isShort")){
                //System.out.println("开多, 当前时间: "+bar.getEndTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isLong",true);
//                tradingRecord.enter(index, bar.getClosePrice(), DecimalNum.valueOf(100));
                position.put("opLong",bar.getClosePrice());

                MyOrder order = new MyOrder("开多", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), DecimalNum.ZERO, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);
            }
        }

        if (exitLongRule.isSatisfied(index) || closeLong){
            //如果当前 没有了多单则不平
            if ((Boolean)trade.get("isLong") && !(Boolean)trade.get("start")){
                //System.out.println("平多, 当前时间: "+bar.getEndTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isLong",false);
//                tradingRecord.exit(index, bar.getClosePrice(), DecimalNum.valueOf(100));

                Num result = opLong.minus(bar.getClosePrice());
                MyOrder order = new MyOrder("平多", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), result, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);

                position.put("opLong",null);

//                System.out.println("-------------------------------------");
            }
        }

        if (entryShortRule.isSatisfied(index)){
            //如果当前 有了多单和空单则不开
            if (!(Boolean)trade.get("isLong") && !(Boolean)trade.get("start") && !(Boolean)trade.get("isShort")){
//                System.out.println("开空, 当前时间: "+bar.getEndTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isShort",true);
//                tradingRecord.exit(index, bar.getClosePrice(), DecimalNum.valueOf(100));
                position.put("opShort",bar.getClosePrice());

                MyOrder order = new MyOrder("开空", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), DecimalNum.ZERO, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);

            }
        }
        if (exitShortRule.isSatisfied(index) || closeShort){
            //如果当前 没有了空单则不开
            if (!(Boolean)trade.get("isLong") && !(Boolean)trade.get("start") && (Boolean)trade.get("isShort")){
//                System.out.println("平空, 当前时间: "+bar.getEndTime()+",当前开盘ema： "+openLineValue+",收盘ema： "+closeLineValue);
                trade.put("isShort",false);
//                tradingRecord.enter(index, bar.getClosePrice(), DecimalNum.valueOf(100));
                position.put("opShort",null);

                Num result = opShort.minus(bar.getClosePrice());
                MyOrder order = new MyOrder("平空", bar.getEndTime(), bar.getClosePrice(), bar.getClosePrice(), result, closeLineValue, openLineValue, closeLineValue);
                orders.add(order);

//                System.out.println("-------------------------------------");
            }
        }
    }
//===================================策略回测 结束================================================
}