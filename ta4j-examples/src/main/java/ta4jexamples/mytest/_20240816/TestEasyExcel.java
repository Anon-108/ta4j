package ta4jexamples.mytest._20240816;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    public static void main(String[] args) {
        // 定义Excel文件路径
        String fileName = "D:\\Program Files\\Code\\ta4j\\ta4j-core\\src\\main\\resources\\Klines\\BTCUSDT\\2024_08_15\\easy_example.xlsx";

        // 创建示例数据
        List<Person> data = new ArrayList<>();
        data.add(new Person("张三", 25, "男"));
        data.add(new Person("李四", 30, "女"));
        data.add(new Person("王五", 28, "男"));

        // 使用EasyExcel将数据写入Excel文件
        EasyExcel.write(fileName, Person.class).sheet("数据表").doWrite(data);

        System.out.println("Excel 文件已成功创建！");
    }

    // 定义一个数据类（每一行对应一个对象）
    public static class Person {
        private String name;
        private Integer age;
        private String gender;

        // 构造函数
        public Person(String name, Integer age, String gender) {
            this.name = name;
            this.age = age;
            this.gender = gender;
        }

        // Getters and Setters（必须有）
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }
}
