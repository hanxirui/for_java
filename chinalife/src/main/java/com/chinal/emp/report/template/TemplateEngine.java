package com.chinal.emp.report.template;

import org.apache.commons.lang.StringUtils;

import com.chinal.emp.report.vo.TemplateVo;

/**
 * 端到端portal模板处理类 <br>
 * <p>
 * Create on : 2016年1月21日<br>
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
public class TemplateEngine {

	/**
	 * <code>m_instance</code> - 单例.
	 */
	private static TemplateEngine s_instance = new TemplateEngine();

	/**
	 * Constructors.
	 */
	private TemplateEngine() {
	}

	/**
	 * {method description}.
	 * 
	 * @return TemplateQueryEngine
	 */
	public static TemplateEngine instance() {
		return s_instance;
	}

	/**
	 * 查询方式.
	 * 
	 * @param templateId
	 *            templateId
	 * @param userId
	 *            userId
	 * @return TemplateVo
	 * @throws ServiceException
	 *             ServiceException
	 * @throws InfoException
	 *             InfoException
	 */
	public TemplateVo execute(final String templateId, final String userId) throws Exception {
		if (StringUtils.isNotBlank(userId)) {
			return query(templateId, userId, true);
		} else {
			return query(templateId, userId, false);
		}
	}

	/**
	 * 查询.
	 * 
	 * @param templateId
	 *            templateId
	 * @param userId
	 *            userId
	 * @param hasUdata
	 *            是否包含用户数据
	 * @return TemplateVo
	 * @throws ServiceException
	 *             ServiceException
	 * @throws InfoException
	 *             InfoException
	 */
	private TemplateVo query(final String templateId, final String userId, final boolean hasUdata) throws Exception {
		TemplateVo template = TemplateQuery.query(templateId, userId, hasUdata);
		return template;
	}

}
