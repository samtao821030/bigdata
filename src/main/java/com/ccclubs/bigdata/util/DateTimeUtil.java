package com.ccclubs.bigdata.util;

import com.ccclubs.bigdata.bean.DataBlock;
import com.ccclubs.bigdata.bean.HistoryStateRecord;
import scala.tools.cmd.gen.AnyVals;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/5/31 0031.
 */
public class DateTimeUtil {
    public static final String format1="yyyy-MM-dd HH:mm:ss";

    public static long Date2UnixFormat(String dateStr,String format){
        long timeMills=-1;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            timeMills = sdf.parse(dateStr).getTime();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return timeMills;
    }

    public static int getYear(long timeMills){
        int year = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMills);
        year = calendar.get(Calendar.YEAR);
        return year;
    }

    public static int getMonth(long timeMills){
        int month = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMills);
        month = calendar.get(Calendar.MONTH)+1;
        return month;
    }

    public static String getDateTimeByFormat1(long timeMills){
        String retDateTime="";
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format1);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            Date date = new Date();
            date.setTime(timeMills);
            retDateTime=sdf.format(date);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return retDateTime;
    }

    public static String getDateTimeFixByInterval(String DateTimeStr){
        String retDateTime="";
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format1);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            long timeMills = sdf.parse(DateTimeStr).getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timeMills);
            int minute=cal.get(Calendar.MINUTE);
            minute=((int)(minute/BlockStatusUtil.time_interval))*BlockStatusUtil.time_interval;
            cal.set(Calendar.MINUTE,minute);
            cal.set(Calendar.SECOND,0);
            Date date = new Date();
            date.setTime(cal.getTimeInMillis());
            retDateTime=sdf.format(date);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return retDateTime;
    }

    public static long getTimeMillsFixByInterval(long timeMills){
        long retMills=0;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format1);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timeMills);
            int minute=cal.get(Calendar.MINUTE);
            minute=((int)(minute/BlockStatusUtil.time_interval))*BlockStatusUtil.time_interval;
            cal.set(Calendar.MINUTE,minute);
            cal.set(Calendar.SECOND,0);
            Date date = new Date();
            retMills=cal.getTimeInMillis();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return retMills;
    }

    public static Map<Long,DataBlock> generateBlockMap(int year, int month){
        Map<Long,DataBlock> blockMap = new LinkedHashMap<Long,DataBlock>();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format1);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            long timeMills = sdf.parse("1970-01-01 00:00:00").getTime();

            Calendar start_cal = Calendar.getInstance();
            Calendar end_cal = Calendar.getInstance();
            start_cal.setTimeInMillis(timeMills);
            end_cal.setTimeInMillis(timeMills);


            start_cal.set(Calendar.YEAR,year);
            end_cal.set(Calendar.YEAR,year);

            start_cal.set(Calendar.MONTH,month-1);
            end_cal.set(Calendar.MONTH,month);

            start_cal.set(Calendar.DAY_OF_MONTH,1);
            end_cal.set(Calendar.DAY_OF_MONTH,1);

            while(start_cal.getTimeInMillis()<=end_cal.getTimeInMillis()){
                DataBlock dataBlock = new DataBlock();
                dataBlock.setBlock_startmills(start_cal.getTimeInMillis());
                blockMap.put(start_cal.getTimeInMillis(),dataBlock);
                start_cal.add(Calendar.MINUTE,BlockStatusUtil.time_interval);
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return blockMap;
    }

    public static void main(String[] args) {
        long timeMills=DateTimeUtil.Date2UnixFormat("2016-11-24 23:03:19",DateTimeUtil.format1);
        System.out.println(getYear(timeMills));
        System.out.println(getMonth(timeMills));
        System.out.println(getDateTimeByFormat1(timeMills));
        System.out.println(getDateTimeFixByInterval("2016-11-24 23:59:19"));
        //Map<Long,DataBlock> blockMap = DateTimeUtil.generateBlockMap(2016,5);
        System.out.println("ok");
    }
}
