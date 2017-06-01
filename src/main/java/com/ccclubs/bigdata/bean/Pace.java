package com.ccclubs.bigdata.bean;

/**
 * Created by Administrator on 2017/6/1 0001.
 */
public class Pace {
    //车机号
    private String cshsNumber;
    //状态阶段统计年份
    private int state_year;
    //状态阶段统计月份
    private int state_month;
    //状态阶段月内统计行号
    private int month_rownum;
    //状态阶段统计起始时间
    private String state_start_time;
    //状态阶段统计结束时间
    private String state_end_time;
    //状态持续时间(单位:毫秒)
    private long state_lastmills;
    //状态种类(1:充电  2:行驶)
    private int state_type;
    //状态子分类
    private int sub_state_type;
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

    public String getState_start_time() {
        return state_start_time;
    }

    public void setState_start_time(String state_start_time) {
        this.state_start_time = state_start_time;
    }

    public String getState_end_time() {
        return state_end_time;
    }

    public void setState_end_time(String state_end_time) {
        this.state_end_time = state_end_time;
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
