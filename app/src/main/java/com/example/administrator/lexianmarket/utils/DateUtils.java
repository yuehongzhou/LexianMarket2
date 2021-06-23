package com.example.administrator.lexianmarket.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private DateUtils() {
    }

    public static long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }
    public static long getFirstOfWeek(long time) {
        Date date1, date2;
        date1 = new Date(time);
        date2 = new Date(date1.getYear(), date1.getMonth(), date1.getDate() - (date1.getDay() + 6) % 7);
        return date2.getTime();
    }
    public static long getLastOfWeek(long time) {
        Date date1, date2;
        date1 = new Date(time);
        date2 = new Date(date1.getYear(), date1.getMonth(), date1.getDate() - (date1.getDay() + 6) % 7 + 6);
        return date2.getTime();
    }
    public static long getFirstOfMonth(long time) {
        Date date1, date2;
        date1 = new Date(time);
        date2 = new Date(date1.getYear(), date1.getMonth(), 1);
        return date2.getTime();
    }

    public static long getLastOfMonth(long time) {
        Date date1, date2;
        date1 = new Date(time);
        date2 = new Date(date1.getYear(), date1.getMonth() + 1, 0);
        return date2.getTime();
    }

}
