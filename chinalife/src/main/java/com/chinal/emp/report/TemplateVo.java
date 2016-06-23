package com.chinal.emp.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 模板.<br>
 * <p>
 * Create on : 2016年1月26日<br>
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
@XStreamAlias("Template")
public class TemplateVo extends BaseVo {

	/**
	 * <code>portlets</code> - {description}.
	 */
	@XStreamAlias("portlets")
	private List<PortletVo> m_portlets = new ArrayList<PortletVo>();

	/**
	 * @return portlets - {return content description}
	 */
	public List<PortletVo> getPortlets() {
		if (CollectionUtils.isNotEmpty(m_portlets)) {
			Collections.sort(m_portlets);
		}
		return m_portlets;
	}

	/**
	 * @param portlets
	 *            - {parameter description}.
	 */
	public void setPortlets(final List<PortletVo> portlets) {
		this.m_portlets = portlets;
	}

}
