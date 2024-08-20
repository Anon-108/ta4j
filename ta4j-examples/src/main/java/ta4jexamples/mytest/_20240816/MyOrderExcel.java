package ta4jexamples.mytest._20240816;

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
    @ExcelProperty(value ="交易总数(包含开单)" )
    private Integer count;
    @ExcelProperty(value ="盈利数量" )
    private Integer profitQty;
    @ExcelProperty(value ="亏损数量" )
    private Integer lossesQty;
    @ExcelProperty(value ="盈率" )
    private Double proportion;
    @ExcelProperty(value ="总盈亏" )
    private Double countProfit;

    @ExcelProperty(value ="连续盈利数" )
    private Integer maxProfitCount;
    //    @ExcelProperty(value ="出现次数(盈利)" )
//    private Integer frequency;
    @ExcelProperty(value ="开始盈利时间",converter = ZonedDateTimeConverter.class )
    private ZonedDateTime startingProfitTime;

    @ExcelProperty(value ="连续亏损数" )
    private Integer maxLossesCount;
    //    @ExcelProperty(value ="出现次数(亏损)" )
//    private Integer frequency2;
    @ExcelProperty(value ="开始亏损时间",converter = ZonedDateTimeConverter.class )
    private ZonedDateTime startingTimeOfLoss;


    //    @ExcelIgnore
    @ExcelProperty(value ="订单ema" )
    private Double orderEma;
    //    @ExcelIgnore
    @ExcelProperty(value ="开盘ema" )
    private Double openEma;
    //    @ExcelIgnore
    @ExcelProperty(value ="收盘ema" )
    private Double closeEma;

    @ExcelProperty(value ="rsiBarCount" )
    private Integer rsiBarCount;
    @ExcelProperty(value ="barCount1" )
    private Integer emaBarCount1;
    @ExcelProperty(value ="barCount2" )
    private Integer emaBarCount2;

    public MyOrderExcel(Double closeEma, String tradeType, ZonedDateTime orderTime, Double orderPrice, Double klinePrice, Double profit, Integer count, Integer profitQty, Integer lossesQty, Double proportion, Double countProfit, Integer maxProfitCount, ZonedDateTime startingProfitTime, Integer maxLossesCount, ZonedDateTime startingTimeOfLoss, Double orderEma, Double openEma, Integer rsiBarCount, Integer emaBarCount1, Integer emaBarCount2) {
        this.closeEma = closeEma;
        this.tradeType = tradeType;
        this.orderTime = orderTime;
        this.orderPrice = orderPrice;
        this.klinePrice = klinePrice;
        this.profit = profit;
        this.count = count;
        this.profitQty = profitQty;
        this.lossesQty = lossesQty;
        this.proportion = proportion;
        this.countProfit = countProfit;
        this.maxProfitCount = maxProfitCount;
        this.startingProfitTime = startingProfitTime;
        this.maxLossesCount = maxLossesCount;
        this.startingTimeOfLoss = startingTimeOfLoss;
        this.orderEma = orderEma;
        this.openEma = openEma;
        this.rsiBarCount = rsiBarCount;
        this.emaBarCount1 = emaBarCount1;
        this.emaBarCount2 = emaBarCount2;
    }

    public MyOrderExcel(String tradeType, ZonedDateTime orderTime, Double orderPrice, Double klinePrice, Double profit, Integer count, Integer profitQty, Integer lossesQty, Double proportion, Integer maxProfitCount, Integer frequency, ZonedDateTime startingProfitTime, Integer maxLossesCount, Integer frequency2, ZonedDateTime startingTimeOfLoss, Double orderEma, Double openEma, Double closeEma) {
        this.tradeType = tradeType;
        this.orderTime = orderTime;
        this.orderPrice = orderPrice;
        this.klinePrice = klinePrice;
        this.profit = profit;
        this.count = count;
        this.profitQty = profitQty;
        this.lossesQty = lossesQty;
        this.proportion = proportion;
        this.maxProfitCount = maxProfitCount;
//        this.frequency = frequency;
        this.startingProfitTime = startingProfitTime;
        this.maxLossesCount = maxLossesCount;
//        this.frequency2 = frequency2;
        this.startingTimeOfLoss = startingTimeOfLoss;
        this.orderEma = orderEma;
        this.openEma = openEma;
        this.closeEma = closeEma;
    }

    public MyOrderExcel(String tradeType, ZonedDateTime orderTime, Double orderPrice, Double klinePrice, Integer profitQty, Integer lossesQty, Integer count, Double profit, Double proportion, Double orderEma, Double openEma, Double closeEma) {
        this.tradeType = tradeType;
        this.orderTime = orderTime;
        this.orderPrice = orderPrice;
        this.klinePrice = klinePrice;
        this.profitQty = profitQty;
        this.lossesQty = lossesQty;
        this.count = count;
        this.profit = profit;
        this.proportion = proportion;
        this.orderEma = orderEma;
        this.openEma = openEma;
        this.closeEma = closeEma;
    }

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

    public Double getProportion() {
        return proportion;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
    }

    public Integer getProfitQty() {
        return profitQty;
    }

    public void setProfitQty(Integer profitQty) {
        this.profitQty = profitQty;
    }

    public Integer getLossesQty() {
        return lossesQty;
    }

    public void setLossesQty(Integer lossesQty) {
        this.lossesQty = lossesQty;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getMaxProfitCount() {
        return maxProfitCount;
    }

    public void setMaxProfitCount(Integer maxProfitCount) {
        this.maxProfitCount = maxProfitCount;
    }

//    public Integer getFrequency() {
//        return frequency;
//    }
//
//    public void setFrequency(Integer frequency) {
//        this.frequency = frequency;
//    }

    public ZonedDateTime getStartingProfitTime() {
        return startingProfitTime;
    }

    public void setStartingProfitTime(ZonedDateTime startingProfitTime) {
        this.startingProfitTime = startingProfitTime;
    }

    public Integer getMaxLossesCount() {
        return maxLossesCount;
    }

    public void setMaxLossesCount(Integer maxLossesCount) {
        this.maxLossesCount = maxLossesCount;
    }

//    public Integer getFrequency2() {
//        return frequency2;
//    }
//
//    public void setFrequency2(Integer frequency2) {
//        this.frequency2 = frequency2;
//    }

    public ZonedDateTime getStartingTimeOfLoss() {
        return startingTimeOfLoss;
    }

    public void setStartingTimeOfLoss(ZonedDateTime startingTimeOfLoss) {
        this.startingTimeOfLoss = startingTimeOfLoss;
    }

    public Double getCountProfit() {
        return countProfit;
    }

    public void setCountProfit(Double countProfit) {
        this.countProfit = countProfit;
    }

    public Integer getRsiBarCount() {
        return rsiBarCount;
    }

    public void setRsiBarCount(Integer rsiBarCount) {
        this.rsiBarCount = rsiBarCount;
    }

    public Integer getEmaBarCount1() {
        return emaBarCount1;
    }

    public void setEmaBarCount1(Integer emaBarCount1) {
        this.emaBarCount1 = emaBarCount1;
    }

    public Integer getEmaBarCount2() {
        return emaBarCount2;
    }

    public void setEmaBarCount2(Integer emaBarCount2) {
        this.emaBarCount2 = emaBarCount2;
    }
}
