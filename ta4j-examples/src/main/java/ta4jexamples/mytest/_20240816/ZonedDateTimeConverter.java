package ta4jexamples.mytest._20240816;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.metadata.GlobalConfiguration;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeConverter implements Converter<ZonedDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Class<ZonedDateTime> supportJavaTypeKey() {
        return ZonedDateTime.class;
    }

    @Override
    public WriteCellData<?> convertToExcelData(ZonedDateTime value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        // 将 ZonedDateTime 转换为字符串格式，并包装在 CellData 中
        return new WriteCellData<>(FORMATTER.format(value));
    }

//    @Override
//    public ZonedDateTime convertToJavaData(CellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
//        // 将 Excel 中的字符串转换回 ZonedDateTime 对象
//        return ZonedDateTime.parse(cellData.getStringValue(), FORMATTER);
//    }
}




