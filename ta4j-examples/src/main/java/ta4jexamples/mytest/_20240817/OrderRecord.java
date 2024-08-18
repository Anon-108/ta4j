package ta4jexamples.mytest._20240817;

import com.alibaba.excel.annotation.ExcelProperty;

public class OrderRecord {
//    交易总数:60,盈利数量:16.0,亏损数量:14.0,利润：550.09 胜率:53.333333333333336
    @ExcelProperty("交易总数")
    private Integer count;
    @ExcelProperty("盈利数量")
    private Double profitQty;
    @ExcelProperty("亏损数量")
    private Double lossQty;
    @ExcelProperty("利润")
    private Double profit;
    @ExcelProperty("胜率")
    private Double proportion;
    private Integer barCount1;
    private Integer barCount2;
    @ExcelProperty("最大连续盈利次数")
    private Integer maxProfit;
    @ExcelProperty("最大连续亏损次数")
    private Integer maxLoss;

    public OrderRecord(Integer count, Double profitQty, Double lossQty, Double profit, Double proportion, Integer barCount1, Integer barCount2, Integer maxProfit, Integer maxLoss) {
        this.count = count;
        this.profitQty = profitQty;
        this.lossQty = lossQty;
        this.profit = profit;
        this.proportion = proportion;
        this.barCount1 = barCount1;
        this.barCount2 = barCount2;
        this.maxProfit = maxProfit;
        this.maxLoss = maxLoss;
    }
    public OrderRecord(Integer count, Double profitQty, Double lossQty, Double profit, Double proportion, Integer barCount1, Integer barCount2) {
        this.count = count;
        this.profitQty = profitQty;
        this.lossQty = lossQty;
        this.profit = profit;
        this.proportion = proportion;
        this.barCount1 = barCount1;
        this.barCount2 = barCount2;
        this.maxProfit = null;
        this.maxLoss = null;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getProfitQty() {
        return profitQty;
    }

    public void setProfitQty(Double profitQty) {
        this.profitQty = profitQty;
    }

    public Double getLossQty() {
        return lossQty;
    }

    public void setLossQty(Double lossQty) {
        this.lossQty = lossQty;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getProportion() {
        return proportion;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
    }

    public Integer getBarCount1() {
        return barCount1;
    }

    public void setBarCount1(Integer barCount1) {
        this.barCount1 = barCount1;
    }

    public Integer getBarCount2() {
        return barCount2;
    }

    public void setBarCount2(Integer barCount2) {
        this.barCount2 = barCount2;
    }

    public Integer getMaxProfit() {
        return maxProfit;
    }

    public void setMaxProfit(Integer maxProfit) {
        this.maxProfit = maxProfit;
    }

    public Integer getMaxLoss() {
        return maxLoss;
    }

    public void setMaxLoss(Integer maxLoss) {
        this.maxLoss = maxLoss;
    }
}
