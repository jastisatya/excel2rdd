package excel2rdd;

import excel2rdd.settings.ExcelSettings;
import excel2rdd.settings.ExcelWorkSheetSettings;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Excel2RddTest {
    @BeforeAll
    static void initAll() {
    }

    @BeforeEach
    void init() {
    }

    @Test
    @DisplayName("Excel2Rdd: option with excelWorkSheetSettingsList")
    void succeedingTest1() {
        List<ExcelWorkSheetSettings> excelWorkSheetSettingsList = new ArrayList<>();

        List<Integer> columnIndexList = new ArrayList<>();
        columnIndexList.add(0);
        columnIndexList.add(1);
        columnIndexList.add(2);

        excelWorkSheetSettingsList.add(new ExcelWorkSheetSettings(1, 0, 19, columnIndexList));
        excelWorkSheetSettingsList.add(new ExcelWorkSheetSettings(2, 0, 19, columnIndexList));


        ExcelSettings excelSettings = new ExcelSettings(ExcelSettings.XLS_FILE_TYPE,
                System.getProperty("user.dir") + "\\src\\test\\resources\\PET_PRI_SPT_S1_D.xls",
                excelWorkSheetSettingsList);

        Excel2Rdd excel2Rdd = new Excel2Rdd();

        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("Excel2RddApp")
                .config("spark.sql.warehouse.dir", "file:///d://spark//spark-warehouse")
                .getOrCreate();

        Dataset<Row> rdd;

        rdd = excel2Rdd.convertToRDD(spark, excelSettings);

        rdd.printSchema();
        rdd.show();
        System.out.println(rdd.count());

        assertEquals(40, rdd.count());

        spark.stop();
    }

    @Test
    @DisplayName("Excel2Rdd: option when excelWorkSheetSettingsList == null")
    void succeedingTest2() {
        ExcelSettings excelSettings = new ExcelSettings(ExcelSettings.XLS_FILE_TYPE,
                System.getProperty("user.dir") + "\\src\\test\\resources\\PET_PRI_SPT_S1_D.xls",
                null);

        Excel2Rdd excel2Rdd = new Excel2Rdd();

        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("Excel2RddApp")
                .config("spark.sql.warehouse.dir", "file:///d://spark//spark-warehouse")
                .getOrCreate();

        Dataset<Row> rdd;

        rdd = excel2Rdd.convertToRDD(spark, excelSettings);

        rdd.printSchema();
        rdd.show();
        System.out.println(rdd.count());

        assertEquals(19, rdd.count());

        spark.stop();
    }

    @Test
    @DisplayName("Excel2Rdd: option when excelSettingsList == null")
    void succeedingTest3() {
        Excel2Rdd excel2Rdd = new Excel2Rdd();

        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("Excel2RddApp")
                .config("spark.sql.warehouse.dir", "file:///d://spark//spark-warehouse")
                .getOrCreate();

        Dataset<Row> rdd;

        rdd = excel2Rdd.convertToRDD(spark, null);

        assertEquals(null, rdd);

        spark.stop();
    }

    @Test
    void failingTest() {
        //fail("a failing test");
    }

    @Test
    @Disabled("for demonstration purposes")
    void skippedTest() {
        // not executed
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }
}