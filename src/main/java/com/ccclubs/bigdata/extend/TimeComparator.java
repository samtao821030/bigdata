package com.ccclubs.bigdata.extend;

import org.apache.spark.sql.Row;
import org.spark_project.guava.primitives.Ints;
import org.spark_project.guava.primitives.Longs;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/6/1 0001.
 */
public class TimeComparator implements Comparator<Row> {
    @Override
    public int compare(Row row1, Row row2) {
        if(row1.getAs("cshsCurrentTime")!=null && row2.getAs("cshsCurrentTime")!=null){
            long cshsCurrentTime1=0;
            long cshsCurrentTime2=0;
            try{
                cshsCurrentTime1=Long.parseLong(row1.getAs("cshsCurrentTime"));
                cshsCurrentTime2=Long.parseLong(row2.getAs("cshsCurrentTime"));
            }
            catch (Exception ex){

            }
            return Longs.compare(cshsCurrentTime1,cshsCurrentTime2);
        }
        else {
            return 0;
        }
    }
}
