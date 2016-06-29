package com.chinal.emp.report.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.chinal.emp.report.vo.TemplateVo;

/**
 * 模板查询. <br>
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
public class TemplateQuery {

	/**
	 * Constructors.
	 */
	private TemplateQuery() {
	}

	/**
	 * 查询方式.
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
	 */
	public static final TemplateVo query(final String templateId, final String userId, final boolean hasUdata)
			throws Exception {
		TemplateVo template = read(templateId);
		if (template != null) {
			if (hasUdata) {
				UserDataService.instance().mergeUdata(templateId, userId, template);
			}
		}
		return template;
	}

	/**
	 * 查询所有template.
	 * 
	 * @return List<TemplateVo>
	 * @throws IOException
	 *             IOException
	 */
	public static final List<TemplateVo> queryAll() throws IOException {
		List<TemplateVo> templates = new ArrayList<TemplateVo>();
		try {
			templates = TemplateService.instance().readAllTemplate();
		} catch (IOException t_e) {
			throw new IOException(t_e);
		}
		return templates;
	}

	/**
	 * 查询模板.
	 * 
	 * @param templateId
	 *            templateId
	 * @return TemplateVo
	 * @throws IOException
	 *             IOException
	 */
	private static TemplateVo read(final String templateId) throws IOException {
		TemplateVo template = null;
		try {
			template = TemplateService.instance().readTemplate(templateId);
		} catch (IOException t_e) {
			throw new IOException(t_e);
		}
		return template;
	}

}
