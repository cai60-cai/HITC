package com.cxs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    private static ZoneId zoneId;

    static {
        zoneId = ZoneId.systemDefault();
    }

    /**
     * LocalDateTime转Date
     *
     * @param dateTime
     * @param format
     * @return
     */
    public static Date LocalDateTimeToDate(LocalDateTime dateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        Instant instant = dateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    /**
     * 字符串转时间 Date
     *
     * @param date
     * @param format
     * @return
     */
    public static Date stringTimeToDate(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param date
     * @return
     */
    public static Date stringTimeToDate(String date) {
        return stringTimeToDate(date, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * date -- > string
     *
     * @param date
     * @return
     */
    public static String toStringDate(Date date) {
        String result = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        try {
            result = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获得单签时间的时间戳
     *
     * @return
     */
    public static String getDataTimeMillis() {
        String result = null;
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYYMMDDHHMMSS);
        try {
            result = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * date 转 str
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * localdatetime 转 str
     *
     * @param date
     * @return
     */
    public static String localDateTimeToString(LocalDateTime date, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得整点时间
     *
     * @param hour
     * @return
     */
    public static Date getHourDate(Integer hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hour, 0, 0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        try {
            return format.parse(format.format(cal.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获得周几
     *
     * @param dt
     * @return
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    public static String getWeekStr(Integer n) {
        String res = "";
        if (null == n) {
            return res;
        }
        switch (n) {
            case 1:
                res = "星期一";
                break;
            case 2:
                res = "星期二";
                break;
            case 3:
                res = "星期三";
                break;
            case 4:
                res = "星期四";
                break;
            case 5:
                res = "星期五";
                break;
            case 6:
                res = "星期六";
                break;
            case 7:
                res = "星期日";
                break;
            default:
                res = "";
                break;
        }
        return res;
    }
}
