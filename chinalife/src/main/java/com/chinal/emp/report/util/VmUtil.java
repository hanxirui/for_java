package com.chinal.emp.report.util;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.chinal.emp.util.ServerEnvUtil;

/**
 * VM工具类. <br>
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
public final class VmUtil {

	/**
	 * <code>m_ve</code> - {description}.
	 */
	private static VelocityEngine s_ve = null;

	/**
	 * 初始化.
	 */
	static {
		s_ve = new VelocityEngine();
		Properties p = new Properties();
		p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, ServerEnvUtil.S_VM_PATH);
		s_ve.init(p);
	}

	/**
	 * Constructors.
	 */
	private VmUtil() {
	}

	/**
	 * 解析VM模板
	 * 
	 * @param param
	 *            模板所需要的参数
	 * @param fileName
	 *            模板文件名
	 * @return html串
	 */
	public static String parseVM(final Map<String, Object> param, final String fileName) {
		VelocityContext t_context = new VelocityContext();
		if (param != null) {
			Iterator<Entry<String, Object>> t_entryItra = param.entrySet().iterator();
			while (t_entryItra.hasNext()) {
				Entry<String, Object> t_entry = t_entryItra.next();
				String t_key = t_entry.getKey();
				t_context.put(t_key, t_entry.getValue());
			}
		}
		t_context.put("I18nUtils", I18nUtils.class);
		t_context.put("WebConstanse", WebConstanse.class);
		t_context.put("HandleCharacters", HandleCharacters.class);
		Template t_template = s_ve.getTemplate(fileName, "utf-8");
		StringWriter t_writer = new StringWriter();
		t_template.merge(t_context, t_writer);
		return t_writer.toString();
	}
}
