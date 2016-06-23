package com.chinal.emp.report.util;

/**
 * Web应用所用到的常量 <br>
 * <p>
 * Create on : 2011-10-13<br>
 * </p>
 * <br>
 * 
 * @author liuyang<br>
 * @version riil.web.common v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public final class WebConstanse {

	/**
	 * <code>S_REQUEST_ERRCODE_KEY</code> - request中放错误信息的key.
	 */
	public static final String S_REQUEST_ERRMSG_KEY = "errMsg";
	/**
	 * <code>S_ADMIN_USER_ID</code> - 管理员帐号.
	 */
	public static final String S_ADMIN_USER_ACCTOUNT = "admin";
	/**
	 * <code>S_ADMIN_USER_ID</code> - 系统管理员ID.
	 */
	public static final String S_ADMIN_USER_ID = "user-admin";
	/**
	 * <code>S_TOPO_PAGE_ID</code> - 网络拓扑页签ID.
	 */
	public static final String S_PORTAL_CONTEXT_KEY = "vs-1";
	/**
	 * <code>S_ADMIN_CONTEXT_KEY</code> - admin应用context key.
	 */
	public static final String S_ADMIN_CONTEXT_KEY = "vs-2";
	/**
	 * <code>S_CMDB_CONTEXT_KEY</code> - cmdb应用context key.
	 */
	public static final String S_CMDB_CONTEXT_KEY = "vs-3";
	/**
	 * <code>S_NETTOPO_CONTEXT_KEY</code> - nettopo应用context key.
	 */
	public static final String S_NETTOPO_CONTEXT_KEY = "vs-4";
	/**
	 * <code>S_NFA_CONTEXT_KEY</code> - nfa应用context key.
	 */
	public static final String S_NFA_CONTEXT_KEY = "vs-5";
	/**
	 * <code>S_BIZSERVICE</code> - bizservice应用context key.
	 */
	public static final String S_BIZSERVICE_CONTEXT_KEY = "vs-6";
	/**
	 * <code>S_KNOWLEDGE</code> - knowledge应用context key.
	 */
	public static final String S_KNOWLEDGE_CONTEXT_KEY = "vs-7";
	/**
	 * <code>S_JEFANG</code> - jifang应用context key.
	 */
	public static final String S_JIFANG_CONTEXT_KEY = "vs-8";
	/**
	 * <code>S_SSO_PARAM</code> - sso请求中的参数KEY.
	 */
	public static final String S_SSO_PARAM = "sso";
	/**
	 * <code>S_NETTOPO_PAGE_KEY</code> - 网络拓扑页签KEY.
	 */
	public static final String S_NETTOPO_PAGE_KEY = "pg-64";
	/**
	 * <code>S_POLLING_PAGE_KEY</code> - 自动巡检页签KEY.
	 */
	public static final String S_POLLING_PAGE_KEY = "pg-66";
	/**
	 * <code>s_currentCtx</code> - 当前应用访问上下文.
	 */
	public static String s_currentCtx = "";
	/**
	 * <code>SECONDS_PER_YEAR</code> - 一年的分钟数.
	 */
	public static final int S_SECONDS_PER_YEAR = 60 * 60 * 24 * 365;
	/**
	 * <code>COOKIE_USER_ID</code> - cookie中存储用户帐号的KEY.
	 */
	public static final String S_COOKIE_USER_ID = "RIIL_ID";

	/**
	 * Constructors.
	 */
	private WebConstanse() {

	}

}
