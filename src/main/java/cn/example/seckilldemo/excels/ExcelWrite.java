//package cn.example.seckilldemo.excels;
//
//import cn.hutool.core.date.DateTime;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.lang.annotation.Target;
//import java.util.Date;
//
///**
// * @Description: excel 创建
// * @Author:
// * @Date: 2023-06-22
// */
//public class ExcelWrite {
//    public static String path = "D:\\upload2\\";
//
//    public static void main(String[] args) throws Exception {
//        testWrite07BigDataS();
//    }
//
//    public static void testWrite07BigDataS() throws Exception {
//        long begin = System.currentTimeMillis();
//        Workbook workbook = new SXSSFWorkbook(); // 创建工作簿
//
//        Sheet sheet = workbook.createSheet();   // 创建 sheet
//        for (int rowNum = 0; rowNum < 100000; rowNum++) {    // 03版本的，最多 65536 ，超过会报错
//            Row row = sheet.createRow(rowNum);  // 创建 行
//            for (int cellNum = 0; cellNum < 10; cellNum++) {
//                Cell cell = row.createCell(cellNum); // 创建列
//                cell.setCellValue(cellNum);
//            }
//        }
//        FileOutputStream fileOutputStream = new FileOutputStream(path + "testWrite07BigDataS.xlsx");
//        workbook.write(fileOutputStream);
//        fileOutputStream.close();
//        // SXSSFWorkbook 会生成临时文件，此处是清楚 临时文件
//        ((SXSSFWorkbook) workbook).dispose();
//        long end = System.currentTimeMillis();
//        System.out.println("耗时：" + (double) (end - begin) / 1000);
//
//    }
//
//}
////
////    public static void testWrite07() {
////        // 1. 创建 07版本的 excel
////        Workbook workbook = new XSSFWorkbook();
////
////        // 2. 创建一个工作表
////        Sheet sheet = workbook.createSheet("测试sheet");
////
////        // 3. 创建 一行
////        Row row1 = sheet.createRow(0);   // 第一行
////
////        // 4. 创建一个单元格（1，1）
////        Cell cell11 = row1.createCell(0); // 单元格 第一行 第一个格子
////        // 5. 给 格子填写数据
////        cell11.setCellValue("今日血压");
////
////        // 4. 创建一个单元格（1，2）
////        Cell cell12 = row1.createCell(1); // 单元格 第一行 第二个格子
////        // 5. 给 格子填写数据
////        cell12.setCellValue(111);
////
////        // 3. 创建 二行
////        Row row2 = sheet.createRow(1);   // 第一行
////
////        // 4. 创建一个单元格（1，1）
////        Cell cell21 = row2.createCell(0); // 单元格 第一行 第一个格子
////        // 5. 给 格子填写数据
////        cell21.setCellValue("今日空气质量");
////
////        // 4. 创建一个单元格（1，2）
////        Cell cell22 = row2.createCell(1); // 单元格 第一行 第二个格子
////        // 5. 给 格子填写数据
////        String time = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
////        cell22.setCellValue(time);
////
////
////        // 输出 IO流
////        try {
////            FileOutputStream fileOutputStream = new FileOutputStream(path + "\\07版本测试表1.xlsx");
////            // 输出
////            workbook.write(fileOutputStream);
////            // 关闭流
////            fileOutputStream.close();
////            System.out.println("输出成功");
////        } catch (Exception e) {
////            throw new RuntimeException(e);
////        }
////
////    }
////}
