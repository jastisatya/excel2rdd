package excel2rdd;

import excel2rdd.row.ExcelRow;
import excel2rdd.row.ExcelRowProcessor;
import excel2rdd.settings.ExcelSettings;
import excel2rdd.settings.ExcelWorkSheetSettings;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Excel2Rdd {

    public Excel2Rdd() {
    }

    private String getCellStringValue(Cell cell) {
        String empty = "";

        if (cell == null) {
            return empty;
        }

        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue()).replace(".0", "");
        }

        return empty;
    }

    private void readWorkSheet(List<ExcelRow> excelRowList, Sheet sheet, ExcelWorkSheetSettings excelWorkSheetSettings) {
        int startRow = 0;
        int endRow = 0;

        startRow = excelWorkSheetSettings.getStartRow();

        if (excelWorkSheetSettings.getEndRow() <= 0 || excelWorkSheetSettings.getEndRow() <= excelWorkSheetSettings.getStartRow()) {
            endRow = sheet.getLastRowNum();
        } else {
            endRow = excelWorkSheetSettings.getEndRow();
        }

        ExcelRowProcessor excelRowProcessor = new ExcelRowProcessor();

        for (int i = startRow; i <= endRow; i++) {
            org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
            if (row != null) {
                ExcelRow excelRow = new ExcelRow();

                for (Integer x : excelWorkSheetSettings.getColumnIndexList()) {
                    excelRowProcessor.setColumnValue(excelRow, x, getCellStringValue(row.getCell(x)));
                }

                excelRowList.add(excelRow);
            }
        }
    }

    private void readWorkSheet(List<ExcelRow> excelRowList, Sheet sheet) throws ClassNotFoundException {
        int startRow = 0;
        int endRow = sheet.getLastRowNum();

        ExcelRowProcessor excelRowProcessor = new ExcelRowProcessor();

        for (int i = startRow; i <= endRow; i++) {
            org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);

            if (row != null) {
                ExcelRow excelRow = new ExcelRow();

                for (int x = 0; x < Class.forName("excel2rdd.row.ExcelRow").getDeclaredFields().length; x++) {
                    excelRowProcessor.setColumnValue(excelRow, x, getCellStringValue(row.getCell(x)));
                }

                excelRowList.add(excelRow);
            }
        }
    }

    private List<?> readExcel(ExcelSettings excelSettings) throws IOException, ClassNotFoundException {
        List<ExcelRow> resultList = new ArrayList<>();

        FileInputStream inputStream = new FileInputStream(new File(excelSettings.getFileName()));
        Workbook workbook = null;

        if (excelSettings.getFileType() == ExcelSettings.XLS_FILE_TYPE) {
            workbook = new HSSFWorkbook(inputStream);
        }

        if (excelSettings.getFileType() == ExcelSettings.XLSX_FILE_TYPE) {
            workbook = new XSSFWorkbook(inputStream);
        }

        if (workbook == null) {
            return null;
        }

        if (excelSettings.getExcelWorkSheetSettingsList() != null) {
            for (ExcelWorkSheetSettings x : excelSettings.getExcelWorkSheetSettingsList()) {
                Sheet sheet = workbook.getSheetAt(x.getWorkSheetIndex());
                readWorkSheet(resultList, sheet, x);
            }
        } else {
            Sheet sheet = workbook.getSheetAt(0);
            readWorkSheet(resultList, sheet);
        }

        workbook.close();
        inputStream.close();

        return resultList;
    }

    public Dataset<Row> convertToRDD(SparkSession spark, ExcelSettings excelSettings) {
        if (excelSettings == null) {
            return null;
        }

        try {
            return spark.createDataFrame(readExcel(excelSettings), ExcelRow.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {

    }
}
