package com.yijian.them.utils;

import android.content.Context;

import com.yqjr.utils.wheel.TimeRange;
import com.yqjr.utils.wheel.WheelView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Times {
    private static String[] months_31 = new String[]{"01", "03", "05", "07", "08", "10", "12"};
    private static String[] months = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    public Times() {
    }

    public static List<String> yearList(int count) {
        List<String> mList = new ArrayList();
        mList.clear();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(1);

        for (int i = year; i > year - count; --i) {
            mList.add(i + "年");
        }

        return mList;
    }

    public static List<String> yearList(int year, int count) {
        List<String> mList = new ArrayList();
        mList.clear();

        for (int i = year; i < year + count; ++i) {
            mList.add(i + "年");
        }

        return mList;
    }

    public static List<String> monthList() {
        List<String> mList = new ArrayList();
        mList.clear();

        for (int i = 0; i < months.length; ++i) {
            mList.add(months[i] + "月");
        }

        return mList;
    }

    public static int getYear(int count, int year) {
        for (int i = 0; i < yearList(count).size(); ++i) {
            if (((String) yearList(count).get(i)).equals(year + "年")) {
                return i;
            }
        }

        return 0;
    }

    public static int getMonth(String month) {
        for (int i = 0; i < monthList().size(); ++i) {
            if (((String) monthList().get(i)).equals(month)) {
                return i;
            }
        }

        return 0;
    }

    public static List<String> dayList(String year, String month) {
        List<String> mList = new ArrayList();
        mList.clear();
        byte day;
        if (Integer.parseInt(year) % 4 != 0 && Integer.parseInt(year) % 400 != 0) {
            if (month.equals("02")) {
                day = 28;
            } else if (Arrays.asList(months_31).contains(month)) {
                day = 31;
            } else {
                day = 30;
            }
        } else if (month.equals("02")) {
            day = 29;
        } else if (Arrays.asList(months_31).contains(month)) {
            day = 31;
        } else {
            day = 30;
        }

        for (int i = 1; i < day + 1; ++i) {
            if (i < 10) {
                mList.add("0" + i + "日");
            } else {
                mList.add(i + "日");
            }
        }

        return mList;
    }

    public static final Date dateFromCommonStr(String stringDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            date = sdf.parse(stringDate);
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return date;
    }

    public static final String timeToStr(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(time);
    }

    public static final Date timeFromCNStr(String stringTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("H点m分");
        Date time = null;

        try {
            time = sdf.parse(stringTime);
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return time;
    }

    public static final String dateToStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static final Date dateTimeFromStr(String stringDateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = null;
        try {
            time = sdf.parse(stringDateTime);
        } catch (ParseException var4) {
            var4.printStackTrace();
        }
        return time;
    }

    public static final String dateTimeToStr(Date dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dateTime);
    }

    public static final Date dateTimeFromCustomStr(String date, String time) {
        Date dateTime = new Date();
        Calendar calendar = Calendar.getInstance();
        if (!date.equals("今天")) {
            if (date.equals("明天")) {
                calendar.add(5, 1);
            } else if (date.equals("后天")) {
                calendar.add(5, 2);
            } else {
                dateTime = dateFromCommonStr(date);
                calendar.setTime(dateTime);
            }
        }

        Date timeFormat = timeFromCNStr(time);

        try {
            calendar.set(11, timeFormat.getHours());
            calendar.set(12, timeFormat.getMinutes());
            dateTime = calendar.getTime();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return dateTime;
    }

    public static String showScore(float score) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format((double) score);
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    public static ArrayList<String> buildDays(TimeRange timeRange) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(timeRange.getStart_time());
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(timeRange.getEnd_time());
        calendarStart.add(12, 10);
        ArrayList daysList = new ArrayList();

        Date date;
        while (calendarStart.before(calendarEnd)) {
            date = calendarStart.getTime();
            daysList.add(dateToStr(date));
            calendarStart.add(5, 1);
        }

        if (isInSameDay(calendarStart, calendarEnd)) {
            date = calendarEnd.getTime();
            daysList.add(dateToStr(date));
        }

        return daysList;
    }

    public static ArrayList buildHoursByDay(WheelView wheelViewDay, TimeRange timeRange) {
        if (wheelViewDay.getSelectedPosition() == 0) {
            return buildHourListStart(timeRange);
        } else {
            return wheelViewDay.getSelectedPosition() == wheelViewDay.getSize() - 1 ? buildHourListEnd(timeRange) : buildNomalHourList();
        }
    }

    public static ArrayList buildMinutesByDayHour(WheelView wheelViewDay, WheelView wheelViewHour, TimeRange timeRange) {
        if (wheelViewDay.getSelectedPosition() == 0 && wheelViewHour.getSelectedPosition() == 0) {
            return buildMinuteListStart(timeRange);
        } else {
            return wheelViewDay.getSelectedPosition() == wheelViewDay.getSize() - 1 && wheelViewHour.getSelectedPosition() == wheelViewHour.getSize() - 1 ? buildMinuteListEnd(timeRange) : buildNomalMinuteList();
        }
    }

    public static ArrayList buildHourListStart(TimeRange timeRange) {
        Date dateStart = timeRange.getStart_time();
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(dateStart);
        calendarStart.add(12, 10);
        int hourStart = calendarStart.get(11);
        int min = calendarStart.get(12);
        ArrayList hourList = new ArrayList();
        Date dateEnd = timeRange.getEnd_time();
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(dateEnd);
        int hourEnd;
        if (isInSameDay(calendarStart, calendarEnd)) {
            hourEnd = calendarEnd.get(11);
        } else {
            hourEnd = 23;
        }

        for (int i = hourStart; i <= hourEnd; ++i) {
            hourList.add(i + "点");
        }

        return hourList;
    }

    public static ArrayList buildNomalHourList() {
        ArrayList hourList = new ArrayList();

        for (int i = 0; i < 24; ++i) {
            hourList.add(i + "点");
        }

        return hourList;
    }

    public static ArrayList buildHourListEnd(TimeRange timeRange) {
        Date dateEnd = timeRange.getEnd_time();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateEnd);
        int hourEnd = calendar.get(11);
        ArrayList hourList = new ArrayList();

        for (int i = 0; i <= hourEnd; ++i) {
            hourList.add(i + "点");
        }

        return hourList;
    }

    public static ArrayList buildMinuteListStart(TimeRange timeRange) {
        Date dateStart = timeRange.getStart_time();
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(dateStart);
        calendarStart.add(12, 10);
        int minStart = calendarStart.get(12) / 10 * 10;
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(timeRange.getEnd_time());
        int minEnd;
        if (isInSameHour(calendarStart, calendarEnd)) {
            minEnd = calendarEnd.get(12) / 10 * 10;
        } else {
            minEnd = 50;
        }

        ArrayList minList = new ArrayList();

        for (int i = minStart; i <= minEnd; i += 10) {
            minList.add(i + "分");
        }

        return minList;
    }

    public static ArrayList buildMinuteListEnd(TimeRange timeRange) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeRange.getEnd_time());
        int minEnd = calendar.get(12) / 10 * 10;
        ArrayList minList = new ArrayList();

        for (int i = 0; i <= minEnd; i += 10) {
            minList.add(i + "分");
        }

        return minList;
    }

    public static ArrayList buildNomalMinuteList() {
        ArrayList minuteList = new ArrayList();

        for (int i = 0; i < 60; i += 10) {
            minuteList.add(i + "分");
        }

        return minuteList;
    }

    public static boolean isInSameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(1) == calendar2.get(1) && calendar1.get(2) == calendar2.get(2) && calendar1.get(5) == calendar2.get(5);
    }

    public static boolean isInSameHour(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(1) == calendar2.get(1) && calendar1.get(2) == calendar2.get(2) && calendar1.get(5) == calendar2.get(5) && calendar1.get(11) == calendar2.get(11);
    }

    public static int getfutureYear(int count, int year) {
        for (int i = 0; i < futureYearList(count).size(); ++i) {
            if (((String) futureYearList(count).get(i)).equals(year + "")) {
                return i;
            }
        }

        return 0;
    }

    public static List<String> futureYearList(int count) {
        List<String> mList = new ArrayList();
        mList.clear();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(1);

        for (int i = year; i < year + count; ++i) {
            mList.add(i + "年");
        }

        return mList;
    }

    public static List<String> futureMonthList(int month) {
        List<String> mList = new ArrayList();
        mList.clear();

        for (int i = month; i < 13; ++i) {
            mList.add(i + "月");
        }

        return mList;
    }

    public static List<String> futuredayList(String year, String month, int date) {
        List<String> mList = new ArrayList();
        mList.clear();
        int day = 30;
        year = year.substring(0, year.length() - 1);
        month = month.substring(0, month.length() - 1);
        if (Integer.parseInt(year) % 4 != 0 && Integer.parseInt(year) % 400 != 0) {
            if (month.equals("2")) {
                day = 28;
            } else if (Arrays.asList(months_31).contains(month)) {
                day = 31;
            } else {
                day = 30;
            }
        } else if (month.equals("2")) {
            day = 29;
        } else if (Arrays.asList(months_31).contains(month)) {
            day = 31;
        } else {
            day = 30;
        }

        for (int i = date; i < day + 1; ++i) {
            mList.add(i + "日");
        }

        return mList;
    }


    public static final String YMD = "yyyyMMdd";
    public static final String YMD_YEAR = "yyyy";
    public static final String YMD_BREAK = "yyyy-MM-dd";
    public static final String YMDHMS = "yyyyMMddHHmmss";
    public static final String YMDHMS_BREAK = "yyyy-MM-dd HH:mm:ss";
    public static final String YMDHMS_BREAK_HALF = "yyyy-MM-dd HH:mm";
    /**
     * 计算时间差
     */
    public static final int CAL_MINUTES = 1000 * 60;
    public static final int CAL_HOURS = 1000 * 60 * 60;
    public static final int CAL_DAYS = 1000 * 60 * 60 * 24;

    /**
     * 获取当前时间格式化后的值
     *  
     *
     * @param pattern
     * @return
     */
    public static String getNowDateText(String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    /**
     * 获取日期格式化后的值
     *  
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getDateText(Date date, String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 字符串时间转换成Date格式
     *  
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date getDate(String date, String pattern) throws ParseException {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.parse(date);
    }

    private static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 获取时间戳
     *
     * @param date
     * @return
     */
    public static Long getTime(Date date) {
        return date.getTime();
    }

    /**
     * 计算时间差
     *
     * @param startDate
     * @param endDate
     * @param calType   计算类型,按分钟、小时、天数计算
     * @return
     */
    public static int calDiffs(Date startDate, Date endDate, int calType) {
        Long start = Times.getTime(startDate);
        Long end = Times.getTime(endDate);
        int diff = (int) ((end - start) / calType);
        return diff;
    }

    /**
     * 计算时间差值以某种约定形式显示
     *  
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String timeDiffText(Date startDate, Date endDate) {
        int calDiffs = Times.calDiffs(startDate, endDate, Times.CAL_MINUTES);
        if (calDiffs == 0) {
            return "刚刚";
        }
        if (calDiffs < 60) {
            return calDiffs + "分钟前";
        }
        calDiffs = Times.calDiffs(startDate, endDate, Times.CAL_HOURS);
        if (calDiffs < 24) {
            return calDiffs + "小时前";
        }
        if (calDiffs < 48) {
            return "昨天";
        }
        return Times.getDateText(startDate, Times.YMDHMS_BREAK);
    }

    /**
     * 显示某种约定后的时间值,类似微信朋友圈发布说说显示的时间那种
     *  
     *
     * @param date
     * @return
     */
    public static String showTimeText(Date date) {
        return Times.timeDiffText(date, new Date());
    }

    public static String getTime(String date) throws ParseException {
        Date start = Times.getDate(date, Times.YMDHMS_BREAK);
        return Times.showTimeText(start);
    }

    public static long getBirthDay(String birthday) {
        return dateTimeFromStr(birthday + " 00:00:00").getTime();
    }

}
