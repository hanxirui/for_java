package com.chinal.emp.report.vo;

/**
 * 用户数据.<br>
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
public class UserDataVo {

	/**
	 * 摸板中定义的portletId
	 */
	private String m_originalPortletId;

	/**
	 * <code>m_portletId</code> - {description}.
	 */
	private String m_portletId;
	/**
	 * <code></code> - {description}.
	 */
	private int m_x;

	/**
	 * <code>y</code> - {description}.
	 */
	private int m_y;

	/**
	 * <code>width</code> - {description}.
	 */
	private int m_width;

	/**
	 * <code>height</code> - {description}.
	 */
	private int m_height;

	/**
	 * <code>data</code> - {description}.
	 */
	private String m_data;

	/**
	 * <code>visibility</code> - {description}.
	 */
	private boolean m_visibility;

	/**
	 * @return originalPortletId - {return content description}
	 */
	public final String getOriginalPortletId() {
		return m_originalPortletId;
	}

	/**
	 * @param originalPortletId
	 *            - {parameter description}.
	 */
	public final void setOriginalPortletId(final String originalPortletId) {
		m_originalPortletId = originalPortletId;
	}

	/**
	 * @return portletId - {return content description}
	 */
	public String getPortletId() {
		return m_portletId;
	}

	/**
	 * @param portletId
	 *            - {parameter description}.
	 */
	public void setPortletId(final String portletId) {
		m_portletId = portletId;
	}

	/**
	 * @return x - {return content description}
	 */
	public int getX() {
		return m_x;
	}

	/**
	 * @param x
	 *            - {parameter description}.
	 */
	public void setX(final int x) {
		m_x = x;
	}

	/**
	 * @return y - {return content description}
	 */
	public int getY() {
		return m_y;
	}

	/**
	 * @param y
	 *            - {parameter description}.
	 */
	public void setY(final int y) {
		m_y = y;
	}

	/**
	 * @return width - {return content description}
	 */
	public int getWidth() {
		return m_width;
	}

	/**
	 * @param width
	 *            - {parameter description}.
	 */
	public void setWidth(final int width) {
		m_width = width;
	}

	/**
	 * @return height - {return content description}
	 */
	public int getHeight() {
		return m_height;
	}

	/**
	 * @param height
	 *            - {parameter description}.
	 */
	public void setHeight(final int height) {
		m_height = height;
	}

	/**
	 * @return data - {return content description}
	 */
	public String getData() {
		return m_data;
	}

	/**
	 * @param data
	 *            - {parameter description}.
	 */
	public void setData(final String data) {
		m_data = data;
	}

	/**
	 * @return visibility - {return content description}
	 */
	public boolean isVisibility() {
		return m_visibility;
	}

	/**
	 * @param visibility
	 *            - {parameter description}.
	 */
	public void setVisibility(final boolean visibility) {
		m_visibility = visibility;
	}
}
