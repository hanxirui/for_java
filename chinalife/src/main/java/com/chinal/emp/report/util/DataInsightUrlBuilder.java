package com.chinal.emp.report.util;

/**
 * 生成获取DataInsight系统数据的url. <br>
 * <p>
 * Create on : 2016年2月26日<br>
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
public final class DataInsightUrlBuilder {

	/**
	 * Data Insight系统IP.
	 */
	private static final String S_IP = "";

	/**
	 * Data Insight系统Port.
	 */
	private static final String S_PORT = "";

	/**
	 * 查询给定时间内的统计信息的uri.
	 */
	private static final String S_URI_SEARCHBYDATE = "/apis/p2p/searchByDate";

	/**
	 * 按时间interval 分组后的统计信息的uri.
	 */
	private static final String S_URI_SEARCHBYDATEHISTOGRAM = "/apis/p2p/searchByDateHistogram";

	/**
	 * 查询EG数据.
	 */
	private static final String S_URI_SEARCHEG = "/apis/p2p/searchEG";

	/**
	 * 查询图表指标
	 */
	private static final String S_URI_METRIC = "/settings/mfs?token=riil";

	/**
	 * 查询通用饼图数据
	 */
	private static final String S_GENERAL_PIE_DATA = "/getBuckets?token=riil";
	/**
	 * 查询通用线图数据
	 */
	private static final String S_GENERAL_LINE_DATA = "/getGraph?token=riil";
	/**
	 * 查询通用柱图数据
	 */
	private static final String S_GENERAL_BAR_DATA = "/getBuckets?token=riil";
	/**
	 * 查询通用区域图数据
	 */
	private static final String S_GENERAL_AREA_DATA = "/getBuckets?token=riil";
	/**
	 * 查询通用指标卡片数据
	 */
	private static final String S_GENERAL_CALC_DATA = "/getMetrics?token=riil";
	/**
	 * 查询通用数据表数据
	 */
	private static final String S_GENERAL_TABLE_DATA = "/getBuckets?token=riil";
	/**
	 * 查询大数据平台支持的数据源.
	 */
	private static final String S_SEARCH_DATASOURCE = "/apis/category";

	/**
	 * Constructors.
	 */
	private DataInsightUrlBuilder() {
	}

	/**
	 * 查询给定时间内的统计信息的url.
	 * 
	 * @return String
	 */
	public static String getSearchByDateUrl() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(S_IP).append(":").append(S_PORT).append(S_URI_SEARCHBYDATE);
		return builder.toString();
	}

	/**
	 * 查询按时间interval 分组后的统计信息的url.
	 * 
	 * @return String
	 */
	public static String getSearchByDateHistogramUrl() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(S_IP).append(":").append(S_PORT).append(S_URI_SEARCHBYDATEHISTOGRAM);
		return builder.toString();
	}

	/**
	 * 查询查询EG数据信息的url.
	 * 
	 * @return String
	 */
	public static String getSearchEGUrl() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(S_IP).append(":").append(S_PORT).append(S_URI_SEARCHEG);
		return builder.toString();
	}

	/**
	 * 查询图表指标的url.
	 * 
	 * @return String
	 */
	public static String getSearchMetricUrl() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(S_IP).append(":").append(S_PORT).append(S_URI_METRIC);
		return builder.toString();
	}

	/**
	 * 查询通用饼图数据的url.
	 * 
	 * @return String
	 */
	public static String getGeneralPieDataUrl() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(S_IP).append(":").append(S_PORT).append(S_GENERAL_PIE_DATA);
		return builder.toString();
	}

	/**
	 * 查询通用线图数据的url.
	 * 
	 * @return String
	 */
	public static String getGeneralLineDataUrl() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(S_IP).append(":").append(S_PORT).append(S_GENERAL_LINE_DATA);
		return builder.toString();
	}

	/**
	 * 查询通用柱图数据的url.
	 * 
	 * @return String
	 */
	public static String getGeneralBarDataUrl() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(S_IP).append(":").append(S_PORT).append(S_GENERAL_BAR_DATA);
		return builder.toString();
	}

	/**
	 * 查询通用区域图数据的url.
	 * 
	 * @return String
	 */
	public static String getGeneralAreaDataUrl() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(S_IP).append(":").append(S_PORT).append(S_GENERAL_AREA_DATA);
		return builder.toString();
	}

	/**
	 * 查询通用指标卡片数据的url.
	 * 
	 * @return String
	 */
	public static String getGeneralCalcDataUrl() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(S_IP).append(":").append(S_PORT).append(S_GENERAL_CALC_DATA);
		return builder.toString();
	}

	/**
	 * 查询通用数据表数据的url.
	 * 
	 * @return String
	 */
	public static String getGeneralTableDataUrl() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(S_IP).append(":").append(S_PORT).append(S_GENERAL_TABLE_DATA);
		return builder.toString();
	}

	/**
	 * 查询大数据支持的数据源的url.
	 * 
	 * @return String
	 */
	public static String getSearchDataSourceUrl() {
		StringBuilder builder = new StringBuilder("http://");
		builder.append(S_IP).append(":").append(S_PORT).append(S_SEARCH_DATASOURCE);
		return builder.toString();
	}
}
