package com.chinal.emp.report;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * {class description} <br>
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
@XStreamAlias("Button")
public class ButtonVo extends BaseVo {

	/**
	 * <code>icon</code> - {description}.
	 */
	@XStreamAlias("icon")
	private String m_icon;

	/**
	 * <code>jsNameSpace</code> - {description}.
	 */
	@XStreamAlias("jsNameSpace")
	private String m_jsNameSpace;

	/**
	 * <code>jsFun</code> - {description}.
	 */
	@XStreamAlias("jsFun")
	private String m_jsFun;

	/**
	 * <code>jsFile</code> - {description}.
	 */
	@XStreamAlias("jsFile")
	private String m_jsFile;

	/**
	 * <code>action</code> - {description}.
	 */
	@XStreamAlias("action")
	private String m_action;

	/**
	 * <code>param</code> - {description}.
	 */
	@XStreamAlias("jsParam")
	private String m_param;

	/**
	 * @return icon - {return content description}
	 */
	public String getIcon() {
		return m_icon;
	}

	/**
	 * @param icon
	 *            - {parameter description}.
	 */
	public void setIcon(final String icon) {
		m_icon = icon;
	}

	/**
	 * @return jsNameSpace - {return content description}
	 */
	public String getJsNameSpace() {
		return m_jsNameSpace;
	}

	/**
	 * @param jsNameSpace
	 *            - {parameter description}.
	 */
	public void setJsNameSpace(final String jsNameSpace) {
		m_jsNameSpace = jsNameSpace;
	}

	/**
	 * @return jsFun - {return content description}
	 */
	public String getJsFun() {
		return m_jsFun;
	}

	/**
	 * @param jsFun
	 *            - {parameter description}.
	 */
	public void setJsFun(final String jsFun) {
		m_jsFun = jsFun;
	}

	/**
	 * @return jsFile - {return content description}
	 */
	public String getJsFile() {
		return m_jsFile;
	}

	/**
	 * @param jsFile
	 *            - {parameter description}.
	 */
	public void setJsFile(final String jsFile) {
		m_jsFile = jsFile;
	}

	/**
	 * @return action - {return content description}
	 */
	public String getAction() {
		return m_action;
	}

	/**
	 * @param action
	 *            - {parameter description}.
	 */
	public void setAction(final String action) {
		m_action = action;
	}

	/**
	 * @return param - {return content description}
	 */
	public String getParam() {
		return m_param;
	}

	/**
	 * @param param
	 *            - {parameter description}.
	 */
	public void setParam(final String param) {
		m_param = param;
	}

}
