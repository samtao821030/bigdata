package com.ccclubs.bigdata.extend;

import org.apache.spark.Partitioner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/1 0001.
 */
public class ChsNumberPartitioner extends Partitioner {

    private Map<String,Integer> chsMap = new HashMap<String,Integer>();


    public ChsNumberPartitioner(Map<String,Integer> chsMap){
        this.chsMap=chsMap;
    }

    @Override
    public int numPartitions() {
        int size = 0;
        if(chsMap!=null && chsMap.size()>0){
            size=chsMap.size();
        }
        return size;
    }

    @Override
    public int getPartition(Object key) {
        int partition=0;
        String key_str = key.toString();
        if(chsMap.get(key_str)!=null){
            partition=chsMap.get(key_str);
        }
        return partition;
    }
}
