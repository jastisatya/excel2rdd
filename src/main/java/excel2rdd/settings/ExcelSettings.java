package excel2rdd.settings;

import java.util.List;

public class ExcelSettings {
    public final static String XLS_FILE_TYPE = "XLS";
    public final static String XLSX_FILE_TYPE = "XLSX";
    private String fileType;
    private String fileName;
    private List<ExcelWorkSheetSettings> excelWorkSheetSettingsList;

    public ExcelSettings() {
    }

    public ExcelSettings(String fileType, String fileName, List<ExcelWorkSheetSettings> excelWorkSheetSettingsList) {
        this.fileType = fileType;
        this.fileName = fileName;
        this.excelWorkSheetSettingsList = excelWorkSheetSettingsList;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<ExcelWorkSheetSettings> getExcelWorkSheetSettingsList() {
        return excelWorkSheetSettingsList;
    }

    public void setExcelWorkSheetSettingsList(List<ExcelWorkSheetSettings> excelWorkSheetSettingsList) {
        this.excelWorkSheetSettingsList = excelWorkSheetSettingsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExcelSettings that = (ExcelSettings) o;

        if (fileType != null ? !fileType.equals(that.fileType) : that.fileType != null) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        return excelWorkSheetSettingsList != null ? excelWorkSheetSettingsList.equals(that.excelWorkSheetSettingsList) : that.excelWorkSheetSettingsList == null;
    }

    @Override
    public int hashCode() {
        int result = fileType != null ? fileType.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (excelWorkSheetSettingsList != null ? excelWorkSheetSettingsList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExcelSettings{" +
                "fileType='" + fileType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", excelWorkSheetSettingsList=" + excelWorkSheetSettingsList +
                '}';
    }
}
