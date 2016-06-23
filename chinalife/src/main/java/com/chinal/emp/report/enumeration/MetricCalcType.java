package com.chinal.emp.report.enumeration;

/**
 * 指标运算类型 <br>
 * <p>
 * Create on : 2016年6月14日<br>
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
public enum MetricCalcType {
	/**
	 * Count
	 */
	count("Count"),
	/**
	 * Average
	 */
	average("Average"),
	/**
	 * Sum
	 */
	sum("Sum"),
	/**
	 * Median
	 */
	median("Median"),
	/**
	 * Min
	 */
	min("Min"),
	/**
	 * Max
	 */
	max("Max"),
	/**
	 * Unique Count
	 */
	uniqueCount("Unique Count"),
	/**
	 * Percentiles
	 */
	percentiles("Percentiles"),
	/**
	 * Percentile Ranks
	 */
	percentileRanks("Percentile Ranks");

	/**
	 * displayName
	 */
	private String m_displayName;

	/**
	 * Constructors.
	 * 
	 * @param displayName
	 *            displayName
	 */
	private MetricCalcType(final String displayName) {
		this.m_displayName = displayName;
	}

	/**
	 * getDisplayName.
	 * 
	 * @return displayName
	 */
	public String getDisplayName() {
		return this.m_displayName;
	}

	/**
	 * {method description}.
	 * 
	 * @param key
	 *            key
	 * @return MetricCalcType
	 */
	public static MetricCalcType parse(final String key) {
		MetricCalcType[] values = MetricCalcType.values();
		for (MetricCalcType item : values) {
			if (item.name().equals(key)) {
				return item;
			}
		}
		return null;
	}
}
