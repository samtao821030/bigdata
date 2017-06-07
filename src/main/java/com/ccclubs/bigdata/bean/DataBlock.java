package com.ccclubs.bigdata.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/6 0006.
 */
public class DataBlock {

    //状态种类(1:充电  2:驾驶  3:闲置)
    private int state_type;

    //数据块电量变化百分比
    private int changed_battery;

    //数据块行驶里程变化
    private long changed_obd_miles;

    //数据块起始行驶总里程
    private long start_obd_miles;
    //数据块结束行驶总里程
    private long end_obd_miles;

    //数据块平均速度
    private float avg_speed;

    //block起始时间
    private long block_startmills;

    //block起始电量百分比
    private int block_startbattery;

    //block结束电量百分比
    private int block_endbattery;

    public int getBlock_endbattery() {
        return block_endbattery;
    }

    public void setBlock_endbattery(int block_endbattery) {
        this.block_endbattery = block_endbattery;
    }

    public int getBlock_startbattery() {
        return block_startbattery;
    }

    public void setBlock_startbattery(int block_startbattery) {
        this.block_startbattery = block_startbattery;
    }

    //该数据块内记录列表
    private List<HistoryStateRecord> recordList=new ArrayList<>();

    public long getStart_obd_miles() {
        return start_obd_miles;
    }

    public void setStart_obd_miles(long start_obd_miles) {
        this.start_obd_miles = start_obd_miles;
    }

    public long getEnd_obd_miles() {
        return end_obd_miles;
    }

    public void setEnd_obd_miles(long end_obd_miles) {
        this.end_obd_miles = end_obd_miles;
    }

    public int getState_type() {
        return state_type;
    }

    public void setState_type(int state_type) {
        this.state_type = state_type;
    }

    public int getChanged_battery() {
        return changed_battery;
    }

    public void setChanged_battery(int changed_battery) {
        this.changed_battery = changed_battery;
    }

    public long getChanged_obd_miles() {
        return changed_obd_miles;
    }

    public void setChanged_obd_miles(long changed_obd_miles) {
        this.changed_obd_miles = changed_obd_miles;
    }

    public float getAvg_speed() {
        return avg_speed;
    }

    public void setAvg_speed(float avg_speed) {
        this.avg_speed = avg_speed;
    }

    public long getBlock_startmills() {
        return block_startmills;
    }

    public void setBlock_startmills(long block_startmills) {
        this.block_startmills = block_startmills;
    }

    public List<HistoryStateRecord> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<HistoryStateRecord> recordList) {
        this.recordList = recordList;
    }
}
