package com.ccclubs.bigdata.bean;

/**
 * Created by Administrator on 2017/6/1 0001.
 */
public class Pace {
    //车机号
    private String cshsNumber;
    //授权系统
    private int cshsAccess;
    //状态阶段统计年份
    private int state_year;
    //状态阶段统计月份
    private int state_month;
    //状态阶段月内统计行号
    private int month_rownum;
    //状态阶段统计起始时间
    private long start_state_mills;
    //状态阶段统计结束时间
    private long end_state_mills;
    //状态持续时间(单位:毫秒)
    private long state_lastmills;
    //状态种类(1:充电  2:驾驶  3:闲置)
    private int state_type;
    //状态子分类
    private int sub_state_type;
    //电量变化百分比
    private int changed_battery;
    //阶段开始行驶里程数
    private long start_obd_miles;
    //阶段结束行驶里程数
    private long end_obd_miles;
    //阶段行驶里程变化
    private long changed_obd_miles;

    public long getChanged_obd_miles() {
        return changed_obd_miles;
    }

    public void setChanged_obd_miles(long changed_obd_miles) {
        this.changed_obd_miles = changed_obd_miles;
    }

    public int getCshsAccess() {
        return cshsAccess;
    }

    public void setCshsAccess(int cshsAccess) {
        this.cshsAccess = cshsAccess;
    }

    public int getChanged_battery() {
        return changed_battery;
    }

    public void setChanged_battery(int changed_battery) {
        this.changed_battery = changed_battery;
    }

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

    //记录是否有效(0:无效  1:有效)
    private int active_flg;

    public String getCshsNumber() {
        return cshsNumber;
    }

    public void setCshsNumber(String cshsNumber) {
        this.cshsNumber = cshsNumber;
    }

    public int getState_year() {
        return state_year;
    }

    public void setState_year(int state_year) {
        this.state_year = state_year;
    }

    public int getState_month() {
        return state_month;
    }

    public void setState_month(int state_month) {
        this.state_month = state_month;
    }

    public int getMonth_rownum() {
        return month_rownum;
    }

    public void setMonth_rownum(int month_rownum) {
        this.month_rownum = month_rownum;
    }

    public long getStart_state_mills() {
        return start_state_mills;
    }

    public void setStart_state_mills(long start_state_mills) {
        this.start_state_mills = start_state_mills;
    }

    public long getEnd_state_mills() {
        return end_state_mills;
    }

    public void setEnd_state_mills(long end_state_mills) {
        this.end_state_mills = end_state_mills;
    }

    public long getState_lastmills() {
        return state_lastmills;
    }

    public void setState_lastmills(long state_lastmills) {
        this.state_lastmills = state_lastmills;
    }

    public int getState_type() {
        return state_type;
    }

    public void setState_type(int state_type) {
        this.state_type = state_type;
    }

    public int getSub_state_type() {
        return sub_state_type;
    }

    public void setSub_state_type(int sub_state_type) {
        this.sub_state_type = sub_state_type;
    }

    public int getActive_flg() {
        return active_flg;
    }

    public void setActive_flg(int active_flg) {
        this.active_flg = active_flg;
    }
}
