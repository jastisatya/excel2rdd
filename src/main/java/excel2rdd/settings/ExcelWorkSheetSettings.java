package excel2rdd.settings;

import java.util.List;

public class ExcelWorkSheetSettings {
    private int workSheetIndex;
    private int startRow;
    private int endRow;
    private List<Integer> columnIndexList;

    public ExcelWorkSheetSettings() {
    }

    public ExcelWorkSheetSettings(int workSheetIndex, int startRow, int endRow, List<Integer> columnIndexList) {
        this.workSheetIndex = workSheetIndex;
        this.startRow = startRow;
        this.endRow = endRow;
        this.columnIndexList = columnIndexList;
    }

    public int getWorkSheetIndex() {
        return workSheetIndex;
    }

    public void setWorkSheetIndex(int workSheetIndex) {
        this.workSheetIndex = workSheetIndex;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public List<Integer> getColumnIndexList() {
        return columnIndexList;
    }

    public void setColumnIndexList(List<Integer> columNIndexList) {
        this.columnIndexList = columNIndexList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExcelWorkSheetSettings that = (ExcelWorkSheetSettings) o;

        if (workSheetIndex != that.workSheetIndex) return false;
        if (startRow != that.startRow) return false;
        if (endRow != that.endRow) return false;
        return columnIndexList != null ? columnIndexList.equals(that.columnIndexList) : that.columnIndexList == null;
    }

    @Override
    public int hashCode() {
        int result = workSheetIndex;
        result = 31 * result + startRow;
        result = 31 * result + endRow;
        result = 31 * result + (columnIndexList != null ? columnIndexList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExcelWorkSheetSettings{" +
                "workSheetIndex=" + workSheetIndex +
                ", startRow=" + startRow +
                ", endRow=" + endRow +
                ", columNIndexList=" + columnIndexList +
                '}';
    }
}
