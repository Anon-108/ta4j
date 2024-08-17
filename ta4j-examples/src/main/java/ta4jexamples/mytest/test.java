package ta4jexamples.mytest;
import java.util.ArrayList;
import java.util.List;

public class test {

    // 计算EMA
    public static List<Double> calculateEMA(List<Double> prices, int period) {
        List<Double> emaList = new ArrayList<>();
        if (prices.isEmpty() || period <= 0) {
            return emaList;
        }

        double alpha = 2.0 / (period + 1);
        Double ema = null;

        for (double price : prices) {
            if (ema == null) {
                ema = price; // 初始值设置为第一条价格
            } else {
                ema = (alpha * price) + ((1 - alpha) * ema);
            }
            emaList.add(ema);
        }

        return emaList;
    }

    // 将1小时数据汇总为1日数据
    public static List<Double> aggregateHourlyToDaily(List<Double> hourlyPrices) {
        List<Double> dailyPrices = new ArrayList<>();
        int hoursPerDay = 24;

        for (int i = 0; i < hourlyPrices.size(); i += hoursPerDay) {
            int start = i + hoursPerDay - 1;
            if (start > hourlyPrices.size()-1){
                double dayStart = hourlyPrices.get(i);
                double endNum = hourlyPrices.get(hourlyPrices.size()-1);
                System.out.println();
                break;
            }

            double dailyClose = hourlyPrices.get(start); // 取最后一个小时的价格作为日线收盘价
            dailyPrices.add(dailyClose);
            if (i >= 47){
                System.out.println();
            }
        }

        return dailyPrices;
    }

    public static void main(String[] args) {
        // 示例数据
        List<Double> hourlyPrices = new ArrayList<>();
//        for (int i = 0; i < 720; i++) { // 30天的小时数据
        for (int i = 0; i < 65; i++) { // 30天的小时数据
            hourlyPrices.add(100.0 + Math.random() * 10); // 随机生成价格数据
        }

        // 汇总1小时数据为1日数据
        List<Double> dailyPrices = aggregateHourlyToDaily(hourlyPrices);

        // 计算1日级别的EMA
        int period = 14; // EMA周期
        List<Double> dailyEMA = calculateEMA(dailyPrices, period);

        // 输出EMA结果
        for (int i = 0; i < dailyEMA.size(); i++) {
            System.out.printf("Day %d: EMA = %.2f%n", i + 1, dailyEMA.get(i));
        }
    }
}
