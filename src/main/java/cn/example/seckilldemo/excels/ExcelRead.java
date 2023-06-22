//package cn.example.seckilldemo.excels;
//
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//
///**
// * @Description: excel 读
// * @Author:
// * @Date: 2023-06-22
// */
//public class ExcelRead {
//
//    public static String path = "D:\\upload2\\";
//
//    public static void main(String[] args) throws Exception {
//        testFormula();
//    }
//
//    // 公式
//    public static void testFormula() throws Exception {
//        // 获取文件流
//        FileInputStream fileInputStream = new FileInputStream(path + "公式.xlsx");
//
//        // 根据文件 创建工作簿
//        Workbook workbook = new XSSFWorkbook(fileInputStream);
//
//        // 得到 sheet 表
//        Sheet sheet = workbook.getSheetAt(0);
//
//
//        // 得到 行
//        Row row = sheet.getRow(5);  // 对照公式表
//
//        // 得到 列
//        Cell cell = row.getCell(0);
//
//        // 获取计算公式
//        FormulaEvaluator formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
//
//        // 输出单元格
//        int cellType = cell.getCellType();
//        switch (cellType){
//            case Cell.CELL_TYPE_FORMULA: // 公式
//                String cellFormula = cell.getCellFormula();
//                System.out.println(cellFormula);
//
//                // 计算
//                CellValue evaluate = formulaEvaluator.evaluate(cell);
//                String cellValue = evaluate.formatAsString();
//                System.out.println(cellValue);
//                break;
//        }
//
//        fileInputStream.close();
//    }
//}
