package excel2rdd;

import excel2rdd.row.ExcelRow;
import excel2rdd.row.ExcelRowProcessor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XLSX2RDD {
    private String xlsxFileName;
    private int xlsxStartSheetIndex;
    private int xlsxNumberOfSheets;
    private int xlsxNumberOfColumns;
    private int xlsxRowStart;
    private int xlsxNumberOfRows;

    public XLSX2RDD(String xlsxFileName, int xlsxNumberOfColumns, int xlsxStartSheetIndex, int xlsxNumberOfSheets, int xlsxRowStart, int xlsxNumberOfRows) {
        this.xlsxFileName = xlsxFileName;
        this.xlsxStartSheetIndex = xlsxStartSheetIndex;
        this.xlsxNumberOfSheets = xlsxNumberOfSheets;
        this.xlsxNumberOfColumns = xlsxNumberOfColumns;
        this.xlsxRowStart = xlsxRowStart;
        this.xlsxNumberOfRows = xlsxNumberOfRows;
    }

   private String getCellStringValue(Cell cell) {
        String empty = "";

        if (cell == null) {
            return empty;
        }

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
        }
        return empty;
    }

    private void readXLSXSheet(List<ExcelRow> excelRowList, Sheet sheet, ExcelRowProcessor excelRowProcessor) {
        int rowEnd = 0;

        if (xlsxNumberOfRows <= 0) {
            rowEnd = sheet.getLastRowNum();
        } else {
            rowEnd = xlsxRowStart + xlsxNumberOfRows - 1;
        }

        for (int i = xlsxRowStart; i <= rowEnd; i++) {
            org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
            ExcelRow excelRow = new ExcelRow();

            for (int j = 0; j < xlsxNumberOfColumns; j++) {
                excelRowProcessor.setColumnValue(excelRow, j, getCellStringValue(row.getCell(j)));
            }

            excelRowList.add(excelRow);
        }
    }

    public List<?> readXLSX() throws IOException {
        List<ExcelRow> resultList = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(new File(xlsxFileName));
        Workbook workbook = new XSSFWorkbook(inputStream);
        ExcelRowProcessor excelRowProcessor = new ExcelRowProcessor();

        for (int i = 0; i < xlsxNumberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            readXLSXSheet(resultList, sheet, excelRowProcessor);
        }


        workbook.close();
        inputStream.close();

        return resultList;
    }

    public Dataset<org.apache.spark.sql.Row> convertToRDD(SparkSession spark) {
        try {
            return spark.createDataFrame(readXLSX(), ExcelRow.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
