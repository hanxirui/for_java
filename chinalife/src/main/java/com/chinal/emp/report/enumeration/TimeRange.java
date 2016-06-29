package com.chinal.emp.report.enumeration;

/**
 * 综合展示时间范围选择 <br>
 * <p>
 * Create on : 2016年6月2日<br>
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
public enum TimeRange {

	/**
	 * 最近15分钟.
	 */
	recent_15_min(15 * 60 * 1000, "15m"),

	/**
	 * 最近30分钟.
	 */
	recent_30_min(30 * 60 * 1000, "30m"),

	/**
	 * 最近1小时.
	 */
	recent_1_hour(60 * 60 * 1000, "1h"),

	/**
	 * 最近4小时.
	 */
	recent_4_hour(4 * 60 * 60 * 1000, "4h"),

	/**
	 * 最近12小时.
	 */
	recent_12_hour(12 * 60 * 60 * 1000, "12h"),

	/**
	 * 最近24小时.
	 */
	recent_24_hour(24 * 60 * 60 * 1000, "24h"),

	/**
	 * 最近7天.
	 */
	recent_7_day(7 * 24 * 60 * 60 * 1000, "7d"),

	/**
	 * 最近30天.
	 */
	recent_30_day(30 * 24 * 60 * 60 * 1000, "30d"),

	/**
	 * 最近60天.
	 */
	recent_60_day(60 * 24 * 60 * 60 * 1000, "60d"),

	/**
	 * 最近90天.
	 */
	recent_90_day(90 * 24 * 60 * 60 * 1000, "90d"),

	/**
	 * 最近6个月.
	 */
	recent_6_month(6 * 30 * 24 * 60 * 60 * 1000, "6mon"),

	/**
	 * 最近1年.
	 */
	recent_1_year(365 * 24 * 60 * 60 * 1000, "1year");

	/**
	 * S_7
	 */
	private static final long S_7 = 7;

	/**
	 * S_12
	 */
	private static final long S_12 = 12;

	/**
	 * S_24
	 */
	private static final long S_24 = 24;

	/**
	 * S_30
	 */
	private static final long S_30 = 30;

	/**
	 * S_60
	 */
	private static final long S_60 = 60;

	/**
	 * S_1000
	 */
	private static final long S_1000 = 1000;

	/**
	 * 时间差（ms）.
	 */
	private long m_difftime;

	/**
	 * 描述信息.
	 */
	private String m_desc;

	/**
	 * Constructors.
	 * 
	 * @param difftime
	 *            开始结束时间差
	 * @param desc
	 *            描述信息
	 */
	private TimeRange(final long difftime, final String desc) {
		this.m_difftime = difftime;
		this.m_desc = desc;
	}

	/**
	 * @return desc - {return content description}
	 */
	public final String getDesc() {
		return m_desc;
	}

	/**
	 * 获取开始时间（ms）.
	 * 
	 * @param to
	 *            结束时间
	 * @return 开始时间（ms）
	 */
	public final long getFrom(final long to) {
		return to - m_difftime;
	}

	/**
	 * 获取时间间隔.
	 * 
	 * @param from
	 *            开始时间
	 * @param to
	 *            结束时间
	 * @return 时间间隔
	 */
	public static String getInterval(final long from, final long to) {
		long rangetime = to - from;
		long single = rangetime / S_30;
		long second = S_1000;
		long minutes = second * S_60;
		long hour = minutes * S_60;
		long day = hour * S_24;
		long week = day * S_7;
		long month = day * S_30;
		long year = month * S_12;
		String interval = "";
		if (single / year > 1) {
			interval = single / year + "Y";
		} else if (single / month > 1) {
			interval = single / month + "M";
		} else if (single / week > 1) {
			interval = single / (week) + "w";
		} else if (single / day > 1) {
			interval = single / (day) + "d";
		} else if (single / hour > 1) {
			interval = single / (hour) + "h";
		} else if (single / minutes > 1) {
			interval = single / (minutes) + "m";
		} else {
			interval = "30s";
		}
		return interval;
	}

	/**
	 * {method description}.
	 * 
	 * @param key
	 *            key
	 * @return DataSource
	 */
	public static TimeRange parse(final String key) {
		TimeRange[] values = TimeRange.values();
		for (TimeRange item : values) {
			if (item.name().equals(key)) {
				return item;
			}
		}
		return null;
	}
}
