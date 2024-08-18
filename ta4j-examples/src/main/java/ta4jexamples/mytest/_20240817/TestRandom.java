package ta4jexamples.mytest._20240817;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
public class TestRandom {


    public static void main(String[] args) {
        final int MAX_VALUE = 100;
        Set<String> generatedPairs = new HashSet<>();
        Random random = new Random();
        while (true) {
            // 生成一对随机数
            int a = random.nextInt(MAX_VALUE) + 1;
            int b = random.nextInt(MAX_VALUE) + 1;

            // 构造数对字符串
            String pair = a + "," + b;

            // 检查是否已经生成过
            if (!generatedPairs.contains(pair)) {
                // 未生成过，则输出并存储
                System.out.println("Generated pair: (" + a + ", " + b + ")");
                generatedPairs.add(pair);
            }

            // 检查是否已经生成了所有可能的数对
            if (generatedPairs.size() >= MAX_VALUE * 10) {
                System.out.println("All possible pairs have been generated.");
                break;
            }
        }
    }
}
