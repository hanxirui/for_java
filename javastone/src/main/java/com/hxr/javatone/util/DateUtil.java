package com.hxr.javatone.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static String yyyy_MM_dd = "yyyy-MM-dd";

	public static Date getDayBegin(Date date) {
		Calendar t_calendar = Calendar.getInstance();
		t_calendar.setTime(new Date());
		t_calendar.set(Calendar.HOUR_OF_DAY, 0);
		t_calendar.set(Calendar.MINUTE, 0);
		t_calendar.set(Calendar.SECOND, 0);

		Date t_startTime = t_calendar.getTime();
		return t_startTime;
	}

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
	 * @param format
	 * @param date
	 * @return
	 */
	public static String getDateByFormat(String format, Date date) {
		return new SimpleDateFormat(format).format(date);
	}

	public static String getFullFormatDate(Date date) {
		return new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss).format(date);
	}

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
