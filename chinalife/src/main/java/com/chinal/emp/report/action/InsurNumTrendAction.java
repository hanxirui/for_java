package com.chinal.emp.report.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.report.enumeration.SearchFrequency;
import com.chinal.emp.report.vo.InsurTrendReportVo;
import com.chinal.emp.service.InsuranceRecordService;
import com.chinal.emp.util.DateUtil;
import com.chinal.emp.util.SpringBeanUtils;

/**
 * 综合展示页数据处理action,根据数据源获取不同的类进行处理. <br>
 * <p>
 * Create on : 2016年1月22日<br>
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
@Controller
public class InsurNumTrendAction extends AbsAction {

	/**
	 * 最近一天.
	 */
	private static final String S_1D = "1d";
	/**
	 * 最近一周.
	 */
	private static final String S_1W = "1w";
	/**
	 * 自定义.
	 */
	private static final String S_CUSTOM = "custom";
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.riil.ete.portal.action.AbsAction#actionData(java.util.Map)
	 */
	@Override
	public Map<String, Object> actionData(final Map<String, Object> map) throws Exception {

		try {
			InsuranceRecordService insuranceRecordService = SpringBeanUtils.getBean2(InsuranceRecordService.class);
			String data = (String) map.get("parameter");
			Map<String, String> timeRange = getDefaultTimeRange();
			SearchFrequency freq = null;
			if (StringUtils.isNotBlank(data)) {
				JSONObject jsonData = JSONObject.parseObject(data);
				if (jsonData.containsKey("timeType")) {
					String timeType = (String) jsonData.get("timeType");
					map.put("timeType", timeType);
					if (S_CUSTOM.equals(timeType)) {
						String start = (String) jsonData.get(S_START);
						String end = (String) jsonData.get(S_END);

						map.put("startDate", start);
						map.put("endDate", end);
						timeRange.put(S_START, start);
						timeRange.put(S_END, end);
					}
				} else {
					map.put("timeType", "1m");
				}
			}
			freq = SearchFrequency.F_1D;
			map.put("freq", freq.getFreq());
			map.put("displayTimeRange", timeRange.get("displayTimeRange"));

			String start = timeRange.get(S_START);
			String end = timeRange.get(S_END);

			List<InsurTrendReportVo> datas = insuranceRecordService.queryInsurTrendReport(start, end);
			if (datas.size() > 0) {
				map.put("nodata", false);
			} else {
				map.put("nodata", true);
			}
			map.put("insurTrendReport", JSONObject.toJSONString(datas));

		} catch (Exception e) {
			throw new Exception(e);
		}
		return map;

	}

	/**
	 * 获取查询时间范围.
	 * 
	 * @param timeType
	 *            1小时、1周
	 * @return 查询时间范围
	 */
	private Map<String, String> getDefaultTimeRange() {
		Map<String, String> result = new HashMap<String, String>();

		result.put(S_END, DateUtil.dateAddMonth(DateUtil.getShortFormatDate(new Date()), 1));

		result.put(S_START, DateUtil.getShortFormatDate(new Date()));
		result.put("displayTimeRange", DateUtil.getShortFormatDate(new Date()) + " ~ "
				+ DateUtil.dateAddMonth(DateUtil.getShortFormatDate(new Date()), 1));

		return result;
	}
}
