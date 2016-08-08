package com.chinal.emp.entity;

import org.durcframework.core.SearchEntity;

/**
 * @author hanxirui
 *
 */
public class AnalysisPojo extends SearchEntity {
	/**
	 * 
	 */
	private String startDate;
	/**
	 * 
	 */
	private String endDate;
	/**
	 * 
	 */
	private String orgCode;
	/**
	 * 
	 */
	private String topn;
	/**
	 * 查询类型：1按机构查询；2按个人查询
	 */
	private String orgType;
	/**
	 * 图形类型：线图，柱图，饼图
	 */
	private String chartType;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getTopn() {
		return topn;
	}

	public void setTopn(String topn) {
		this.topn = topn;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

}
