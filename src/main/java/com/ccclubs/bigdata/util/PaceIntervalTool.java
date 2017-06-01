package com.ccclubs.bigdata.util;

import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/1 0001.
 */
public class PaceIntervalTool {
    private List<Row> stateList = new ArrayList<Row>();

    public PaceIntervalTool(List<Row> stateList){
        this.stateList=stateList;
    }


}
