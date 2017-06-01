package com.ccclubs.bigdata.batch;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ccclubs.bigdata.util.DBHelper;
import com.mongodb.spark.MongoSpark;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.sql.SQLException;

/**
 * Created by Administrator on 2017/5/31 0031.
 */
public class IndexCalc {
    private static Logger logger = Logger.getLogger(IndexCalc.class);
    private static long offset = 0;
    private static long rows = 10;

    public static void main(String[] args) throws SQLException {
        Logger.getLogger("org").setLevel(Level.ERROR);
        SparkSession sparkSession = SparkSession.builder()
                .master("local")
                .appName("MongodbExtract")
                .config("spark.mongodb.input.uri", "mongodb://tsm_user:tsm2017@114.55.24.97:3717/history.CsHistoryState")
                .config("spark.mongodb.output.uri", "mongodb://tsm_user:tsm2017@114.55.24.97:3717/history.CsHistoryState")
                .getOrCreate();
        JavaSparkContext jsc = new JavaSparkContext(sparkSession.sparkContext());
        Dataset<Row> history_states_df = MongoSpark.load(jsc).toDF();
        history_states_df.cache();
        history_states_df.createOrReplaceTempView("CsHistoryState");

        DBHelper dbHelper = new DBHelper();
        while(true){
            //首先先检索
            JSONArray array = dbHelper.queryRecords(" select * from cs_machine_mapping where length(cs_number)>0 and cs_access=1 order by cs_number asc ", offset, rows);

            if(array.size()>0) {
                //得到计算首条车机号
                JSONObject startRow = array.getJSONObject(0);
                String start_cs_number = startRow.getString("cs_number");
                //得到计算末尾车机号
                JSONObject endRow = array.getJSONObject(array.size()-1);
                String end_cs_number = endRow.getString("cs_number");

                offset += rows;
                if(offset>20){
                    break;
                }
            }
            else {
                break;
            }
        }

    }
}
