package com.ccclubs.bigdata.batch;

import com.ccclubs.bigdata.bean.Pace;
import com.ccclubs.bigdata.extend.ChsNumberPartitioner;
import com.ccclubs.bigdata.extend.TimeComparator;
import com.ccclubs.bigdata.util.DBHelper;
import com.ccclubs.bigdata.util.DateTimeUtil;
import com.ccclubs.bigdata.util.PaceIntervalTool;
import com.google.common.collect.Ordering;
import com.mongodb.spark.MongoSpark;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.sql.SQLException;
import java.util.*;
import java.util.Collections;

/**
 * Created by Administrator on 2017/5/31 0031.
 */
public class IndexCalcTest {
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
        //history_states_df.cache();
        history_states_df.createOrReplaceTempView("CsHistoryState");
        DBHelper dbHelper = new DBHelper();
        long start_timemills= DateTimeUtil.Date2UnixFormat("2017-05-01 00:00:00",DateTimeUtil.format1);
        long end_timemills = DateTimeUtil.Date2UnixFormat("2017-05-31 23:59:59",DateTimeUtil.format1);

        Broadcast<Integer> year_broadcast = jsc.broadcast(DateTimeUtil.getYear(start_timemills));
        Broadcast<Integer> month_broadcast = jsc.broadcast(DateTimeUtil.getMonth(start_timemills));

        Map<String,Integer> chsMap = new HashMap<>();
        chsMap.put("T6710239",0);
        chsMap.put("T6710274",1);
        chsMap.put("T6710211",2);
        chsMap.put("CQDA2621",3);
        String sql_str=" select * from CsHistoryState a where (a.cshsNumber='T6710239' or a.cshsNumber='T6710274' or a.cshsNumber='T6710211' or a.cshsNumber='CQDA2621' ) "
                +" and a.cshsCurrentTime>=%d and a.cshsCurrentTime<=%d ";
        sql_str=String.format(sql_str,start_timemills,end_timemills);
        Dataset<Row> result_df=sparkSession.sql(sql_str);
        result_df.toJavaRDD().mapToPair(new PairFunction<Row, String, Row>() {
            @Override
            public Tuple2<String, Row> call(Row row) throws Exception {
                String cshsNumber = row.getAs("cshsNumber");
                return new Tuple2<String,Row>(cshsNumber,row);
            }
        })
        .partitionBy(new ChsNumberPartitioner(chsMap))
        .map(new Function<Tuple2<String,Row>, Row>() {
            @Override
            public Row call(Tuple2<String, Row> stringRowTuple2) throws Exception {
                return stringRowTuple2._2();
            }
        })
        .mapPartitions(new FlatMapFunction<Iterator<Row>, Row>() {

            public Iterator<Row> call(Iterator<Row> rowIterator) throws Exception {
                //logger.info("出记录啦!");
                TimeComparator timeComparator = new TimeComparator();
                List<Row> rowList = new ArrayList<Row>();
                while(rowIterator.hasNext()){
                    rowList.add(rowIterator.next());
                }
                Ordering<Row> rowOrdering = Ordering.from(timeComparator);
                Collections.sort(rowList,rowOrdering);
//                for(Row row:rowList){
//                    logger.info("车机号:"+row.getAs("cshsNumber")+" 上传时间:"+row.getAs("cshsCurrentTime")+" 电量百分比:"+row.getAs("cshsEvBattery")+" 车速:"+row.getAs("cshsSpeed")+" 总里程:"+row.getAs("cshsObdMile"));
//                }
                PaceIntervalTool paceIntervalTool = new PaceIntervalTool(rowList,year_broadcast.getValue(),month_broadcast.getValue());
                List<Pace> paceList = paceIntervalTool.calcPaceData();
                logger.info("");
                return rowIterator;
            }


        })
         .take(1)
        ;

    }
}
