package com.ccclubs.bigdata.batch;

import com.mongodb.spark.MongoSpark;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import scala.tools.nsc.util.ClassPath;

/**
 * Created by Administrator on 2017/5/25 0025.
 */
public class MongodbExtract {
    public static void main(String[] args) {
        Logger.getLogger("org").setLevel(Level.ERROR);
        SparkSession sparkSession = SparkSession.builder()
                .master("local")
                .appName("MongodbExtract")
                .config("spark.mongodb.input.uri", "mongodb://tsm_user:tsm2017@114.55.24.97:3717/history.CsHistoryState")
                .config("spark.mongodb.output.uri", "mongodb://tsm_user:tsm2017@114.55.24.97:3717/history.CsHistoryState")
                .getOrCreate();

        JavaSparkContext jsc = new JavaSparkContext(sparkSession.sparkContext());
        Dataset<Row> history_states_df = MongoSpark.load(jsc).toDF();
        history_states_df.createOrReplaceTempView("CsHistoryState");

        Dataset<Row> result_df = sparkSession.sql("select _id,cshsNumber from CsHistoryState where cshsCurrentTime<=1479999799000 and cshsCurrentTime>=1479999699000 and cshsNumber='GB006702'");

        result_df.show();
        jsc.close();

    }
}
