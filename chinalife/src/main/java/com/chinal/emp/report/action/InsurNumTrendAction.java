package com.chinal.emp.report.action;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.chinal.emp.report.PortletVo;
import com.chinal.emp.report.enumeration.GraphType;

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
public class InsurNumTrendAction extends AbsAction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.riil.ete.portal.action.AbsAction#actionData(java.util.Map)
	 */
	@Override
	public Map<String, Object> actionData(final Map<String, Object> map) throws Exception {
		PortletVo portlet = (PortletVo) map.get("portlet");
		String dataSource = portlet.getDataSource();
		String data = (String) map.get("parameter");
		String graphType = GraphType.line.getName();
		if (StringUtils.isNotBlank(data)) {
			JSONObject jsonData = JSONObject.parseObject(data);
			// graphType = jsonData.getString(AbsGetDataAction.S_KEY_GRAPHTYPE);
			// map.put(AbsGetDataAction.S_KEY_GRAPHTYPE, graphType);
		}
		try {
			// ComprehensiveUtil.getAction(dataSource, graphType).getData(map);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return map;
	}
}
