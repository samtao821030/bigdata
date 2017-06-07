package com.ccclubs.bigdata.util;

import com.ccclubs.bigdata.bean.DataBlock;
import com.ccclubs.bigdata.bean.HistoryStateRecord;
import com.ccclubs.bigdata.bean.Pace;
import org.apache.spark.sql.Row;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Administrator on 2017/6/1 0001.
 */
public class PaceIntervalTool {
    private List<Row> stateList = new ArrayList<Row>();
    private int year;
    private int month;
    private Map<String,DataBlock> paceMap;

    public PaceIntervalTool(List<Row> stateList,int year,int month){
        this.stateList=stateList;
        this.year=year;
        this.month=month;
    }

    public List<Pace> calcPaceData(){
        List<Pace> paceList = new ArrayList<Pace>();

        Map<Long,DataBlock> blockMap = DateTimeUtil.generateBlockMap(year,month);

        HistoryStateRecord historyStateRecord = null;
        for(int i=0;i<stateList.size();i++){
            historyStateRecord = new HistoryStateRecord(stateList.get(i));
            long key_timemills = DateTimeUtil.getTimeMillsFixByInterval(historyStateRecord.getStateCurrentMills());
            if(blockMap.get(key_timemills)!=null){
                DataBlock dataBlock = blockMap.get(key_timemills);
                List<HistoryStateRecord> recordList=dataBlock.getRecordList();
                recordList.add(historyStateRecord);
            }
        }

        Iterator<Map.Entry<Long,DataBlock>> iterator = blockMap.entrySet().iterator();

        int state_type = -1;
        Pace pace = null;
        while(iterator.hasNext()){
            Map.Entry<Long,DataBlock> entry = iterator.next();
            DataBlock dataBlock = entry.getValue();

        }

        return paceList;
    }


}
