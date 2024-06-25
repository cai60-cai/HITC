package com.cxs.utils;

import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author cxs
 *
 */
public class TimeUtil<main> {
    /**
     * 根据全局通用时间格式化
     * @param timeInMillis
     * @return
     */
    public static String formatDefault(long timeInMillis) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeInMillis);
        return formatDefault(time.getTime());
    }

    /**
     * 根据全局通用时间格式化
     * @param dateTime
     * @return
     */
    public static String formatDefault(LocalDateTime dateTime) {
        if (ObjectUtils.isEmpty(dateTime)) {
            return "";
        }
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        return formatDefault(date);
    }

    /**
     * 根据全局通用时间格式化
     * @param dateStr
     * @return
     */
    public static String formatDefault(String dateStr) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatDefault(format.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Date formatStrtoDate(String dateStr) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据全局通用时间格式化
     * @param dateStr
     * @return
     */
    public static String formatDateStr(String dateStr) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.format(Long.valueOf(dateStr)*1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 时间字符串转Long
     */
    public static Long formatStrDate(String dateStr){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(dateStr).getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据全局通用时间格式化
     * @param date
     * @return
     */
    public static String formatDefault(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar time = Calendar.getInstance();
        time.setTime(date);

        long timediff = (now.getTimeInMillis() - time.getTimeInMillis())/(1000*60); // 时间差（分钟）
        if(timediff < 60) { // 1小时内
            return timediff + "分钟前";
        } else {
            if(time.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                    && time.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && time.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) { // 当天
                return format(date, "HH:mm");
            } else if(time.get(Calendar.YEAR) == now.get(Calendar.YEAR)) { // 当年
                return format(date, "MM-dd HH:mm");
            } else { // 不在当年
                return format(date, "yyyy-MM-dd HH:mm");
            }
        }
    }

    /**
     * 根据format格式化时间
     *
     * @param time
     * @param format
     * @return
     */
    public static String format(Date time, String format) {
        String timeStr = null;
        if (time != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                timeStr = sdf.format(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return timeStr;
    }
}
