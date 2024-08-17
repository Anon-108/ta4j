package ta4jexamples.mytest._20240816;

import com.alibaba.excel.annotation.ExcelProperty;
import org.ta4j.core.num.Num;

import java.time.ZonedDateTime;

public class MyOrder {
    //    private Trade.TradeType
    @ExcelProperty("开单方向")
    private String tradeType;
    @ExcelProperty(value = "开单时间", converter = ZonedDateTimeConverter.class)
    private ZonedDateTime orderTime;
    @ExcelProperty(value = "开单价格", converter = NumConverter.class)
    private Num orderPrice;
    @ExcelProperty(value ="当前价格" , converter = NumConverter.class)
    private Num klinePrice;
    @ExcelProperty(value ="利润" , converter = NumConverter.class)
    private Num profit;
    private Num orderEma;
    private Num openEma;
    private Num closeEma;

    public MyOrder(String tradeType, ZonedDateTime orderTime, Num orderPrice, Num klinePrice, Num profit, Num orderEma, Num openEma, Num closeEma) {
        this.tradeType = tradeType;
        this.orderTime = orderTime;
        this.orderPrice = orderPrice;
        this.klinePrice = klinePrice;
        this.profit = profit;
        this.orderEma = orderEma;
        this.openEma = openEma;
        this.closeEma = closeEma;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public ZonedDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(ZonedDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public Num getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Num orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Num getKlinePrice() {
        return klinePrice;
    }

    public void setKlinePrice(Num klinePrice) {
        this.klinePrice = klinePrice;
    }

    public Num getProfit() {
        return profit;
    }

    public void setProfit(Num profit) {
        this.profit = profit;
    }

    public Num getOrderEma() {
        return orderEma;
    }

    public void setOrderEma(Num orderEma) {
        this.orderEma = orderEma;
    }

    public Num getOpenEma() {
        return openEma;
    }

    public void setOpenEma(Num openEma) {
        this.openEma = openEma;
    }

    public Num getCloseEma() {
        return closeEma;
    }

    public void setCloseEma(Num closeEma) {
        this.closeEma = closeEma;
    }

//    @Override
//    public String toString() {
//        return "{" +
//                "方向='" + tradeType + '\'' +
//                ", 开单时间=" + orderTime +
//                ", 开单价格=" + orderPrice +
//                ", K线价格=" + klinePrice +
//                ", 利润=" + profit +
//                ", orderEma=" + orderEma +
//                ", openEma=" + openEma +
//                ", closeEma=" + closeEma +
//                '}';
//    }
    @Override
    public String toString() {
        return "{" +
                "方向=" + tradeType +
                ", 开单时间=" + orderTime +
                ", 开单价格=" + orderPrice +
                ", K线价格=" + klinePrice +
                ", 利润=" + profit +
                '}';
    }

}
