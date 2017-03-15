import excel2rdd.XLS2RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class MainTest {
    public static void main(final String[] args) throws InterruptedException {
        XLS2RDD xls2RDD = new XLS2RDD(System.getProperty("user.dir") + "\\src\\test\\resources\\PET_PRI_SPT_S1_D.xls",
                6,
                0,
                1,
                6,
                7);

        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("Excel2RddApp")
                .config("spark.sql.warehouse.dir", "file:///d://spark//spark-warehouse")
                .getOrCreate();

        Dataset<Row> rdd;

        rdd = xls2RDD.convertToRDD(spark);
        rdd.printSchema();
        rdd.show();
        System.out.println(rdd.count());

        spark.stop();
    }
}
