package com.chinal.emp.util;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * 前台展示所需的一些公共方法 <br>
 * <p>
 * Create on : 2012-6-12<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author liuyang<br>
 * @version riil.webcommons v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public final class WebUtil {

	/**
	 * Constructors.
	 */
	private WebUtil() {

	}

	/**
	 * 获取访问路径的上下文.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String(http://serverName:serverPort/context)
	 */
	public static String getCtx(final HttpServletRequest request) {
		final StringBuffer t_ctx = new StringBuffer();
		t_ctx.append(getServerPath(request));
		t_ctx.append(request.getContextPath());
		return t_ctx.toString();
	}

	/**
	 * 获取访问的http url.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String(http://serverName:serverport)
	 */
	public static String getServerPath(final HttpServletRequest request) {
		final StringBuffer t_serverPath = new StringBuffer();
		t_serverPath.append(request.getScheme()).append("://");
		t_serverPath.append(request.getServerName()).append(":");
		t_serverPath.append(request.getServerPort());
		return t_serverPath.toString();
	}

	/**
	 * 按中文排序.
	 * 
	 * @param list
	 *            待排序集合
	 */
	public static void sortByChina(final List<String> list) {
		final Comparator<Object> t_cmp = Collator.getInstance(Locale.CHINA);
		Collections.sort(list, t_cmp);
	}
}
