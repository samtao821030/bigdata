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
        int changed_battery = 0;
        long changed_obd_miles=0;
        Pace pace = null;
        while(iterator.hasNext()){
            Map.Entry<Long,DataBlock> entry = iterator.next();
            DataBlock dataBlock = entry.getValue();
            BlockStatusUtil.judgeBlockStatus(dataBlock);

            //得到当前数据块的状态
            int current_state_type = dataBlock.getState_type();

            //生成新阶段
            if(state_type!=current_state_type){
                //进行相关状态位的重置
                state_type=current_state_type;
                changed_battery = 0;
                changed_obd_miles=0;

                pace = new Pace();
                //设置车机号
                pace.setCshsNumber(historyStateRecord.getCshsNumber());
                //设置授权号
                pace.setCshsAccess(historyStateRecord.getCshsAccess());
                //设置阶段状态
                pace.setState_type(current_state_type);
                //设置统计年
                pace.setState_year(this.year);
                //设置统计月
                pace.setState_month(this.month);
                //设置阶段开始时间
                pace.setStart_state_mills(dataBlock.getBlock_startmills());

                //设置阶段开始总里程数
                pace.setStart_obd_miles(dataBlock.getStart_obd_miles());

                //累加电量变化
                changed_battery+=dataBlock.getChanged_battery();
                //累加里程数变化
                changed_obd_miles+=dataBlock.getChanged_obd_miles();

                pace.setChanged_battery(changed_battery);
                pace.setChanged_obd_miles(changed_obd_miles);
                pace.setEnd_state_mills(dataBlock.getBlock_startmills()+BlockStatusUtil.time_interval*60*1000);
                pace.setEnd_obd_miles(dataBlock.getEnd_obd_miles());

                paceList.add(pace);


            }
            else{
                if(pace!=null){
                    //累加电量变化
                    changed_battery+=dataBlock.getChanged_battery();
                    //累加里程数变化
                    changed_obd_miles+=dataBlock.getChanged_obd_miles();

                    pace.setChanged_battery(changed_battery);
                    pace.setChanged_obd_miles(changed_obd_miles);
                    pace.setEnd_state_mills(dataBlock.getBlock_startmills()+BlockStatusUtil.time_interval*60*1000);
                    pace.setEnd_obd_miles(dataBlock.getEnd_obd_miles());
                }
            }

        }

        return paceList;
    }


}
