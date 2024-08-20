package ta4jexamples.mytest._240818;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.context.AnalysisContext;
import ta4jexamples.mytest._20240816.MyOrderExcel;
import ta4jexamples.mytest._20240817.OrderRecord;

import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public List<OrderRecord> getOrderRecords(String readFilePath){
        OrderRecordListener orderRecordListener = new OrderRecordListener();
        EasyExcel.read(readFilePath, OrderRecord.class,orderRecordListener ).sheet().doRead();
        return orderRecordListener.getList();
    }

    // 定义一个监听器，用来处理每一行的数据
    public static class OrderRecordListener implements ReadListener<OrderRecord> {
        private List<OrderRecord> list = new ArrayList<>();

        @Override
        public void invoke(OrderRecord orderRecord, AnalysisContext analysisContext) {
            list.add(orderRecord); // 可以在这里对每一行的数据进行处理
//            System.out.println("读取到数据: " + orderRecord);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            // 全部读取完后，可以在这里处理最终的结果，比如将数据存入数据库
            System.out.println("所有数据读取完毕！");
        }
        public List<OrderRecord> getList(){
            return list;
        }
    }

    public static void main(String[] args) {
        String readFileName = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\randomEmaTest\\RandomEmaTestRSI_2_file2.xlsx"; // 文件路径
        String writeFileName = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\randomEmaTest\\RandomEmaTestRSI_2_file2_筛选后的指标.xlsx"; // 文件路径
        List<OrderRecord> writeOrderRecords = new ArrayList<>();

        OrderRecordListener orderRecordListener = new OrderRecordListener();
        // 使用 EasyExcel 读取 Excel 文件
        EasyExcel.read(readFileName, OrderRecord.class,orderRecordListener ).sheet().doRead();
        List<OrderRecord> list = orderRecordListener.getList();
        for (OrderRecord orderRecord : list) {
            if (orderRecord.getCount() > 276 && orderRecord.getProportion() >= 51){
                writeOrderRecords.add(orderRecord);
            }
        }
//        EasyExcel.write(fileName, MyOrderExcel.class).sheet("数据表").doWrite(data);

        EasyExcel.write(writeFileName,OrderRecord.class).sheet("RandomEmaTestRSI_2_file2_筛选后的指标").doWrite(writeOrderRecords);
        System.out.println("写入完毕： "+writeFileName);


    }
}

