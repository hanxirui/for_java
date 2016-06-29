package com.chinal.emp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static String yyyy_MM_dd = "yyyy-MM-dd";

	/**
	 * @param date
	 * @return 2016-07-12 00:00:00
	 */
	public static Date getDayBegin(Date date) {
		Calendar t_calendar = Calendar.getInstance();
		t_calendar.setTime(new Date());
		t_calendar.set(Calendar.HOUR_OF_DAY, 0);
		t_calendar.set(Calendar.MINUTE, 0);
		t_calendar.set(Calendar.SECOND, 0);

		Date t_startTime = t_calendar.getTime();
		return t_startTime;
	}

	/**
	 * @param date
	 * @return 2016-07-12 23:59:59
	 */
	public static Date getDayEnd(Date date) {
		Calendar t_calendar = Calendar.getInstance();
		t_calendar.setTime(new Date());
		t_calendar.set(Calendar.HOUR_OF_DAY, 23);
		t_calendar.set(Calendar.MINUTE, 59);
		t_calendar.set(Calendar.SECOND, 59);

		Date endTime = t_calendar.getTime();
		return endTime;
	}

	/**
	 * @param date
	 * @param format
	 * @return
	 */
	public static String format(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	public static String getFullFormatNow() {
		return getFullFormatDate(new Date());
	}

	/**
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getFullFormatDate(Date date) {
		return new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss).format(date);
	}

	public static String getShortFormatNow() {
		return getShortFormatDate(new Date());
	}

	/**
	 * @param date
	 * @return yyyy-MM-dd
	 */
	public static String getShortFormatDate(Date date) {
		return new SimpleDateFormat(yyyy_MM_dd).format(date);
	}

	/**
	 * yyyy
	 * 
	 * @return String
	 */
	public static String getCurrYear() {
		String today = getShortFormatDate(new Date());
		return today.substring(0, today.length() - 6);
	}

	/**
	 * mm
	 * 
	 * @return String
	 */
	public static String getCurrMonth() {
		String today = getShortFormatDate(new Date());
		return today.substring(5, today.length() - 3);
	}

	/**
	 * dd
	 * 
	 * @return String
	 */
	public static String getCurrDay() {
		String today = getShortFormatDate(new Date());
		return today.substring(8, today.length());
	}

	public static String dateAddMonth(String date, int month) {
		Calendar calendar = strToCalendar(date);

		calendar.add(Calendar.MONTH, month);

		return calendarToDate(calendar);
	}

	public static String getAddMonth(String curMonth, int month) {
		String preDate = DateUtil.dateAddMonth(curMonth + "-01", month);
		String[] smonth = preDate.split("-");

		return smonth[0] + "-" + smonth[1];
	}

	public static Calendar strToCalendar(String strDate) {
		Calendar calendar = Calendar.getInstance();

		int h = 0, m = 0, s = 0;

		String[] sdt = strDate.split(" ");
		if (sdt.length > 1) {
			String[] st = sdt[1].split(":");
			if (st.length > 0) {
				h = Integer.parseInt(st[0]);
			}
			if (st.length > 1) {
				m = Integer.parseInt(st[1]);
			}
			if (st.length > 2) {
				s = Integer.parseInt(st[2]);
			}
		}

		int year = 0, month = 0, day = 0;

		String[] sd = sdt[0].split("-");
		if (sd.length > 0) {
			year = Integer.parseInt(sd[0]);
		}
		if (sd.length > 1) {
			month = Integer.parseInt(sd[1]) - 1;
		}
		if (sd.length > 2) {
			day = Integer.parseInt(sd[2]);
		}

		calendar.set(year, month, day, h, m, s);

		return calendar;
	}

	public static String calendarToDate(Calendar calendar) {
		return new SimpleDateFormat(yyyy_MM_dd).format(calendar.getTime());
	}

	public static String calendarToDateTime(Calendar calendar) {
		return new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss).format(calendar.getTime());
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDayNumOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
		return dateOfMonth;
	}

	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println(DateUtil.getDayBegin(new Date()));
		System.out.println(DateUtil.getDayEnd(new Date()));
		System.out.println(DateUtil.getFullFormatDate(DateUtil.getDayBegin(new Date())));
		System.out.println(DateUtil.getFullFormatDate(DateUtil.getDayEnd(new Date())));
		System.out.println(DateUtil.getCurrDay());
		System.out.println(DateUtil.getCurrMonth());
		System.out.println(DateUtil.getCurrYear());
		System.out.println(DateUtil.getFullFormatDate(new Date()));
		System.out.println(DateUtil.dateAddMonth(DateUtil.getShortFormatDate(new Date()), 1));
		System.out.println(DateUtil.dateAddMonth(DateUtil.getShortFormatDate(new Date()), 3));
		System.out.println(DateUtil.dateAddMonth(DateUtil.getShortFormatDate(new Date()), 5));
		System.out.println(DateUtil.dateAddMonth(DateUtil.getShortFormatDate(new Date()), 7));
		System.out.println(DateUtil.getAddMonth(DateUtil.getShortFormatDate(new Date()), 7));
		System.out.println(DateUtil.strToCalendar(DateUtil.getShortFormatDate(new Date())));
	}

}
