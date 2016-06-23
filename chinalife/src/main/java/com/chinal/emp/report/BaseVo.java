package com.chinal.emp.report;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * BaseVO. <br>
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
public class BaseVo {

	/**
	 * <code>id</code> - {description}.
	 */
	@XStreamAlias("id")
	private String m_id;

	/**
	 * <code>name</code> - {description}.
	 */
	@XStreamAlias("name")
	private String m_name;

	/**
	 * Constructors.
	 */
	public BaseVo() {
	}

	/**
	 * @return id - {return content description}
	 */
	public String getId() {
		return m_id;
	}

	/**
	 * @param id
	 *            - {parameter description}.
	 */
	public void setId(final String id) {
		this.m_id = id;
	}

	/**
	 * @return name - {return content description}
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * @param name
	 *            - {parameter description}.
	 */
	public void setName(final String name) {
		this.m_name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
