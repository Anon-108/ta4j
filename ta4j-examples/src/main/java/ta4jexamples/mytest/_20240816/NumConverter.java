package ta4jexamples.mytest._20240816;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.metadata.GlobalConfiguration;
import org.ta4j.core.num.DecimalNum;

import java.math.BigDecimal;

public class NumConverter implements Converter<Double> {

    @Override
    public Class<Double> supportJavaTypeKey() {
        return Double.class;
    }

    @Override
    public WriteCellData<?> convertToExcelData(Double value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return new WriteCellData<>();
    }

//    @Override
    public Double convertToJavaData(WriteCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        // 如果数据是 DecimalNum 类型
            DecimalNum decimalNum = (DecimalNum) cellData.getData();
            return decimalNum.doubleValue(); // 将 DecimalNum 转换为 Integer

    }
}
