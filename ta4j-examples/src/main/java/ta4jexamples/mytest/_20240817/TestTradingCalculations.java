package ta4jexamples.mytest._20240817;

import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

public class TestTradingCalculations {
    public static void main(String[] args) {
        //止盈亏比例
        Num profitAndLossRatio = DecimalNum.valueOf(1.26).dividedBy(DecimalNum.valueOf(100));
        System.out.println(profitAndLossRatio.doubleValue());

        // 输入数据
//        double openingPrice = 0.16659;  // 开仓价格
//        double takeProfitPrice = 0.16680;  // 止盈价格
//        double stopLossPrice = 0.16616;  // 止损价格
//        double quantity = 3010;       // 数量
//        double marginRatio = 0.1;    // 保证金比例
//        double leverage = 10.0;       // 杠杆倍数

//        double openingPrice = 0.08019;  // 开仓价格
//        double takeProfitPrice = 0.07997;  // 止盈价格
//        double stopLossPrice = 0.08;  // 止损价格
//        double quantity = 6157;       // 数量
//        double marginRatio = 0.1;    // 保证金比例
//        double leverage = 10.0;       // 杠杆倍数
//
//
//        // 计算保证金
//        double margin = (openingPrice * quantity) / leverage;
//
//        // 计算止盈盈亏
//        double profitAtTakeProfit = (takeProfitPrice - openingPrice) * quantity;
//
//        // 计算止盈回报率
//        double returnRateAtTakeProfit = (profitAtTakeProfit / margin) * 100;
//
//        // 计算止损盈亏
//        double lossAtStopLoss = (stopLossPrice - openingPrice) * quantity;
//
//        // 计算止损回报率
//        double returnRateAtStopLoss = (lossAtStopLoss / margin) * 100;
//
//        // 计算风险/回报比
//        double riskRewardRatio = lossAtStopLoss / profitAtTakeProfit;
//
//        // 输出结果
//        System.out.println("保证金: " + margin);
//        System.out.println("风险/回报比: " + riskRewardRatio);
//        System.out.println("止盈盈亏: " + profitAtTakeProfit);
//        System.out.println("止盈回报率: " + returnRateAtTakeProfit + "%");
//        System.out.println("止损盈亏: " + lossAtStopLoss);
//        System.out.println("止损回报率: " + returnRateAtStopLoss + "%");

        System.out.println("===========================================");
        // 输入数据
        double openingPrice = 0.16659;  // 开仓价格
        double leverage = 10.0;         // 杠杆倍数
        double profitLossPercentage = 1.26 / 100;  // 止盈/止损百分比
        double quantity = 3010;         // 数量

        // 计算止盈价格和止损价格
        double takeProfitPrice = openingPrice * (1 + profitLossPercentage);
        double stopLossPrice = openingPrice * (1 - profitLossPercentage);

        // 计算止盈和止损金额
        double profitAtTakeProfit = (takeProfitPrice - openingPrice) * quantity * leverage;
        double lossAtStopLoss = (stopLossPrice - openingPrice) * quantity * leverage;

        // 输出结果
        System.out.println("开仓价格: " + openingPrice);
        System.out.println("止盈价格: " + takeProfitPrice);
        System.out.println("止损价格: " + stopLossPrice);
        System.out.println("止盈金额: " + profitAtTakeProfit);
        System.out.println("止损金额: " + lossAtStopLoss);



    }
}
