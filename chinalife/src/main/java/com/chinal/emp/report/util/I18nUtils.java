package com.chinal.emp.report.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.NoSuchMessageException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 国际化工具类<br>
 * <p>
 * Create on : 2011-9-15<br>
 * </p>
 * <br>
 * 
 * @author weiyi@ruijie.com.cn<br>
 * @version riil_web_common v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public final class I18nUtils {

	/**
	 * Constructors.
	 */
	private I18nUtils() {

	}

	public static I18nUtils getInstance() {

		return new I18nUtils();
	}

	/**
	 * 获取国际化.
	 * 
	 * @param key
	 *            国际化key
	 * @param request
	 *            HttpServletRequest
	 * @return 国际化文本
	 */
	public static String getMessage(final String key, final HttpServletRequest request) {
		try {
			WebApplicationContext t_ctx = (WebApplicationContext) request
					.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
			return t_ctx.getMessage(key, null, Locale.getDefault());
		} catch (NoSuchMessageException t_e) {
		}
		return key;

	}

	/**
	 * 获取国际化.
	 * 
	 * @param key
	 *            国际化KEY
	 * @param args
	 *            参数
	 * @param request
	 *            HttpServletRequest
	 * @return 国际化文本
	 */
	public static String getMessage(final String key, final String[] args, final HttpServletRequest request) {
		if (args == null || args.length == 0) {
			return getMessage(key, request);
		}
		WebApplicationContext t_ctx = (WebApplicationContext) request
				.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		String[] t_args = new String[args.length];
		for (int t_i = 0; t_i < args.length; t_i++) {
			String t_msg = null;
			try {
				t_msg = t_ctx.getMessage(args[t_i], null, Locale.getDefault());
			} catch (NoSuchMessageException t_e) {
			}
			if (t_msg == null) {
				t_args[t_i] = args[t_i];
			} else {
				t_args[t_i] = t_msg;
			}
		}
		try {
			return t_ctx.getMessage(key, t_args, Locale.getDefault());
		} catch (NoSuchMessageException t_e) {
		}
		return key;
	}

	/**
	 * 获取当前Locale.
	 * 
	 * @return Locale
	 */
	public static Locale getCurrentLocale() {
		return Locale.getDefault();
	}

	public static String getMessage(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
