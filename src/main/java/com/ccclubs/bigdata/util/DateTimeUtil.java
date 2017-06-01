package com.ccclubs.bigdata.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

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

    public static void main(String[] args) {
        DateTimeUtil.Date2UnixFormat("2016-11-24 23:03:19",DateTimeUtil.format1);
    }
}
