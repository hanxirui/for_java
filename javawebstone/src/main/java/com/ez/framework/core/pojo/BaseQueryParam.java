package com.ez.framework.core.pojo;

import java.util.HashMap;
import java.util.Map;

import com.ez.framework.core.dam.DBType;
import com.ez.framework.core.dam.mybatis.DAMFactory;
import com.ez.framework.core.dao.IQueryParam;
import com.ez.framework.core.service.ServiceContainer;

public class BaseQueryParam implements IQueryParam {

	private Map<String, Object> m_customParamers;

	/**
	 * <code>m_pageIndex</code> - query by page index.
	 */
	private int m_pageIndex = PageDataPojo.S_DEFAULT_FIRST_PAGE_INDEX;

	/**
	 * <code>m_pageSize</code> - query page size.
	 */
	private int m_pageSize = PageDataPojo.S_DEFAULT_PAGE_SIZE;

	/**
	 * <code>m_sortColumns</code> - query order by column array.
	 */
	private String[] m_sortColumns;

	/**
	 * <code>m_sortColumn</code> - query order by column array.
	 */
	private String m_sortColumn;

	/**
	 * <code>m_sortType</code> - query order by type:DESC/ASC.
	 */
	private SORT m_sortType;

	/**
	 * <code>m_isNumber</code> - sort column by number.
	 */
	private boolean m_isNumber;

	/**
	 * <code>m_startIndex</code> - page start index.
	 */
	private int m_startIndex;

	/**
	 * <code>m_endIndex</code> - page end index.
	 */
	private int m_endIndex;

	/**
	 * <code>m_pageCountSqlId</code> - page count SQL id.
	 */
	private String m_pageCountSqlId;

	/**
	 * <code>m_pageSqlId</code> - page SQL id.
	 */
	private String m_pageSqlId;

	/**
	 * <code>m_orderBySQL</code> - order by SQL.
	 */
	private String m_orderBySQL;

	private String m_dbType;

	/**
	 * Default Constructors.
	 */
	public BaseQueryParam() {

	}

	/**
	 * Copy Constructors.
	 * 
	 * @param another
	 */
	public BaseQueryParam(final BaseQueryParam another) {

		if (another.m_customParamers != null) {
			m_customParamers = new HashMap<String, Object>();
			m_customParamers.putAll(another.m_customParamers);
		}
		m_pageIndex = another.m_pageIndex;
		m_pageSize = another.m_pageSize;

		if (another.m_sortColumns != null && another.m_sortColumns.length != 0) {
			m_sortColumns = new String[another.m_sortColumns.length];
			System.arraycopy(another.m_sortColumns, 0, m_sortColumns, 0, another.m_sortColumns.length);
		}

		m_sortColumn = another.m_sortColumn;
		m_sortType = another.m_sortType;
		m_isNumber = another.m_isNumber;
		m_startIndex = another.m_startIndex;
		m_endIndex = another.m_endIndex;
		m_pageCountSqlId = another.m_pageCountSqlId;
		m_pageSqlId = another.m_pageSqlId;
		m_orderBySQL = another.m_orderBySQL;
		m_dbType = another.m_dbType;
	}

	public int getPageSize() {
		return this.m_pageSize;
	}

	public int getPageIndex() {
		return this.m_pageIndex;
	}

	public String[] getSortColumn() {

		return this.m_sortColumns;
	}

	public void setPageSize(int pageSize) {

		if (pageSize > 0) {
			this.m_pageSize = pageSize;
		}

	}

	public void setPageIndex(int pageIndex) {

		if (pageIndex > 0) {
			this.m_pageIndex = pageIndex;
		}
	}

	public void setSortColumn(String sortColumn, SORT sortType) {

		this.setSortColumn(sortColumn, sortType, false);

	}

	public void setSortColumn(String sortColumn, SORT sortType, boolean isNumber) {

		this.m_sortColumn = sortColumn;

		this.m_sortType = sortType;

		this.m_isNumber = isNumber;

	}

	public void setSortColumn(String[] sortColumn) {

		this.m_sortColumns = sortColumn;
	}

	/*
	 * (non-Javadoc)
	 * @see com.riil.core.dao.IQueryParam#getSortType()
	 */
	public SORT getSortType() {
		return m_sortType;
	}

	/*
	 * (non-Javadoc)
	 * @see com.riil.core.dao.IQueryParam#setSortType(com.riil.core.dao.IQueryParam.SORT)
	 */
	public void setSortType(SORT m_sortType) {
		this.m_sortType = m_sortType;
	}

	public String getOrderBy() {

		StringBuilder t_builder = null;

		if (null != this.m_sortColumn && this.m_sortColumn.length() > 0) {

			t_builder = new StringBuilder(" ");

			String t_dbType = getDBType();

			if (m_isNumber) {
				if (t_dbType.equalsIgnoreCase(DBType.mysql.toString())) {
					t_builder.append("(" + this.m_sortColumn + "+0)");
				} else if (t_dbType.equalsIgnoreCase(DBType.mssqlserver.toString())) {
					t_builder.append("CAST(" + this.m_sortColumn + " AS BIGINT)");
				} else if (t_dbType.equalsIgnoreCase(DBType.oracle.toString())) {
					t_builder.append("TO_NUMBER (" + this.m_sortColumn + ")");
				}
			} else {
				t_builder.append(this.m_sortColumn);
			}

			t_builder.append(" ").append(getSortKeyword());

		} else if (null != this.m_sortColumns && this.m_sortColumns.length > 0) {

			t_builder = new StringBuilder(" ");

			for (int i = 0; i < this.m_sortColumns.length; i++) {
				if (i == 0) {
					t_builder.append(this.m_sortColumns[i]);
				} else {
					t_builder.append(" ").append(getSortKeyword());
					t_builder.append(", ").append(this.m_sortColumns[i]);
				}
			}

			t_builder.append(" ").append(getSortKeyword());
		}

		if (null != t_builder) {
			this.m_orderBySQL = t_builder.toString();
		}

		return this.m_orderBySQL;
	}

	private String getSortKeyword() {

		String t_sort;

		if (m_sortType == null) {
			t_sort = SORT.ASC.toString();
		} else {
			t_sort = m_sortType.toString();
		}

		return t_sort;
	}

	/**
	 * get page start index.
	 * 
	 * @return page start index.
	 */
	public int getStartIndex() {
		return m_startIndex;
	}

	/**
	 * set page start index.
	 * 
	 * @param startIndex - page start index.
	 */
	public void setStartIndex(int startIndex) {
		m_startIndex = startIndex;
	}

	/**
	 * get page end index.
	 * 
	 * @return page end index.
	 */
	public int getEndIndex() {
		return m_endIndex;
	}

	/**
	 * set page end index.
	 * 
	 * @param endIndex - page end index.
	 */
	public void setEndIndex(int endIndex) {
		m_endIndex = endIndex;
	}

	public void setPageSqlId(String sqlId) {
		this.m_pageSqlId = sqlId;

	}

	public void setPageCountSqlId(String sqlCountId) {
		this.m_pageCountSqlId = sqlCountId;

	}

	public String getPageSqlId() {
		return this.m_pageSqlId;
	}

	public String getPageCountSqlId() {
		return this.m_pageCountSqlId;
	}

	public void setCustomParamers(String key, Object value) {
		if (m_customParamers == null) {
			m_customParamers = new HashMap<String, Object>();
		}

		m_customParamers.put(key, value);
	}

	public Map<String, Object> getCustomParamers() {
		return m_customParamers;
	}

	private String getDBType() {

		if (m_dbType == null) {
			try {
				DAMFactory t_factory = ServiceContainer.getDatabaseComponent(DAMFactory.S_NAME);
				m_dbType = t_factory.getDbType();
			} catch (Throwable t_e) {
				m_dbType = DBType.mysql.toString();
			}
		}

		return m_dbType;
	}

}
