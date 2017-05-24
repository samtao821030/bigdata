package com.ccclubs.bigdata.batch;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/24 0024.
 */
public class MysqlExtractCluster {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName("MysqlExtractLocal");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);

        Map<String,String> options = new HashMap<String,String>();
        options.put("url", "jdbc:mysql://10.132.27.108:3306/ccclubs_data_center");
        options.put("user", "tsm_user");
        options.put("password", "fKWVfxCMEhGmRDyoozOw");
        options.put("dbtable", "cs_machine");

//        Encoder<Cs_machine> cs_machine_encoder = Encoders.bean(Cs_machine.class);
//        Dataset<Cs_machine> cs_machine_ds = sqlContext.read().format("jdbc").options(options).load().as(cs_machine_encoder);
//        //cs_machine_ds.printSchema();
//        cs_machine_ds.toJavaRDD().foreach(new VoidFunction<Cs_machine>() {
//            @Override
//            public void call(Cs_machine cs_machine) throws Exception {
//                System.out.println("编号:"+cs_machine.getCsm_id()+" 授权系统:"+cs_machine.getCsm_access());
//            }
//        });
        Dataset<Row> cs_machine_ds = sqlContext.read().format("jdbc").options(options).load();

//        cs_machine_ds.toJavaRDD().foreach(new VoidFunction<Row>() {
//            @Override
//            public void call(Row row) throws Exception {
//                System.out.println(
//                                 "编号:"+row.getAs("csm_id").toString()+" "
//                                +"授权系统:"+row.getAs("csm_access").toString()+" "
//                                +"终端类型:"+row.getAs("csm_te_type").toString()+" "
////                                +"协议类型:"+row.getAs("csm_protocol").toString()
//                );
//            }
//        });
        cs_machine_ds.registerTempTable("cs_machine");
        Dataset<Row> filter_ds = sqlContext.sql(" select a.csm_protocol,count(a.csm_id) from cs_machine a " +
                " where a.csm_protocol is not null "+
                " group by a.csm_protocol ");
        filter_ds.show();
        sc.close();
    }
}
