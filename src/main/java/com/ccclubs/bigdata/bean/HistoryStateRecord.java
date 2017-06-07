package com.ccclubs.bigdata.bean;

import org.apache.log4j.Logger;
import org.apache.spark.sql.Row;

/**
 * Created by Administrator on 2017/6/4 0004.
 */
public class HistoryStateRecord {
    private static Logger logger = Logger.getLogger(HistoryStateRecord.class);

    public HistoryStateRecord(){

    }

    public HistoryStateRecord(Row row){
        //设置车机号
        try{
            cshsNumber = row.getAs("cshsNumber");
        }
        catch (Exception ex){
            logger.error("车机号转换失败!");
        }
        //设置授权系统
        try{
            cshsAccess = row.getAs("cshsAccess");
        }
        catch (Exception ex){
            logger.error("车机号转换失败!");
        }
        //设置当前上传时间
        try{
            stateCurrentMills = row.getAs("cshsCurrentTime");
        }
        catch (Exception ex){
            logger.error("上传时间转换失败!");
        }
        //设置电量百分比
        try{
            cshsEvBattery = Integer.parseInt(row.getAs("cshsEvBattery"));
        }
        catch (Exception ex){
            logger.error("电量百分比转换失败!");
        }
        //设置车速
        try{
            cshsSpeed = Integer.parseInt(row.getAs("cshsSpeed"));
        }
        catch (Exception ex){
            logger.error("车速转换失败!");
        }
        //总里程
        try{
            cshsObdMile= Long.parseLong(row.getAs("cshsObdMile"));
        }
        catch (Exception ex){
            logger.error("总里程转换失败!");
        }
    }

    //车机号
    private String cshsNumber="";
    //授权系统
    private int cshsAccess=-1;
    //当前上传时间
    private long stateCurrentMills=0;
    //电量百分比
    private int cshsEvBattery=0;
    //车速
    private int cshsSpeed=0;
    //总里程
    private long cshsObdMile=0;

    public String getCshsNumber() {
        return cshsNumber;
    }

    public void setCshsNumber(String cshsNumber) {
        this.cshsNumber = cshsNumber;
    }

    public int getCshsAccess() {
        return cshsAccess;
    }

    public void setCshsAccess(int cshsAccess) {
        this.cshsAccess = cshsAccess;
    }

    public long getStateCurrentMills() {
        return stateCurrentMills;
    }

    public void setStateCurrentMills(long stateCurrentMills) {
        this.stateCurrentMills = stateCurrentMills;
    }

    public int getCshsEvBattery() {
        return cshsEvBattery;
    }

    public void setCshsEvBattery(int cshsEvBattery) {
        this.cshsEvBattery = cshsEvBattery;
    }

    public int getCshsSpeed() {
        return cshsSpeed;
    }

    public void setCshsSpeed(int cshsSpeed) {
        this.cshsSpeed = cshsSpeed;
    }

    public long getCshsObdMile() {
        return cshsObdMile;
    }

    public void setCshsObdMile(long cshsObdMile) {
        this.cshsObdMile = cshsObdMile;
    }
}
