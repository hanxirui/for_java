package com.chinal.emp.report.enumeration;

import com.chinal.emp.report.util.I18nUtils;

/**
 * 图表类型 <br>
 * <p>
 * Create on : 2016年5月18日<br>
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
public enum GraphType {

	/**
	 * 线图
	 */
	line(I18nUtils.getMessage("info.charttype.name.line")),
	/**
	 * 饼图
	 */
	pie(I18nUtils.getMessage("info.charttype.name.pie")),
	/**
	 * 柱图
	 */
	bar(I18nUtils.getMessage("info.charttype.name.bar")),
	/**
	 * 区域图
	 */
	area(I18nUtils.getMessage("info.charttype.name.area")),
	/**
	 * 数据表
	 */
	table(I18nUtils.getMessage("info.charttype.name.table")),
	/**
	 * 指标
	 */
	calculator(I18nUtils.getMessage("info.charttype.name.calculator"));

	/**
	 * 名称.
	 */
	private String m_name;

	/**
	 * @param name
	 *            name
	 */
	private GraphType(final String name) {
		this.m_name = name;
	}

	/**
	 * @return name - {return content description}
	 */
	public final String getName() {
		return m_name;
	}

	/**
	 * @param name
	 *            - {parameter description}.
	 */
	public final void setName(final String name) {
		m_name = name;
	}

	/**
	 * {method description}.
	 * 
	 * @param key
	 *            key
	 * @return ChartType
	 */
	public static GraphType parse(final String key) {
		GraphType[] values = GraphType.values();
		for (GraphType chartType : values) {
			if (chartType.name().equals(key)) {
				return chartType;
			}
		}
		return null;
	}

}
