package cn.example.seckilldemo.excels;

import cn.example.seckilldemo.entity.DemoData;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @Author:
 * @Date: 2023-06-22
 */
public class TestExcel {

    public static String path = "D:\\upload2\\";

    @Test
    public void simpleFill() {
        // 模板注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
        // 定义模板文件路径
        // 注意 模板的 变量 要与实体对应
        String templateFileName = path + "1678149812045" + ".xlsx";

        // 方案1 根据对象填充
        String fileName = path + System.currentTimeMillis() + ".xlsx";
        // 这里 会填充到第一个sheet， 然后文件流会自动关闭
        DemoData fillData = new DemoData();
        fillData.setStrName("张三");
        fillData.setDoubleData(5.2);
        fillData.setCreatTime(new Date(System.currentTimeMillis()));
        EasyExcel.write(fileName).withTemplate(templateFileName).sheet().doFill(fillData);
    }

}

