package com.ez.framework.core.util;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;

public abstract class DatetimeUtils {

	/**
	 * 日期时间格式
	 */
	public static final String S_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String S_FULLDATETIME = "yyyyMMddHHmmss";

	public static final String S_FILEDATETIME = "yyMMdd";

	public static final String S_SHORTDATE = "yyyyMMdd";
	/**
	 * 日期格式
	 */
	public static final String S_DATE_FORMAT = "yyyy-MM-dd";
	/**
	 * 时间格式
	 */
	public static final String S_TIME_FORMAT = "HH:mm:ss";
	public static final String S_TIME_FORMAT_SHORT = "HH:mm";

	/**
	 * 日期加时间计算
	 * 
	 * @param date
	 *            日期到天
	 * @param workTime
	 *            时分秒
	 * @return
	 * @throws ParseException
	 */
	public static Date addTime4Date(final Date date, final String workTime) throws ParseException {
		return addTime4Date(date, stringToTime(workTime));

	}

	/**
	 * 日期加时间计算
	 * 
	 * @param date
	 *            日期到天
	 * @param workTime
	 *            时分秒
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date addTime4Date(final Date date, final Time workTime) {
		Date t_time4ZeroHour = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
		Date t_worktime = new Date(workTime.getTime());
		t_time4ZeroHour = DateUtils.addHours(t_time4ZeroHour, t_worktime.getHours());
		t_time4ZeroHour = DateUtils.addMinutes(t_time4ZeroHour, t_worktime.getMinutes());
		t_time4ZeroHour = DateUtils.addSeconds(t_time4ZeroHour, t_worktime.getSeconds());
		return t_time4ZeroHour;
	}

	public static String datetimeToString(final Date t_date) throws ParseException {
		DateFormat df = new SimpleDateFormat(S_DATETIME_FORMAT);
		return df.format(t_date);
	}

	public static String fulltimeToString(final Date t_date) {
		DateFormat df = new SimpleDateFormat(S_FULLDATETIME);
		return df.format(t_date);
	}

	public static String filetime(final Date t_date) {
		DateFormat df = new SimpleDateFormat(S_FILEDATETIME);
		return df.format(t_date);
	}

	public static String shortDate(final Date t_date) {

		DateFormat df = new SimpleDateFormat(S_SHORTDATE);
		return df.format(t_date);

	}

	/**
	 * 字符串转换为时间.
	 * 
	 * @param t_fields
	 *            字符串
	 * @return 时间
	 * @throws ParseException
	 */
	public static String dateToString(final Date t_date) throws ParseException {
		DateFormat df = new SimpleDateFormat(S_DATE_FORMAT);
		return df.format(t_date);
	}

	/**
	 * 获得两个日期的 日差.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int daysBetween(final Date startDate, final Date endDate) {
		long beginTime = startDate.getTime();
		long endTime = endDate.getTime();

		return (int) ((endTime - beginTime) / (double) 86400000);
		// return (int) ((endTime - beginTime) / 86400000 + 0.5);
		// return (int) (endTime / 86400000 - beginTime / 86400000);
		// return (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24) + 0.5);
	}

	/**
	 * 获得两个日期的 日差.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(final String strStartDate, final String strEndDate) throws ParseException {
		return daysBetween(stringToDate(strStartDate), stringToDate(strEndDate));
	}

	/**
	 * 取得星期几.
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(final Date date) {
		Calendar t_calendar = Calendar.getInstance();

		t_calendar.setTimeInMillis(date.getTime());
		int dayOfWeek = t_calendar.get(Calendar.DAY_OF_WEEK);
		dayOfWeek -= 1;
		if (dayOfWeek == 0) {
			dayOfWeek = 7;
		}
		return dayOfWeek;
	}

	/**
	 * get end time of day<br>
	 * such as 2012-07-05 23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEnd4Day(final Date date) {
		Calendar t_calendar = Calendar.getInstance();
		t_calendar.setTime(date);
		t_calendar.set(Calendar.MILLISECOND, 0);
		t_calendar.set(Calendar.SECOND, 59);
		t_calendar.set(Calendar.MINUTE, 59);
		t_calendar.set(Calendar.HOUR_OF_DAY, 23);
		return t_calendar.getTime();
	}

	/**
	 * get end time of Month<br>
	 * such as 2012-07-31 23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEnd4Month(final Date date) {
		Calendar t_calendar = Calendar.getInstance();
		t_calendar.setTime(date);
		t_calendar.set(Calendar.MILLISECOND, 0);
		t_calendar.set(Calendar.SECOND, 59);
		t_calendar.set(Calendar.MINUTE, 59);
		t_calendar.set(Calendar.HOUR_OF_DAY, 23);
		t_calendar.set(Calendar.DAY_OF_MONTH, t_calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return t_calendar.getTime();
	}

	/**
	 * get end time of Week<br>
	 * such as 2012-07-08 23:59:59 sunday
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEnd4Week(final Date date) {
		Date t_end = DateUtils.addDays(getStart4Week(date), 6);
		Calendar t_calendar = Calendar.getInstance();
		t_calendar.setTime(t_end);
		t_calendar.set(Calendar.MILLISECOND, 0);
		t_calendar.set(Calendar.SECOND, 59);
		t_calendar.set(Calendar.MINUTE, 59);
		t_calendar.set(Calendar.HOUR_OF_DAY, 23);
		return t_calendar.getTime();
	}

	/**
	 * 取得今天零点的豪秒数.
	 * 
	 * @return 豪秒
	 */
	public static long getMillSecond4TodayZeroHour() {
		return DateUtils.truncate(new GregorianCalendar(), Calendar.DAY_OF_MONTH).getTimeInMillis();
	}

	/**
	 * get start time of day<br>
	 * such as 2012-07-01 00:00:00
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStart4Day(final Date date) {
		return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
	}

	/**
	 * get start time of Month<br>
	 * such as 2012-07-01 00:00:00
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStart4Month(final Date date) {
		Calendar t_calendar = Calendar.getInstance();
		t_calendar.setTime(date);
		t_calendar.set(Calendar.MILLISECOND, 0);
		t_calendar.set(Calendar.SECOND, 0);
		t_calendar.set(Calendar.MINUTE, 0);
		t_calendar.set(Calendar.HOUR_OF_DAY, 0);
		t_calendar.set(Calendar.DAY_OF_MONTH, 1);
		return t_calendar.getTime();
	}

	/**
	 * get start time of Week<br>
	 * such as 2012-07-02 00:00:00 monday
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStart4Week(final Date date) {
		Calendar tmp = Calendar.getInstance();
		tmp.setTime(date);
		tmp.set(Calendar.DAY_OF_MONTH, tmp.get(Calendar.DAY_OF_MONTH) - 1);

		Calendar t_calendar = Calendar.getInstance();
		t_calendar.setTime(tmp.getTime());
		t_calendar.set(Calendar.MILLISECOND, 0);
		t_calendar.set(Calendar.SECOND, 0);
		t_calendar.set(Calendar.MINUTE, 0);
		t_calendar.set(Calendar.HOUR_OF_DAY, 0);
		t_calendar.set(Calendar.DAY_OF_WEEK, 1);
		return t_calendar.getTime();
	}

	public static Date getFirstDay(final int year, final int month) {
		Calendar t_calendar = Calendar.getInstance();
		t_calendar.setTimeInMillis(System.currentTimeMillis());
		t_calendar.set(Calendar.MILLISECOND, 0);
		t_calendar.set(Calendar.SECOND, 0);
		t_calendar.set(Calendar.MINUTE, 0);
		t_calendar.set(Calendar.HOUR_OF_DAY, 0);
		t_calendar.set(Calendar.DAY_OF_MONTH, 1);
		t_calendar.set(Calendar.MONTH, month - 1);
		t_calendar.set(Calendar.YEAR, year);
		return t_calendar.getTime();
	}

	public static int getYear(final long millis) {
		Calendar t_calendar = Calendar.getInstance();
		t_calendar.setTimeInMillis(millis);
		return t_calendar.get(Calendar.YEAR);
	}

	public static int getMonth(final long millis) {
		Calendar t_calendar = Calendar.getInstance();
		t_calendar.setTimeInMillis(millis);
		return t_calendar.get(Calendar.MONTH) + 1;
	}

	public static int getCurrentMonthDayNum() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public static long getMonthDayNum(final int year, final int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public static String getYearMonth(final long millis) {
		Calendar t_calendar = Calendar.getInstance();
		t_calendar.setTimeInMillis(millis);
		int year = t_calendar.get(Calendar.YEAR);
		int month = t_calendar.get(Calendar.MONTH) + 1;
		String connChar = "-";
		if (month < 0) {
			connChar += "0";
		}
		return String.valueOf(year) + connChar + String.valueOf(month);
	}

	/**
	 * 获得当天的时间毫秒数
	 * 
	 * @param workTime
	 *            时间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getTime4Today(final Time workTime) {
		Date t_startTime = new Date(getMillSecond4TodayZeroHour());
		Date t_worktime = new Date(workTime.getTime());
		t_startTime = DateUtils.addHours(t_startTime, t_worktime.getHours());
		t_startTime = DateUtils.addMinutes(t_startTime, t_worktime.getMinutes());
		t_startTime = DateUtils.addSeconds(t_startTime, t_worktime.getSeconds());
		return t_startTime.getTime();
	}

	public static boolean isMatchWeek(final long time, final int[] weeks) {

		Calendar t_calendar = Calendar.getInstance();

		t_calendar.setTimeInMillis(time);
		int dayOfWeek = t_calendar.get(Calendar.DAY_OF_WEEK);
		dayOfWeek -= 1;
		if (dayOfWeek == 0) {
			dayOfWeek = 7;
		}
		for (int i = 0; i < weeks.length; i++) {
			if (dayOfWeek == weeks[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获得两个日期的 分钟差.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int minutesBetween(final Date startDate, final Date endDate) {
		long beginTime = startDate.getTime();
		long endTime = endDate.getTime();

		return (int) ((endTime - beginTime) / (double) 60000);
		// return (int) ((endTime - beginTime) / 86400000 + 0.5);
		// return (int) (endTime / 86400000 - beginTime / 86400000);
		// return (int) ((endTime - beginTime) / (1000 * 60 * 60 * 24) + 0.5);
	}

	/**
	 * change os time
	 * 
	 * @param date
	 *            new date
	 */
	public static void setSystemTime(final Date date) {
		// Operating system name
		String osName = System.getProperty("os.name");
		String cmd = "";
		try {
			if (osName.matches("^(?i)Windows.*$")) {// Window 系统
				// 格式：yyyy-MM-dd
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String t_date = df.format(date);
				cmd = " cmd /c date " + t_date;// cmd =
												// " cmd /c date 2009-03-26";
				Runtime.getRuntime().exec(cmd);
				// 格式 HH:mm:ss
				df = new SimpleDateFormat("HH:mm:ss");
				String t_time = df.format(date);
				cmd = "  cmd /c time " + t_time;// cmd =
												// " cmd /c time 22:35:00";
				Runtime.getRuntime().exec(cmd);
			} else {// Linux 系统
				// 格式 HH:mm:ss
				DateFormat df = new SimpleDateFormat("yyyyMMdd");
				String t_date = df.format(date);
				// 格式：yyyyMMdd
				cmd = "  date -s " + t_date;
				Runtime.getRuntime().exec(cmd);
				// 格式 HH:mm:ss
				df = new SimpleDateFormat("HH:mm:ss");
				String t_time = df.format(date);
				cmd = "  date -s " + t_time;
				Runtime.getRuntime().exec(cmd);
			}
		} catch (IOException e) {
		}
	}

	/**
	 * 字符串转换为时间.
	 * 
	 * @param t_fields
	 *            字符串
	 * @return 时间
	 * @throws ParseException
	 */
	public static Date stringToDate(final String timeString) throws ParseException {
		return DateUtils.parseDate(timeString, new String[] { S_DATE_FORMAT });// .S_DATETIME_FORMAT
	}

	/**
	 * 字符串转换为时间.
	 * 
	 * @param t_fields
	 *            字符串
	 * @return 时间
	 * @throws ParseException
	 */
	public static Date stringToDateTime(final String timeString) throws ParseException {
		return DateUtils.parseDate(timeString, new String[] { S_DATETIME_FORMAT });
	}

	public static Date stringToDateTime(final String timeString, final String pattern) throws ParseException {
		return DateUtils.parseDate(timeString, new String[] { pattern });
	}

	/**
	 * 字符串转换为时间.
	 * 
	 * @param t_fields
	 *            字符串
	 * @return 时间
	 * @throws ParseException
	 */
	public static Time stringToTime(final String timeString) throws ParseException {
		if (StringUtils.isBlank(timeString)) {
			return null;
		}
		if (timeString.length() == 5) {
			return new Time(DateUtils.parseDate(timeString, new String[] { S_TIME_FORMAT_SHORT }).getTime());
		}
		return new Time(DateUtils.parseDate(timeString, new String[] { S_TIME_FORMAT }).getTime());
	}

	/**
	 * type说明：day;week;month,season,year
	 * 
	 * @param type
	 *            day;week;month
	 * @return
	 */
	public static Date getStartDateByType(final String type) {
		if ("day".equals(type)) {
			return getStart4Day(new Date());
		} else if ("week".equals(type)) {
			return getStart4Week(new Date());
		} else if ("month".equals(type)) {
			return getStart4Month(new Date());
		} else if ("season".equals(type)) {
			return getStart4Season(new Date());
		} else if ("year".equals(type)) {
			return getStart4Year(new Date());
		}
		return null;
	}

	public static Date getStart4Year(final Date date) {
		String year = new DateTime(date).toString("yyyy-01-01");
		return DateTime.parse(year).toDate();
	}

	public static Date getStart4Season(final Date date) {
		return DateTime.parse(
				DateTime.now().plusMonths(0 - ((DateTime.now().getMonthOfYear() - 1) % 3)).toString("yyyy-MM-01"))
				.toDate();
	}

	public static void main(final String[] args) {
		// System.out.println(DatetimeUtils.getStart4Season(new
		// Date()).toLocaleString());
		// System.out.println(DatetimeUtils.getStart4Year(new
		// Date()).toLocaleString());
	}

	/*
	 * 获得指定时间前一天
	 */
	public static Date oneDayEarlier(Date date) {
		DateTime dt = new DateTime(date);
		dt = dt.plusDays(-1);
		return dt.toDate();
	}
}
