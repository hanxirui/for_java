package com.chinal.emp.report.enumeration;

/**
 * 大数据按时间interval查询频度枚举. <br>
 * <p>
 * Create on : 2016年3月4日<br>
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
public enum SearchFrequency {

	/**
	 * 每分钟统计.
	 */
	F_1M("1m"),

	/**
	 * 每5分钟统计.
	 */
	F_5M("5m"),

	/**
	 * 每小时统计.
	 */
	F_1H("1h"),

	/**
	 * 每天统计.
	 */
	F_1D("1d");

	/**
	 * 频度值.
	 */
	private String m_freq;

	/**
	 * Constructors.
	 * 
	 * @param freq
	 *            freq
	 */
	SearchFrequency(final String freq) {
		this.m_freq = freq;
	}

	/**
	 * @return freq - {return content description}
	 */
	public final String getFreq() {
		return m_freq;
	}

	/**
	 * @param freq
	 *            - {parameter description}.
	 */
	public final void setFreq(final String freq) {
		m_freq = freq;
	}

	/**
	 * 根据频度值获得大数据按时间interval查询频度.
	 * 
	 * @param freq
	 *            频度值
	 * @return 大数据按时间interval查询频度
	 */
	public static SearchFrequency getByFreq(final String freq) {
		for (SearchFrequency enumObj : SearchFrequency.values()) {
			if (enumObj.getFreq().equals(freq)) {
				return enumObj;
			}
		}
		return null;
	}
}
