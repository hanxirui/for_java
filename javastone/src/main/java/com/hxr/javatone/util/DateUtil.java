package com.hxr.javatone.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
public static String yyyy_MM_dd_HH_mm_ss="yyyy-MM-dd HH:mm:ss";

public static void main(final String[] args) {
//    获得一天的开始和结束时间
    Calendar t_calendar = Calendar.getInstance();
    t_calendar.setTime(new Date());
    t_calendar.set(Calendar.HOUR_OF_DAY, 0);
    t_calendar.set(Calendar.MINUTE, 0);
    t_calendar.set(Calendar.SECOND, 0);
     
    Date t_startTime = t_calendar.getTime();
 
    t_calendar.add(Calendar.DAY_OF_MONTH, 1);
    t_calendar.add(Calendar.SECOND, -1);
     
    Date t_endTime = t_calendar.getTime();
    System.out.println(t_startTime);
    System.out.println(t_endTime);
}
}
