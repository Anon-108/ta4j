package ta4jexamples.mytest._20240816;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

import java.time.ZonedDateTime;

public class MyOrderExcel {
    //    private Trade.TradeType
    @ExcelProperty("开单方向")
    private String tradeType;
    @ExcelProperty(value = "开单时间", converter = ZonedDateTimeConverter.class)
    private ZonedDateTime orderTime;
    @ExcelProperty(value = "开单价格")
    private Double orderPrice;
    @ExcelProperty(value ="当前价格" )
    private Double klinePrice;
    @ExcelProperty(value ="利润" )
    private Double profit;

    @ExcelIgnore
    private Double orderEma;
    @ExcelIgnore
    private Double openEma;
    @ExcelIgnore
    private Double closeEma;

    public MyOrderExcel(String tradeType, ZonedDateTime orderTime, Double orderPrice, Double klinePrice, Double profit, Double orderEma, Double openEma, Double closeEma) {
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

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Double getKlinePrice() {
        return klinePrice;
    }

    public void setKlinePrice(Double klinePrice) {
        this.klinePrice = klinePrice;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getOrderEma() {
        return orderEma;
    }

    public void setOrderEma(Double orderEma) {
        this.orderEma = orderEma;
    }

    public Double getOpenEma() {
        return openEma;
    }

    public void setOpenEma(Double openEma) {
        this.openEma = openEma;
    }

    public Double getCloseEma() {
        return closeEma;
    }

    public void setCloseEma(Double closeEma) {
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
