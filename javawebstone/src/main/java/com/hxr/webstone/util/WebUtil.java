package com.hxr.webstone.util;

import javax.servlet.http.HttpServletRequest;

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

}
