package excel2rdd.row;

import java.io.Serializable;

public class ExcelRow implements Serializable {
    private String COL0;
    private String COL1;
    private String COL2;
    private String COL3;
    private String COL4;
    private String COL5;

    public ExcelRow() {
    }

    public String getCOL0() {
        return COL0;
    }

    public void setCOL0(String COL4) {
        this.COL0 = COL4;
    }

    public String getCOL1() {
        return COL1;
    }

    public void setCOL1(String COL1) {
        this.COL1 = COL1;
    }

    public String getCOL2() {
        return COL2;
    }

    public void setCOL2(String COL2) {
        this.COL2 = COL2;
    }

    public String getCOL3() {
        return COL3;
    }

    public void setCOL3(String COL3) {
        this.COL3 = COL3;
    }

    public String getCOL4() {
        return COL4;
    }

    public void setCOL4(String COL4) {
        this.COL4 = COL4;
    }

    public String getCOL5() {
        return COL5;
    }

    public void setCOL5(String COL5) {
        this.COL5 = COL5;
    }
}
