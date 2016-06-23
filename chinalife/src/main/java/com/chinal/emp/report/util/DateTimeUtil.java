package com.chinal.emp.report.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间工具 <br>
 * Create on : 2011-9-22<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author liqiang@ruijie.com.cn<br>
 * @version riil_web_common v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public final class DateTimeUtil {

	/**
	 * <code>S_DATETIME_FORMAT_ZH</code> - 中文日期格式yyyy-MM-dd HH:mm.
	 */
	public static final String S_DATETIME_FORMAT_ZH = "yyyy-MM-dd HH:mm";

	/**
	 * <code>S_DATETIME_FORMAT_YMD</code> -yyyy-MM-dd.
	 */
	public static final String S_DATETIME_FORMAT_YMD = "yyyy-MM-dd";
	/**
	 * <code>S_DATETIME_FORMAT_YM</code> - yyyy-MM.
	 */
	public static final String S_DATETIME_FORMAT_YM = "yyyy-MM";
	/**
	 * <code>S_DATETIME_FORMAT_YMD_HMS</code> - yyyy-MM-dd HH:mm:ss.
	 */
	public static final String S_DATETIME_FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * <code>S_DATETIME_FORMAT_EN</code> - 英文日期格式.
	 */
	public static final String S_DATETIME_FORMAT_EN = "MM-dd-yyyy HH:mm";

	/**
	 * <code>S_FORMAT</code> - 日期格式化.
	 */
	private static final SimpleDateFormat S_FORMAT;

	static {
		// if (Locale.CHINA.toString().equals(Locale.getDefault().toString())) {
		// S_FORMAT = new SimpleDateFormat(S_DATETIME_FORMAT_ZH);
		// } else {
		S_FORMAT = new SimpleDateFormat(S_DATETIME_FORMAT_ZH);
		// }
	}

	/**
	 * Constructors.
	 */
	private DateTimeUtil() {
	}

	/**
	 * 日期转换成字符串.
	 * 
	 * @param date
	 *            日期
	 * @return 字符串格式yyyy-MM-dd HH:mm
	 */
	public static String dateToString(final Date date) {
		return S_FORMAT.format(date);
	}

	/**
	 * 毫秒转换成字符串.
	 * 
	 * @param millisecond
	 *            毫秒
	 * @return 字符串格式
	 */
	public static String millisecondToString(final long millisecond) {
		Date t_date = new Date(millisecond);
		return S_FORMAT.format(t_date);
	}

	/**
	 * 字符串格式转换成日期.
	 * 
	 * @param date
	 *            日期的字符串格式
	 * @return 日期
	 * @throws ParseException
	 *             异常
	 */
	public static Date stringToDate(final String date) throws ParseException {
		return S_FORMAT.parse(date);
	}

	/**
	 * 字符串格式转换成日期.
	 * 
	 * @param date
	 *            待格式化的日期
	 * @param format
	 *            格式
	 * @return 转换后的日期字符串
	 * @throws ParseException
	 *             抛出异常
	 */
	public static Date stringToDate(final String date, final String format) throws ParseException {
		SimpleDateFormat t_format = new SimpleDateFormat(format);
		return t_format.parse(date);
	}

	/**
	 * 日期转换成字符串.
	 * 
	 * @param date
	 *            待格式化日期
	 * @param format
	 *            格式
	 * @return 转换后的日期字符串
	 */
	public static String dateToString(final Date date, final String format) {
		SimpleDateFormat t_format = new SimpleDateFormat(format);
		return t_format.format(date);
	}

}
