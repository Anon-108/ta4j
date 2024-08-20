package ta4jexamples.mytest._20240820;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

public class ListFilesInDirectory {

    public static void main(String[] args) {
        // 假设我们要读取的目录是当前工作目录
        File directory = new File("D:\\Program Files\\Code\\tradeData\\Data\\binance\\s1\\2024_08_18");
        String createFileName = null;

        // 使用listFiles()方法获取目录下的所有文件和文件夹
        File[] files = directory.listFiles();

        // 检查目录是否为空或不存在
        if (files != null) {
            for (File file : files) {
                createFileName = file.getName(); //获取代币目录，并创建目录
                String filePath = "D:\\Program Files\\Code\\Hengxinchuang\\ta4j\\ta4j-examples\\src\\main\\resources\\Excel\\randomEmaTest\\_20240821\\"+createFileName;
                Path createFilePath = Paths.get(filePath);

                if (!Files.exists(createFilePath)) {
                    try {
                        // 如果目录不存在则创建
                        Files.createDirectories(createFilePath);
                        System.out.println("目录创建成功: " + createFilePath.toAbsolutePath());
                    } catch (IOException e) {
                        // 处理创建目录时的异常
                        System.err.println("无法创建目录: " + e.getMessage());
                        continue;
                    }
                } else {
                    System.out.println("目录已存在: " + createFilePath.toAbsolutePath());
                    continue;
                }


                File[] files2 = file.listFiles();
                // 检查目录是否为空或不存在
                if (files != null) {
                    // 使用自定义的比较器按最后修改时间对文件进行排序
                    Arrays.sort(files2, new Comparator<File>() {
                        @Override
                        public int compare(File f1, File f2) {
                            // 如果f1比f2最后修改时间早，则返回正数；如果晚，则返回负数
                            return Long.compare(f1.lastModified(), f2.lastModified());
                        }
                    });

                    // 打印排序后的文件名和最后修改时间
                    for (File file2 : files2) {
                        if (!file2.isDirectory()) { // 假设我们只关心文件，不关心目录
                            System.out.println(file2.getName() + " - Last Modified: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(file2.lastModified())));
                        }
                    }
                } else {
                    System.out.println("目录不存在或为空");
                }
            }
        } else {
            System.out.println("目录不存在或为空");
        }
    }
}
