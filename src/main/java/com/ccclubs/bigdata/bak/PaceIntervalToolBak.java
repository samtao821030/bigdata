package com.ccclubs.bigdata.bak;

import com.ccclubs.bigdata.bean.HistoryStateRecord;
import com.ccclubs.bigdata.bean.Pace;
import com.ccclubs.bigdata.util.DateTimeUtil;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/1 0001.
 */
public class PaceIntervalToolBak {
    private List<Row> stateList = new ArrayList<Row>();

    public PaceIntervalToolBak(List<Row> stateList){
        this.stateList=stateList;
    }

    public List<Pace> calcPaceData(){
        List<Pace> paceList = new ArrayList<Pace>();
        //阶段状态(因充电状态时间大于行驶时间,设初始状态)
        int state_type = 1;
        //上一条记录
        HistoryStateRecord previousRecord = new HistoryStateRecord(stateList.get(0));
        //当前记录
        HistoryStateRecord currentRecord = new HistoryStateRecord(stateList.get(0));
        //状态统计起始时间
        long start_state_mills = previousRecord.getStateCurrentMills();
        //状态统计结束时间
        long end_state_mills = previousRecord.getStateCurrentMills();
        //月内统计行号
        int month_rownum = 1;
        //状态持续时间(单位:毫秒)
        long state_lastmills=end_state_mills-start_state_mills;
        //电量百分比变化量
        int changed_battery=0;
        //总里程变化
        long changed_obd=0;
        //阶段开始总里程数
        long start_obd_miles = previousRecord.getCshsObdMile();
        //阶段结束总里程数
        long end_obd_miles = currentRecord.getCshsObdMile();

        List<HistoryStateRecord> tempRecordList=new ArrayList<HistoryStateRecord>();//临时存储阶段序列
        for(int i=0;i<stateList.size();i++){
            if(i>0){
                currentRecord=new HistoryStateRecord(stateList.get(i));
                previousRecord=new HistoryStateRecord(stateList.get(i-1));
            }
            Pace pace = null;
            //开始计算相关指标的累计变化
            int near_changed_battery=currentRecord.getCshsEvBattery()-previousRecord.getCshsEvBattery();//相邻记录电量变化
            changed_battery+=near_changed_battery;//阶段电量变化累计值
            long near_changed_obd=currentRecord.getCshsObdMile()-previousRecord.getCshsObdMile();//里程变化
            changed_obd+=near_changed_obd;//阶段里程变化累计值
            state_lastmills+=currentRecord.getStateCurrentMills()-previousRecord.getStateCurrentMills();//阶段累计持续时间

            //如果当前阶段为[充电阶段]
            if(state_type==1){
                tempRecordList.add(currentRecord);
                //如果检测到有速度变化或者已经到达记录最后一条,则开始生成充电阶段记录
                if(currentRecord.getCshsSpeed()>0
                        ||i==stateList.size()-1
                        ){
                    end_state_mills=previousRecord.getStateCurrentMills();//设置状态统计结束时间
                    end_obd_miles=previousRecord.getCshsObdMile();//设置阶段结束总里程数

                    pace = new Pace();
                    //设置阶段状态种类
                    pace.setState_type(state_type);
                    //设置车机号
                    pace.setCshsNumber(previousRecord.getCshsNumber());
                    //设置授权系统
                    pace.setCshsAccess(previousRecord.getCshsAccess());
                    //设置阶段统计年份
                    pace.setState_year(DateTimeUtil.getYear(start_state_mills));
                    //设置阶段统计月份
                    pace.setState_month(DateTimeUtil.getMonth(start_state_mills));
                    //设置月内统计行号
                    pace.setMonth_rownum(month_rownum);
                    //设置阶段统计起始时间
                    pace.setStart_state_mills(start_state_mills);
                    //设置阶段统计结束时间
                    pace.setEnd_state_mills(end_state_mills);
                    //设置阶段持续时间
                    pace.setState_lastmills(state_lastmills);
                    //设置电量变化百分比
                    pace.setChanged_battery(changed_battery);
                    //设置阶段开始总里程数
                    pace.setStart_obd_miles(start_obd_miles);
                    //设置阶段结束总里程数
                    pace.setEnd_obd_miles(end_obd_miles);
                    //设置阶段行驶总里程数
                    pace.setChanged_obd_miles(end_obd_miles-start_obd_miles);

                   //如果充电阶段时间持续5分钟以上,才将充电阶段纳入序列
                    if(state_lastmills>=300000) {
                       //将该阶段加入阶段统计序列
                       paceList.add(pace);
                       //---------------------------此处华丽的分割线-----------------------------
                       //---------------------------开始重置一些重要状态-------------------------
                       start_state_mills = currentRecord.getStateCurrentMills();//重置阶段统计起始时间
                       start_obd_miles = currentRecord.getCshsObdMile();//重置开始阶段总里程数
                       state_lastmills = 0;//重置阶段持续时间
                       changed_battery = 0;//重置电量变化百分比

                   }
                    state_type = 2;//重置阶段状态为行驶

                }
                else if(tempRecordList.size()>=40){//充电阶段闲置性测试
                    HistoryStateRecord last_record= tempRecordList.get(tempRecordList.size()-1);
                    int middle_index = (int)(tempRecordList.size()/2);
                    HistoryStateRecord middle_record = tempRecordList.get(middle_index);
                    //若满足闲置性条件则生成充电记录
                    if(last_record.getCshsEvBattery()-middle_record.getCshsEvBattery()<1){
                        end_state_mills=previousRecord.getStateCurrentMills();//设置状态统计结束时间
                        end_obd_miles=previousRecord.getCshsObdMile();//设置阶段结束总里程数

                        pace = new Pace();
                        //设置阶段状态种类
                        pace.setState_type(state_type);
                        //设置车机号
                        pace.setCshsNumber(previousRecord.getCshsNumber());
                        //设置授权系统
                        pace.setCshsAccess(previousRecord.getCshsAccess());
                        //设置阶段统计年份
                        pace.setState_year(DateTimeUtil.getYear(start_state_mills));
                        //设置阶段统计月份
                        pace.setState_month(DateTimeUtil.getMonth(start_state_mills));
                        //设置月内统计行号
                        pace.setMonth_rownum(month_rownum);
                        //设置阶段统计起始时间
                        pace.setStart_state_mills(start_state_mills);
                        //设置阶段统计结束时间
                        pace.setEnd_state_mills(end_state_mills);
                        //设置阶段持续时间
                        pace.setState_lastmills(state_lastmills);
                        //设置电量变化百分比
                        pace.setChanged_battery(changed_battery);
                        //设置阶段开始总里程数
                        pace.setStart_obd_miles(start_obd_miles);
                        //设置阶段结束总里程数
                        pace.setEnd_obd_miles(end_obd_miles);
                        //设置阶段行驶总里程数
                        pace.setChanged_obd_miles(end_obd_miles-start_obd_miles);
                        //如果充电阶段时间持续5分钟以上,才将充电阶段纳入序列

                        //将该阶段加入阶段统计序列
                        paceList.add(pace);
                        //---------------------------此处华丽的分割线-----------------------------
                        //---------------------------开始重置一些重要状态-------------------------
                        start_state_mills = currentRecord.getStateCurrentMills();//重置阶段统计起始时间
                        start_obd_miles = currentRecord.getCshsObdMile();//重置开始阶段总里程数
                        state_lastmills = 0;//重置阶段持续时间
                        changed_battery = 0;//重置电量变化百分比

                        tempRecordList.clear();//清除缓存序列
                        state_type = 3;//重置阶段状态为闲置
                    }
                }
                else{
                    continue;
                }

            }
            //如果当前阶段为[行驶阶段]
            else if(state_type==2){
                tempRecordList.add(currentRecord);
                int cshsSpeed = currentRecord.getCshsSpeed();
                //如果检测到有电量上升变化或者已经到达记录最后一条,则开始生成充电阶段记录
                if((near_changed_battery>0&&cshsSpeed==0)||i==stateList.size()-1){
                    end_state_mills=previousRecord.getStateCurrentMills();//设置状态统计结束时间
                    end_obd_miles=previousRecord.getCshsObdMile();//设置阶段结束总里程数

                    pace = new Pace();
                    //设置阶段状态种类
                    pace.setState_type(state_type);
                    //设置车机号
                    pace.setCshsNumber(previousRecord.getCshsNumber());
                    //设置授权系统
                    pace.setCshsAccess(previousRecord.getCshsAccess());
                    //设置阶段统计年份
                    pace.setState_year(DateTimeUtil.getYear(start_state_mills));
                    //设置阶段统计月份
                    pace.setState_month(DateTimeUtil.getMonth(start_state_mills));
                    //设置月内统计行号
                    pace.setMonth_rownum(month_rownum);
                    //设置阶段统计起始时间
                    pace.setStart_state_mills(start_state_mills);
                    //设置阶段统计结束时间
                    pace.setEnd_state_mills(end_state_mills);
                    //设置阶段持续时间
                    pace.setState_lastmills(state_lastmills);
                    //设置电量变化百分比
                    pace.setChanged_battery(changed_battery);
                    //设置阶段开始总里程数
                    pace.setStart_obd_miles(start_obd_miles);
                    //设置阶段结束总里程数
                    pace.setEnd_obd_miles(end_obd_miles);
                    //设置阶段行驶总里程数
                    pace.setChanged_obd_miles(end_obd_miles-start_obd_miles);

                    //将该阶段加入阶段统计序列
                    paceList.add(pace);
                    //---------------------------此处华丽的分割线-----------------------------
                    //---------------------------开始重置一些重要状态-------------------------
                    start_state_mills=currentRecord.getStateCurrentMills();//重置阶段统计起始时间
                    start_obd_miles=currentRecord.getCshsObdMile();//重置开始阶段总里程数
                    state_lastmills=0;//重置阶段持续时间
                    changed_battery=0;//重置电量变化百分比
                    state_type=1;//重置阶段状态为充电
                }
                else if(tempRecordList.size()>=40){
                    HistoryStateRecord last_record= tempRecordList.get(tempRecordList.size()-1);
                    int middle_index = (int)(tempRecordList.size()/2);
                    HistoryStateRecord middle_record = tempRecordList.get(middle_index);
                    //若满足闲置性条件则生成行驶记录
                    if(last_record.getCshsObdMile()-middle_record.getCshsObdMile()<1){
                        end_state_mills=previousRecord.getStateCurrentMills();//设置状态统计结束时间
                        end_obd_miles=previousRecord.getCshsObdMile();//设置阶段结束总里程数

                        pace = new Pace();
                        //设置阶段状态种类
                        pace.setState_type(state_type);
                        //设置车机号
                        pace.setCshsNumber(previousRecord.getCshsNumber());
                        //设置授权系统
                        pace.setCshsAccess(previousRecord.getCshsAccess());
                        //设置阶段统计年份
                        pace.setState_year(DateTimeUtil.getYear(start_state_mills));
                        //设置阶段统计月份
                        pace.setState_month(DateTimeUtil.getMonth(start_state_mills));
                        //设置月内统计行号
                        pace.setMonth_rownum(month_rownum);
                        //设置阶段统计起始时间
                        pace.setStart_state_mills(start_state_mills);
                        //设置阶段统计结束时间
                        pace.setEnd_state_mills(end_state_mills);
                        //设置阶段持续时间
                        pace.setState_lastmills(state_lastmills);
                        //设置电量变化百分比
                        pace.setChanged_battery(changed_battery);
                        //设置阶段开始总里程数
                        pace.setStart_obd_miles(start_obd_miles);
                        //设置阶段结束总里程数
                        pace.setEnd_obd_miles(end_obd_miles);
                        //设置阶段行驶总里程数
                        pace.setChanged_obd_miles(end_obd_miles-start_obd_miles);

                        //---------------------------此处华丽的分割线-----------------------------
                        //---------------------------开始重置一些重要状态-------------------------
                        start_state_mills=currentRecord.getStateCurrentMills();//重置阶段统计起始时间
                        start_obd_miles=currentRecord.getCshsObdMile();//重置开始阶段总里程数
                        state_lastmills=0;//重置阶段持续时间
                        changed_battery=0;//重置电量变化百分比
                        state_type = 3;//重置阶段状态为闲置
                        tempRecordList.clear();//清除缓存序列
                    }
                }
                else{
                    continue;
                }
            }
            //如果该状态为闲置状态
            else if(state_type==3){
                tempRecordList.add(currentRecord);
                int cshsSpeed = currentRecord.getCshsSpeed();
                if(currentRecord.getCshsSpeed()>0
                        ||(near_changed_battery>0&&cshsSpeed==0)
                        ||i==stateList.size()-1){
                    end_state_mills=previousRecord.getStateCurrentMills();//设置状态统计结束时间
                    end_obd_miles=previousRecord.getCshsObdMile();//设置阶段结束总里程数

                    pace = new Pace();
                    //设置阶段状态种类
                    pace.setState_type(state_type);
                    //设置车机号
                    pace.setCshsNumber(previousRecord.getCshsNumber());
                    //设置授权系统
                    pace.setCshsAccess(previousRecord.getCshsAccess());
                    //设置阶段统计年份
                    pace.setState_year(DateTimeUtil.getYear(start_state_mills));
                    //设置阶段统计月份
                    pace.setState_month(DateTimeUtil.getMonth(start_state_mills));
                    //设置月内统计行号
                    pace.setMonth_rownum(month_rownum);
                    //设置阶段统计起始时间
                    pace.setStart_state_mills(start_state_mills);
                    //设置阶段统计结束时间
                    pace.setEnd_state_mills(end_state_mills);
                    //设置阶段持续时间
                    pace.setState_lastmills(state_lastmills);
                    //设置电量变化百分比
                    pace.setChanged_battery(changed_battery);
                    //设置阶段开始总里程数
                    pace.setStart_obd_miles(start_obd_miles);
                    //设置阶段结束总里程数
                    pace.setEnd_obd_miles(end_obd_miles);
                    //设置阶段行驶总里程数
                    pace.setChanged_obd_miles(end_obd_miles-start_obd_miles);

                    //将该阶段加入阶段统计序列
                    paceList.add(pace);
                    //---------------------------此处华丽的分割线-----------------------------
                    //---------------------------开始重置一些重要状态-------------------------
                    start_state_mills=currentRecord.getStateCurrentMills();//重置阶段统计起始时间
                    start_obd_miles=currentRecord.getCshsObdMile();//重置开始阶段总里程数
                    state_lastmills=0;//重置阶段持续时间
                    changed_battery=0;//重置电量变化百分比
                    tempRecordList.clear();//清除缓存序列

                    if(currentRecord.getCshsSpeed()>0){
                        state_type=2;
                    }
                    else if(near_changed_battery>0&&cshsSpeed==0){
                        state_type=1;
                    }
                    else{
                        state_type=1;
                    }
                }
            }
        }

        return paceList;
    }


}
