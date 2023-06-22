package cn.example.seckilldemo.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description: easyExcel Demo
 * @Author:
 * @Date: 2023-06-22
 */
@Data
@TableName("t_demodata")
public class DemoData {

    private  int id;

    @ExcelProperty("字符串标题")
    private String strName;

    @ExcelProperty("日期标题")
    private Date creatTime;

    @ExcelProperty("数字标题")
    private Double doubleData;

    /*
     * 忽略字段
     */
    @ExcelIgnore
    private String ignores;
}
