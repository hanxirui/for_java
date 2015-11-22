package com.hxr.webstone.util.tree;

/**
 * 树类型枚举 <br>
 * <p>
 * Create on : 2013-1-9<br>
 * </p>
 * <p>
 * 
 * @author liuyang<br>
 * @version riil.webcommons v6.2.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public enum TreeTypeEnum {
	/**
	 * <code>PICTURE</code> - 登录页面中显示的图片类型.
	 */
	CHECKBOX("checkbox"),
	/**
	 * <code>BACKGROUND</code> - 登录页面中背景类型.
	 */
	RADIO("radio"),

	/**
	 * <code>TEXT</code> - 登录页面中显示的文字类型.
	 */
	NORMAL("normal");

	/**
	 * <code>m_value</code> - 值.
	 */
	private String m_value;

	/**
	 * Constructors.
	 * 
	 * @param value
	 *            枚举值
	 */
	private TreeTypeEnum(final String value) {
		this.m_value = value;
	}

	/**
	 * 获得枚举值.
	 * 
	 * @return 枚举值
	 */
	public String value() {
		return this.m_value;
	}
}
