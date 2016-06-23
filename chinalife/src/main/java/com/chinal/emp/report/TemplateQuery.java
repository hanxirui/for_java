package com.chinal.emp.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	 * 查询指定的template.
	 * 
	 * @param templateId
	 *            templateId
	 * @return TemplateVo
	 * @throws IOException
	 *             IOException
	 */
	public static final TemplateVo query(final String templateId) throws IOException {
		TemplateVo template = read(templateId);
		return template;
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
