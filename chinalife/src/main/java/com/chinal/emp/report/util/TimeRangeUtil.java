package com.chinal.emp.report.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * {class description} <br>
 * <p>
 * Create on : 2016年3月14日<br>
 * </p>
 * <br>
 * 
 * @author chengxuetao@ruijie.com.cn<br>
 * @version riil-education-action v6.7.8 <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public final class TimeRangeUtil {

	/**
	 * 最近一天.
	 */
	private static final String S_1D = "1d";
	/**
	 * 最近一周.
	 */
	private static final String S_1W = "1w";
	/**
	 * 最近一小时.
	 */
	private static final String S_1H = "1h";
	/**
	 * 最近15分钟.
	 */
	private static final String S_15M = "15m";
	/**
	 * 最近4小时.
	 */
	private static final String S_4H = "4h";
	/**
	 * S_START.
	 */
	private static final String S_START = "start";
	/**
	 * S_END.
	 */
	private static final String S_END = "end";
	/**
	 * S_6.
	 */
	private static final int S_6 = 6;
	/**
	 * S_59.
	 */
	private static final int S_59 = 59;
	/**
	 * S_14.
	 */
	private static final int S_14 = 14;
	/**
	 * S_3.
	 */
	private static final int S_3 = 3;
	/**
	 * S_24.
	 */
	private static final int S_24 = 24;
	/**
	 * S_60.
	 */
	private static final int S_60 = 60;
	/**
	 * S_1000.
	 */
	private static final int S_1000 = 1000;
	/**
	 * S_3600.
	 */
	private static final int S_3600 = 3600;

	/**
	 * Constructors.
	 */
	private TimeRangeUtil() {
	}

	/**
	 * 获取查询时间范围.
	 * 
	 * @param timeType
	 *            1天、1周
	 * @return 查询时间范围
	 */
	public static Map<String, Object> getTimeRange(final String timeType) {
		Map<String, Object> result = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		long current = cal.getTimeInMillis();
		long zero = current / (S_1000 * S_3600 * S_24) * (S_1000 * S_3600 * S_24)
				- TimeZone.getDefault().getRawOffset();
		long twelve = zero + S_24 * S_60 * S_60 * S_1000 - 1;
		result.put(S_END, twelve);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String displayTimeRange = dateFormat.format(cal.getTime());
		if (S_1D.equals(timeType)) {
			result.put(S_START, zero);
			result.put("displayTimeRange", displayTimeRange);
		} else if (S_1W.equals(timeType)) {
			// 最近一周
			cal.add(Calendar.DAY_OF_MONTH, -S_6);
			current = cal.getTimeInMillis();
			zero = current / (S_1000 * S_3600 * S_24) * (S_1000 * S_3600 * S_24) - TimeZone.getDefault().getRawOffset();
			result.put(S_START, zero);
			result.put("displayTimeRange", dateFormat.format(cal.getTime()) + "~" + displayTimeRange);
		} else if (S_1H.equals(timeType)) {
			cal.setTime(new Date());
			long end = cal.getTimeInMillis();
			cal.add(Calendar.MINUTE, -S_59);
			long start = cal.getTimeInMillis();
			DateFormat dateFormat4Min = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			result.put(S_START, start);
			result.put(S_END, end);
			result.put("displayTimeRange", dateFormat4Min.format(start) + "~" + dateFormat4Min.format(end));
		} else if (S_15M.equals(timeType)) {
			cal.setTime(new Date());
			long end = cal.getTimeInMillis();
			cal.add(Calendar.MINUTE, -S_14);
			long start = cal.getTimeInMillis();
			DateFormat dateFormat4Min = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			result.put(S_START, start);
			result.put(S_END, end);
			result.put("displayTimeRange", dateFormat4Min.format(start) + "~" + dateFormat4Min.format(end));
		} else if (S_4H.equals(timeType)) {
			cal.setTime(new Date());
			cal.add(Calendar.HOUR_OF_DAY, -1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			long end = cal.getTimeInMillis();
			cal.add(Calendar.HOUR_OF_DAY, -S_3);
			long start = cal.getTimeInMillis();
			DateFormat dateFormat4Hour = new SimpleDateFormat("yyyy-MM-dd HH:00");
			result.put(S_START, start);
			result.put(S_END, end);
			result.put("displayTimeRange", dateFormat4Hour.format(start) + "~" + dateFormat4Hour.format(end));
		}
		return result;
	}

	/**
	 * 获得自定义时间范围.
	 * 
	 * @param customStartTime
	 *            自定义开始时间
	 * @param customEndTime
	 *            自定义结束时间
	 * @return 自定义时间范围
	 * @throws ParseException
	 *             解析异常
	 */
	public static Map<String, Object> getCustomTimeRange(final String customStartTime, final String customEndTime)
			throws ParseException {
		Map<String, Object> timeRange = new HashMap<String, Object>();
		DateFormat dateFormat4Sec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = dateFormat4Sec.parse(customStartTime + " 00:00:00");
		timeRange.put(S_START, startTime.getTime());
		Date endTime = dateFormat4Sec.parse(customEndTime + " 23:59:59");
		timeRange.put(S_END, endTime.getTime());
		return timeRange;
	}
}
