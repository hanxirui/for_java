package com.chinal.emp.report.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * PortletVo<br>
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
@XStreamAlias("Portlet")
public class PortletVo extends BaseVo implements Cloneable, Comparable<PortletVo> {

	/**
	 * <code>desc</code> - {description}.
	 */
	@XStreamAlias("desc")
	private String m_desc;
	/**
	 * <code></code> - {description}.
	 */
	@XStreamAlias("x")
	private int m_x;

	/**
	 * <code>y</code> - {description}.
	 */
	@XStreamAlias("y")
	private int m_y;

	/**
	 * <code>width</code> - {description}.
	 */
	@XStreamAlias("width")
	private int m_width;

	/**
	 * <code>height</code> - {description}.
	 */
	@XStreamAlias("height")
	private int m_height;

	/**
	 * <code>vm</code> - {description}.
	 */
	@XStreamAlias("vm")
	private String m_vm;
	/**
	 * <code>vm</code> - {description}.
	 */
	@XStreamAlias("action")
	private String m_action;

	/**
	 * <code>data</code> - {description}.
	 */
	@XStreamAlias("data")
	private String m_data;

	/**
	 * <code>visibility</code> - {description}.
	 */
	@XStreamAlias("visibility")
	private boolean m_visibility;

	/**
	 * <code>order</code> - {description}.
	 */
	@XStreamAlias("order")
	private int m_order;

	/**
	 * <code>overrideData</code> - {description}.
	 */
	@XStreamAlias("overrideData")
	private boolean m_overrideData;

	/**
	 * <code>m_dataSource</code> - 综合展示数据源.
	 */
	@XStreamAlias("dataSource")
	private String m_dataSource;

	/**
	 * <code>buttons</code> - {description}.
	 */
	@XStreamAlias("buttons")
	private List<ButtonVo> m_buttons = new ArrayList<ButtonVo>();

	/**
	 * @return desc - {return content description}
	 */
	public String getDesc() {
		return m_desc;
	}

	/**
	 * @param desc
	 *            - {parameter description}.
	 */
	public void setDesc(final String desc) {
		m_desc = desc;
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
	 * @return vm - {return content description}
	 */
	public String getVm() {
		return m_vm;
	}

	/**
	 * @param vm
	 *            - {parameter description}.
	 */
	public void setVm(final String vm) {
		m_vm = vm;
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

	/**
	 * @return order - {return content description}
	 */
	public int getOrder() {
		return m_order;
	}

	/**
	 * @param order
	 *            - {parameter description}.
	 */
	public void setOrder(final int order) {
		m_order = order;
	}

	/**
	 * @return buttons - {return content description}
	 */
	public List<ButtonVo> getButtons() {
		return m_buttons;
	}

	/**
	 * @param buttons
	 *            - {parameter description}.
	 */
	public void setButtons(final List<ButtonVo> buttons) {
		m_buttons = buttons;
	}

	/**
	 * @return overrideData - {return content description}
	 */
	public boolean isOverrideData() {
		return m_overrideData;
	}

	/**
	 * @param overrideData
	 *            - {parameter description}.
	 */
	public void setOverrideData(final boolean overrideData) {
		this.m_overrideData = overrideData;
	}

	/**
	 * @return dataSource - {return content description}
	 */
	public final String getDataSource() {
		return m_dataSource;
	}

	/**
	 * @param dataSource
	 *            - {parameter description}.
	 */
	public final void setDataSource(final String dataSource) {
		m_dataSource = dataSource;
	}

	/**
	 * {method description}.
	 * 
	 * @return PortletVo
	 */
	public PortletVo clonePortlet() {
		PortletVo cloneObj;
		try {
			cloneObj = (PortletVo) super.clone();
		} catch (CloneNotSupportedException e) {
			cloneObj = null;
		}
		return cloneObj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final PortletVo other) {
		// 这里添加排序目的是把添加占位用的portlet总是放到最后
		return this.getOrder() - other.getOrder();
	}

}
