package excel2rdd.row;

public class ExcelRowProcessor {
    public void setColumnValue(ExcelRow row, int index, String value) {
        if (index == 0) {
            row.setCOL0(value);
        }

        if (index == 1) {
            row.setCOL1(value);
        }

        if (index == 2) {
            row.setCOL2(value);
        }

        if (index == 3) {
            row.setCOL3(value);
        }

        if (index == 4) {
            row.setCOL4(value);
        }

        if (index == 5) {
            row.setCOL5(value);
        }

    }
}
