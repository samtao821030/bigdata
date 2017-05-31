package com.ccclubs.bigdata.batch;


import org.apache.spark.sql.SparkSession;

/**
 * Created by Administrator on 2017/5/31 0031.
 */
public class HiveTest {
    public static void main(String[] args) {
        String warehouseLocation = "hdfs://114.55.6.246:9000/spark-warehouse";
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark Hive Example")
                .config("spark.sql.warehouse.dir", warehouseLocation)
                .enableHiveSupport()
                .getOrCreate();
        spark.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)");

        spark.stop();
    }



}
