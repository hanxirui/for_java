package com.ez.framework.core.pojo;

import java.util.ArrayList;
import java.util.List;


public class PageDataPojo<T extends IPojo> {

	/**
	 * <code>S_DEFAULT_PAGE_SIZE</code> - default page size.
	 */
	public static final int S_DEFAULT_PAGE_SIZE = 20;

	/**
	 * <code>S_DEFAULT_FIRST_PAGE_INDEX</code> - default first page index.
	 */
	public static final int S_DEFAULT_FIRST_PAGE_INDEX = 1;

	/**
	 * <code>m_datas</code> - page data collection.
	 */
	private List<T> m_datas;

	/**
	 * <code>m_isFirst</code> - true: first page,false:not first page.
	 */
	private boolean m_isFirst;

	/**
	 * <code>m_isLast</code> - true: last page,false:not last page.
	 */
	private boolean m_isLast;

	/**
	 * <code>m_hasNext</code> - true: has next page,false:has not next page.
	 */
	private boolean m_hasNext;

	/**
	 * <code>m_hasPrevious</code> - true: has previous page,false:has not next
	 * previous page.
	 */
	private boolean m_hasPrevious;

	/**
	 * <code>m_isMiddle</code> - true: is middle page,false:is not middle page.
	 */
	private boolean m_isMiddle;

	/**
	 * <code>m_pageIndex</code> - current page index,begin 1.
	 */
	private int m_pageIndex = S_DEFAULT_FIRST_PAGE_INDEX;

	/**
	 * <code>m_pageSize</code> - page size,default page size = 10 .
	 */
	private int m_pageSize = S_DEFAULT_PAGE_SIZE;

	/**
	 * <code>m_recordCount</code> - DB record count.
	 */
	private int m_recordCount;

	/**
	 * <code>m_maxPage</code> - max page index.
	 */
	private int m_maxPage;

	/**
	 * Contructors.
	 */
	public PageDataPojo() {
		m_datas = new ArrayList<T>();
		m_isFirst = false;
		m_isLast = false;
		m_hasNext = false;
		m_hasPrevious = false;
		m_isMiddle = false;
		init();
	}

	/**
	 * Contructors.
	 * 
	 * @param pageSize
	 *            - page size.
	 * @param pageIndex
	 *            - page index,begin 1.
	 * @param resultCount
	 *            - DB select result total count.
	 */
	public PageDataPojo(int pageSize, int pageIndex, int resultCount) {

		super();

		m_datas = new ArrayList<T>();

		if (pageSize > 0) {
			this.m_pageSize = pageSize;
		}
		this.m_recordCount = resultCount;

		if (pageIndex >= 1) {
			this.m_pageIndex = pageIndex;
		}

		init();
	}

	/**
	 * get page data collection.
	 * 
	 * @return page data collection or null.
	 */
	public List<T> getPageData() {
		return this.m_datas;
	}

	/**
	 * set page data collection .
	 * 
	 * @param datas
	 *            - page data collection.
	 */
	public void setPageData(List<T> datas) {

		if (datas != null && !datas.isEmpty()) {

			for (T t_data : datas) {
				this.add(t_data);
			}
		}
	}

	/**
	 * add page data.
	 * 
	 * @param obj
	 *            - page data.
	 * @return true:add success,false:add fail.
	 */
	public boolean add(final T obj) {
		return this.m_datas.add(obj);
	}

	/**
	 * get page data by index.
	 * 
	 * @param index
	 *            - page data index.
	 * @return page data or null.
	 */
	public Object get(final int index) {
		return this.m_datas.get(index);
	}

	/**
	 * check page datas is empty.
	 * 
	 * @return true:empty,false-not empty.
	 */
	public boolean isEmpty() {
		return this.m_datas.isEmpty();
	}

	/**
	 * remove page data from page data collection.
	 * 
	 * @param index
	 *            - page data index.
	 * @return remove page data POJO or null.
	 */
	public Object remove(final int index) {
		return this.m_datas.remove(index);
	}

	/**
	 * remove page data from page data collection.
	 * 
	 * @param obj
	 *            - page data POJO.
	 * @return remove page data POJO or null.
	 */
	public boolean remove(final T obj) {
		return this.m_datas.remove(obj);
	}

	/**
	 * The current page is first page.
	 * 
	 * @return true-is first page,false-is not first page.
	 */
	public boolean isFirst() {
		return this.m_isFirst;
	}

	/**
	 * The current page is last page.
	 * 
	 * @return true-is last page,false-is not last page.
	 */
	public boolean isLast() {
		return this.m_isLast;
	}

	/**
	 * The current page is middle page.
	 * 
	 * @return true - is middle page, false - is not middle page.
	 */
	public boolean isMiddle() {
		return this.m_isMiddle;
	}

	/**
	 * The current page is has next page.
	 * 
	 * @return true-has next page,false-has not next page.
	 */
	public boolean hasNext() {
		return this.m_hasNext;
	}

	/**
	 * The current page is has previous page.
	 * 
	 * @return true - has previous page,false - has not previous page.
	 */
	public boolean hasPrevious() {
		return m_hasPrevious;
	}

	/**
	 * get current page index, page index beging 1.
	 * 
	 * @return current page index
	 */
	public int getPageIndex() {

		if (this.m_pageIndex < 1) {
			return 1;
		} else if (this.m_pageIndex > getMaxPage()) {
			return getMaxPage();
		} else {
			return this.m_pageIndex;
		}
	}

	/**
	 * set current page index,page index beging 1.
	 * 
	 * @param pageIndexPar
	 */
	public void setPageIndex(final int pageIndexPar) {

		if (pageIndexPar >= S_DEFAULT_FIRST_PAGE_INDEX) {
			this.m_pageIndex = pageIndexPar;
		}

		init();
	}

	/**
	 * get page size , default page size is 10.
	 * 
	 * @return page size
	 */
	public int getPageSize() {
		return m_pageSize;
	}

	/**
	 * set page size.
	 * 
	 * @param pageSizePar
	 *            - page size.
	 */
	public void setPageSize(final int pageSizePar) {
		this.m_pageSize = pageSizePar;
		init();
	}

	/**
	 * get current max page index.
	 * 
	 * @return current max page index.
	 */
	public int getMaxPage() {
		/*
		 * maxPage = recordCount / pageSize; if(recordCount % pageSize != 0){
		 * maxPage++; }
		 */
		this.m_maxPage = (this.m_recordCount + this.m_pageSize - 1) / this.m_pageSize;

		return this.m_maxPage;

	}

	/**
	 * get DB record count.
	 * 
	 * @return DB record count or 0.
	 */
	public int getRecordCount() {
		return this.m_recordCount;
	}

	/**
	 * @param recordCountPar
	 *            The recordCount to set.
	 */
	public void setRecordCount(final int recordCountPar) {
		this.m_recordCount = recordCountPar;
		init();
	}

	/**
	 * Calculate start index number for sql server
	 * 
	 * @return
	 */
	public int getMsSqlServerStartIndex() {
		if (this.m_pageIndex == 1) {
			return 0;
		} else {
			return (this.m_pageIndex - 1) * this.m_pageSize;
		}
	}

	/**
	 * Calculate end index number for sql server
	 * 
	 * @return
	 */
	public int getMsSqlServerEndIndex() {
		if (this.m_pageIndex < this.getMaxPage()) {
			return this.m_pageIndex * this.m_pageSize;
		} else {
			return this.getRecordCount();
		}
	}

	/**
	 * calculation page start NUM for MySql DB.
	 * 
	 * @return page start NUM for MySql DB.
	 */
	public int getMysqlLimitIndex() {
		if (this.m_pageIndex == 1) {
			return 0;
		} else {
			int limitIndex = (this.m_pageIndex - 1) * this.m_pageSize;
			if (limitIndex >= this.m_recordCount) {
				return (this.m_maxPage - 1) * this.m_pageSize;
			} else {
				return limitIndex;
			}
		}

	}

	/**
	 * calculation page end NUM for MySql DB.
	 * 
	 * @return page end NUM for MySql DB.
	 */
	public int getMysqlLimitOffset() {
		return m_pageSize;
	}

	/**
	 * calculation page start NUM for Oracle DB.
	 * 
	 * @return page start NUM for Oracle DB.
	 */
	public int getOracleStartIndex() {
		return (this.m_pageIndex - 1) * this.m_pageSize + 1;
	}

	/**
	 * calculation page end NUM for Oracle DB.
	 * 
	 * @return page end NUM for Oracle DB.
	 */
	public int getOracleEndIndex() {

		if (this.m_pageIndex < this.getMaxPage()) {
			return this.m_pageIndex * this.m_pageSize;
		} else {
			return this.getRecordCount();
		}
	}

	/**
	 * initialize
	 */
	private void init() {
		if (getPageIndex() == 1) {
			if (getMaxPage() == 1) {
				// only one page.
				this.m_isFirst = true;
				this.m_isLast = true;
				this.m_hasPrevious = false;
				this.m_hasNext = false;
				this.m_isMiddle = false;
			} else {
				// is first page and have some page.
				this.m_isFirst = true;
				this.m_isLast = false;
				this.m_hasPrevious = false;
				this.m_hasNext = true;
				this.m_isMiddle = false;
			}

		} else if (getPageIndex() == getMaxPage()) {

			// is last page not first page.
			this.m_isFirst = false;
			this.m_isLast = true;
			this.m_hasPrevious = true;
			this.m_hasNext = false;
			this.m_isMiddle = false;

		} else {
			// is middle page.
			this.m_isFirst = false;
			this.m_isLast = false;
			this.m_hasPrevious = true;
			this.m_hasNext = true;
			this.m_isMiddle = true;
		}
	}

}
