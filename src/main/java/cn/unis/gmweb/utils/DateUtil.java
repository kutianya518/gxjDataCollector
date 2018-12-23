package cn.unis.gmweb.utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

public class DateUtil {

    public static final String DATE_TIME_MILL_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String DATE_TIME_UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String HOUR_PATTERN = "yyyy-MM-dd HH";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String MONTH_PATTERN = "yyyy-MM";
    public static final String YEAR_PATTERN = "yyyy";

    public static Date dateStringTodateTime(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期转字符串
     *
     * @param date    日期
     * @param pattern 格式
     * @return 日期字符串
     */
    public static String dateTimeTodateString(Date date, String pattern) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     *UTC字符串转本地字符串，+8 hour
     * @param utcStr
     * @param pattern
     * @return
     */
    public static String  utcStringToLocalString(String utcStr,String pattern)  {
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_UTC_PATTERN);
        format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
        Date date= null;
        try {
            date = format.parse(utcStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTimeTodateString(date,pattern);
    }


}
