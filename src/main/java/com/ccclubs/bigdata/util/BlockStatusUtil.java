package com.ccclubs.bigdata.util;

import com.ccclubs.bigdata.bean.DataBlock;
import com.ccclubs.bigdata.bean.HistoryStateRecord;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6 0006.
 */
public class BlockStatusUtil {
    //数据块时间间隔
    public static final int time_interval=12;

    public static int judgeBlockStatus(DataBlock dataBlock){
        int state_type = -1;
        List<HistoryStateRecord> recordList = dataBlock.getRecordList();
        int changed_battery = 0;
        long changed_obd_miles=0;
        //数据块起始行驶总里程
        long start_obd_miles= 0;
        //数据块结束行驶里程数
        long end_obd_miles=0;

        float avg_speed=0;
        int total_speed=0;
        //若该数据块内不存在数据上传记录,则视为该数据块内的状态为闲置状态
        if(recordList.size()==0){
            //设置开始行驶里程数
            dataBlock.setStart_obd_miles(start_obd_miles);
            //设置结束行驶里程数
            dataBlock.setEnd_obd_miles(end_obd_miles);
            state_type=3;
        }
        else{
            for(int i=0;i<recordList.size();i++){
                if(i==0){
                    //开始行驶总里程数
                    start_obd_miles=recordList.get(i).getCshsObdMile();
                }
                if(i==recordList.size()-1){
                    //结束行驶总里程数
                    end_obd_miles=recordList.get(i).getCshsObdMile();
                }
                if(i>0){
                    HistoryStateRecord currentRecord = recordList.get(i);
                    HistoryStateRecord previousRecord = recordList.get(i-1);
                    //累计电量计算
                    changed_battery+=currentRecord.getCshsEvBattery()-previousRecord.getCshsEvBattery();
                    //累计里程数计算
                    changed_obd_miles+=currentRecord.getCshsObdMile()-previousRecord.getCshsObdMile();
                    //获得累计速度
                    total_speed+=currentRecord.getCshsSpeed();
                }
            }
            //获取平均速度
            avg_speed = (float)(total_speed/recordList.size());
            //数据块的属性设置
            //设置开始行驶里程数
            dataBlock.setStart_obd_miles(start_obd_miles);
            //设置结束行驶里程数
            dataBlock.setEnd_obd_miles(end_obd_miles);
            //设置累计电量变化数
            dataBlock.setChanged_battery(changed_battery);
            //设置累计里程数变化数
            dataBlock.setChanged_obd_miles(changed_obd_miles);
            //设置平均速度
            dataBlock.setAvg_speed(avg_speed);

            //------------------------------------------华丽的分割线----------------------------------------------------
            //------------------------------------------进行数据块状态判断----------------------------------------------
            if(changed_battery>0 && avg_speed==0){
                state_type=1;

            }
            else if(changed_obd_miles>0||avg_speed>0){
                state_type=2;
            }
            else{
                state_type=3;
            }

        }
        dataBlock.setState_type(state_type);
        return state_type;
    }
}
