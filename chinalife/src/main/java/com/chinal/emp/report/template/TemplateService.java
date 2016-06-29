package com.chinal.emp.report.template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.chinal.emp.report.vo.ButtonVo;
import com.chinal.emp.report.vo.PortletVo;
import com.chinal.emp.report.vo.TemplateVo;
import com.chinal.emp.util.ServerEnvUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 模板服务.<br>
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
public class TemplateService {

	/**
	 * <code>S_TEMPLATEID_DUMMY</code> - 创建template的模板文件.
	 */
	public static final String S_TEMPLATEID_DUMMY = "template_dummy";

	/**
	 * <code>S_PORTLETID_DUMMY</code> - 添加portlet的模板id .
	 */
	public static final String S_PORTLETID_DUMMY = "Portlet_dummy";

	/**
	 * <code>S_PORTLETID_PLACEHOLDER</code> - 添加portlet占位小窗口portlet id.
	 */
	public static final String S_PORTLETID_PLACEHOLDER = "Portlet_placeholder";

	/**
	 * <code>S_XML</code> - {description}.
	 */
	public static final String S_XML = ".xml";

	/**
	 * <code>s_instance</code> - {description}.
	 */
	private static TemplateService s_instance = new TemplateService();

	/**
	 * <code>s_dummyTemplate</code> - 新建小窗口的模板.
	 */
	private static TemplateVo s_dummyTemplate = null;

	/**
	 * <code>m_xstream</code> - {description}.
	 */
	private XStream m_xstream = null;

	/**
	 * Constructors.
	 */
	private TemplateService() {
		m_xstream = new XStream(new DomDriver());
		m_xstream.autodetectAnnotations(true);
		m_xstream.alias("Button", ButtonVo.class);
		m_xstream.alias("Portlet", PortletVo.class);
		m_xstream.alias("Template", TemplateVo.class);
	}

	/**
	 * {method description}.
	 * 
	 * @return ReadWriteFile
	 */
	public static TemplateService instance() {
		return s_instance;
	}

	/**
	 * {method description}.
	 * 
	 * @param templateId
	 *            templateId
	 * @return TemplateVo
	 * @throws IOException
	 *             IOException
	 */
	public TemplateVo readTemplate(final String templateId) throws IOException {
		return (TemplateVo) readXml(ServerEnvUtil.S_TEMPLATE_PATH + templateId + S_XML);
	}

	/**
	 * {method description}.
	 * 
	 * @return List<TemplateVo>
	 * @throws IOException
	 *             IOException
	 */
	public List<TemplateVo> readAllTemplate() throws IOException {
		String filePath = ServerEnvUtil.S_TEMPLATE_PATH;
		File templateDir = new File(filePath);
		List<TemplateVo> templates = new ArrayList<TemplateVo>();
		if (templateDir != null && templateDir.isDirectory()) {
			File[] listFiles = templateDir.listFiles();
			for (File file : listFiles) {
				if (file.getName().startsWith(S_TEMPLATEID_DUMMY) || !file.getName().endsWith(S_XML)) {
					// 跳过模板文件
					continue;
				}
				TemplateVo template = (TemplateVo) readXml(file);
				templates.add(template);
			}
		}
		return templates;
	}

	/**
	 * {method description}.
	 * 
	 * @param templateId
	 *            templateId
	 * @param template
	 *            TemplateVo
	 * @throws IOException
	 *             IOException
	 */
	public void saveTemplate(final String templateId, final TemplateVo template) throws IOException {
		write(ServerEnvUtil.S_TEMPLATE_PATH + templateId + S_XML, template);
	}

	/**
	 * 删除template.
	 * 
	 * @param templateId
	 *            templateId
	 * @return true or false
	 * @throws IOException
	 *             IOException
	 */
	public boolean deleteTemplate(final String templateId) throws IOException {
		String filePath = ServerEnvUtil.S_TEMPLATE_PATH + templateId + S_XML;
		if (StringUtils.isNotBlank(filePath)) {
			File file = new File(filePath);
			return file.delete();
		}
		return false;
	}

	/**
	 * 获取portlet.
	 * 
	 * @param templateId
	 *            template id
	 * @param portletId
	 *            portlet id
	 * @return portlet设置数据
	 * @throws IOException
	 *             IOException
	 */
	public PortletVo getPortlet(final String templateId, final String portletId) throws IOException {
		TemplateVo template = readTemplate(templateId);
		List<PortletVo> portlets = template.getPortlets();
		PortletVo portlet = null;
		if (CollectionUtils.isNotEmpty(portlets)) {
			for (PortletVo portletVo : portlets) {
				if (portletVo.getId().equals(portletId)) {
					portlet = portletVo;
					break;
				}
			}
		}
		return portlet;
	}

	/**
	 * 获取portlet设置数据.
	 * 
	 * @param templateId
	 *            template id
	 * @param portletId
	 *            portlet id
	 * @return portlet设置数据
	 * @throws IOException
	 *             IOException
	 */
	public String getData(final String templateId, final String portletId) throws IOException {
		TemplateVo template = readTemplate(templateId);
		List<PortletVo> portlets = template.getPortlets();
		if (CollectionUtils.isNotEmpty(portlets)) {
			for (PortletVo portletVo : portlets) {
				if (portletVo.getId().equals(portletId)) {
					return portletVo.getData();
				}
			}
		}
		return "";
	}

	/**
	 * 保存portlet设置数据.
	 * 
	 * @param templateId
	 *            template id
	 * @param portletId
	 *            portlet id
	 * @param dataJson
	 *            设置数据
	 * @throws IOException
	 *             IOException
	 */
	public void saveData(final String templateId, final String portletId, final String dataJson) throws IOException {
		TemplateVo template = readTemplate(templateId);
		List<PortletVo> portlets = template.getPortlets();
		boolean needSave = false;
		if (CollectionUtils.isNotEmpty(portlets)) {
			for (PortletVo portletVo : portlets) {
				if (portletVo.getId().equals(portletId)) {
					portletVo.setData(dataJson);
					needSave = true;
					break;
				}
			}
		}
		if (needSave) {
			saveTemplate(templateId, template);
		}
	}

	/**
	 * getDummyTemplate.
	 * 
	 * @return TemplateVo
	 * @throws IOException
	 *             IOException
	 */
	public TemplateVo getDummyTemplate() throws IOException {
		if (s_dummyTemplate == null) {
			s_dummyTemplate = readTemplate(S_TEMPLATEID_DUMMY);
		}
		return s_dummyTemplate;
	}

	/**
	 * getDummyPortlet.
	 * 
	 * @return PortletVo
	 * @throws IOException
	 *             IOException
	 */
	public PortletVo getDummyPortlet() throws IOException {
		PortletVo portlet = null;
		TemplateVo dummyTemplate = getDummyTemplate();
		List<PortletVo> portlets = dummyTemplate.getPortlets();
		for (PortletVo portletVo : portlets) {
			if (S_PORTLETID_DUMMY.equals(portletVo.getId())) {
				portlet = portletVo;
				break;
			}
		}
		return portlet;
	}

	/**
	 * getPlaceholderPortlet.
	 * 
	 * @return PortletVo
	 * @throws IOException
	 *             IOException
	 */
	public PortletVo getPlaceholderPortlet() throws IOException {
		PortletVo portlet = null;
		TemplateVo dummyTemplate = getDummyTemplate();
		List<PortletVo> portlets = dummyTemplate.getPortlets();
		for (PortletVo portletVo : portlets) {
			if (S_PORTLETID_PLACEHOLDER.equals(portletVo.getId())) {
				portlet = portletVo;
				break;
			}
		}
		return portlet;
	}

	/**
	 * 写文件.
	 * 
	 * @param templateFile
	 *            templateId
	 * @param template
	 *            TemplateVo
	 * @throws IOException
	 *             IOException
	 */
	private void write(final String templateFile, final TemplateVo template) throws IOException {
		FileWriter t_fw = null;
		try {
			t_fw = new FileWriter(templateFile);
			synchronized (m_xstream) {
				t_fw.write(m_xstream.toXML(template));
				t_fw.flush();
			}
		} finally {
			if (t_fw != null) {
				t_fw.close();
				t_fw = null;
			}
		}
	}

	/**
	 * {method description}.
	 * 
	 * @param filePath
	 *            filePath
	 * @return Object
	 * @throws IOException
	 *             IOException
	 */
	private Object readXml(final String filePath) throws IOException {
		if (StringUtils.isBlank(filePath)) {
			return null;
		}
		return m_xstream.fromXML(new File(filePath));
	}

	/**
	 * {method description}.
	 * 
	 * @param filePath
	 *            filePath
	 * @return Object
	 * @throws IOException
	 *             IOException
	 */
	private Object readXml(final File file) throws IOException {
		if (file.exists()) {
			return m_xstream.fromXML(file);
		}
		return null;

	}
}
