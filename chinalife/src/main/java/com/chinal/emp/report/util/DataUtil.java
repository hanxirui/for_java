package com.chinal.emp.report.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * {class description} <br>
 * 
 * <p>
 * Create on : 2016年2月25日<br>
 * </p>
 * <br>
 * 
 * @author R04281<br>
 * @version riil-education-action v6.2.0 <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public class DataUtil {
	/**
	 * <code>S_YYYY_MM_DD_HH_MM</code> - yyyy-MM-dd HH:mm
	 */
	private static final String S_YYYY_MM_DD_HH = "yyyy-MM-dd HH";
	/**
	 * <code>S_YYYY_MM_DD_HH_MM</code> - {description}.
	 */
	private static final String S_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	/**
	 * <code>S_PRE</code> - 分和秒为0
	 */
	private static final String S_PRE = ":00:00";
	/**
	 * <code>S_7</code> - {description}.
	 */
	private static final int S_7 = -7;
	/**
	 * <code>S_INSTANCE</code> - 单例模式
	 */
	private static final DataUtil S_INSTANCE = new DataUtil();

	/**
	 * Constructors.
	 */
	private DataUtil() {

	}

	/**
	 * 单例模式.
	 *
	 * @return DataUtil
	 */
	public static DataUtil getInstance() {
		return S_INSTANCE;
	}

	/**
	 * 日期转换成字符串.
	 * 
	 * @param date
	 *            日期
	 * @return 字符串格式yyyy-MM-dd HH:mm
	 */
	public static String dateToString(final Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(S_YYYY_MM_DD_HH_MM);
		return sdf.format(date);
	}

	/**
	 * 取7天前日期
	 * 
	 * @return 返回当前日期之前7天的日期
	 */
	public String getBefore7Day() {
		String t_result = "";
		SimpleDateFormat sdf = new SimpleDateFormat(S_YYYY_MM_DD_HH);
		Calendar c = getCurrentTime();
		c.add(Calendar.DATE, S_7);
		Date monday = c.getTime();
		t_result = sdf.format(monday) + S_PRE;
		return t_result;

	}

	/**
	 * 当前时间 String类型
	 * 
	 * @return String 到小时
	 */
	public String getCurrentTimeStr() {
		String t_result = "";
		SimpleDateFormat sdf = new SimpleDateFormat(S_YYYY_MM_DD_HH);
		Calendar c = getCurrentTime();
		t_result = sdf.format(c.getTime()) + S_PRE;
		return t_result;
	}

	/**
	 * 当前时间 Calendar类型
	 * 
	 * @return Calendar
	 */
	public Calendar getCurrentTime() {
		Calendar c = Calendar.getInstance();
		return c;
	}

}
