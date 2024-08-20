package ta4jexamples.mytest._240818;

import com.alibaba.excel.annotation.ExcelProperty;

import java.time.ZonedDateTime;

public class MyTradingRecordAnalysis {
    @ExcelProperty(value = "连续数")
    private Integer count;
    //    @ExcelProperty(value ="出现次数(盈利)" )
//    private Integer frequency;
    @ExcelProperty(value = "开始盈利时间")
    private ZonedDateTime startTime;

    @ExcelProperty(value = "状态：0:盈利，1:亏损")
    private Integer status = 0;

    public MyTradingRecordAnalysis(Integer count, ZonedDateTime startTime, Integer status) {
        this.count = count;
        this.startTime = startTime;
        this.status = status;
    }

    public MyTradingRecordAnalysis() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}