package excel2rdd;

import excel2rdd.row.ExcelRow;
import excel2rdd.row.ExcelRowProcessor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XLS2RDD {
    private String xlsFileName;
    private int xlsStartSheetIndex;
    private int xlsNumberOfSheets;
    private int xlsNumberOfColumns;
    private int xlsRowStart;
    private int xlsNumberOfRows;

    public XLS2RDD(String xlsFileName, int xlsNumberOfColumns, int xlsStartSheetIndex, int xlsNumberOfSheets, int xlsRowStart, int xlsNumberOfRows) {
        this.xlsFileName = xlsFileName;
        this.xlsStartSheetIndex = xlsStartSheetIndex;
        this.xlsNumberOfSheets = xlsNumberOfSheets;
        this.xlsNumberOfColumns = xlsNumberOfColumns;
        this.xlsRowStart = xlsRowStart;
        this.xlsNumberOfRows = xlsNumberOfRows;
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

    private void readXLSSheet(List<ExcelRow> excelRowList, Sheet sheet, ExcelRowProcessor excelRowProcessor) {
        int rowEnd = 0;

        if (xlsNumberOfRows <= 0) {
            rowEnd = sheet.getLastRowNum();
        } else {
            rowEnd = xlsRowStart + xlsNumberOfRows - 1;
        }

        for (int i = xlsRowStart; i <= rowEnd; i++) {
            org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
            ExcelRow excelRow = new ExcelRow();

            for (int j = 0; j < xlsNumberOfColumns; j++) {
                excelRowProcessor.setColumnValue(excelRow, j, getCellStringValue(row.getCell(j)));
            }

            excelRowList.add(excelRow);
        }
    }

    public List<?> readXLS() throws IOException {
        List<ExcelRow> resultList = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(new File(xlsFileName));
        Workbook workbook = new HSSFWorkbook(inputStream);
        ExcelRowProcessor excelRowProcessor = new ExcelRowProcessor();

        for (int i = 0; i < xlsNumberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            readXLSSheet(resultList, sheet, excelRowProcessor);
        }


        workbook.close();
        inputStream.close();

        return resultList;
    }

    public Dataset<org.apache.spark.sql.Row> convertToRDD(SparkSession spark) {
        try {
            return spark.createDataFrame(readXLS(), ExcelRow.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
