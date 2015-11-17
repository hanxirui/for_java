package com.ez.framework.core.dao;

public interface IQueryParam {
	

    static enum SORT {
        ASC, DESC
    };

    /**
     * set page SQL id.
     * @param sqlId - SQL ID.
     */
    void setPageSqlId(String sqlId);

    /**
     * @param sqlCountId
     */
    void setPageCountSqlId(String sqlCountId);

    /**
     * @return
     */
    String getPageSqlId();

    /**
     * @return
     */
    String getPageCountSqlId();

    /**
     * get page size.
     * 
     * @return page size.
     */
    int getPageSize();

    /**
     * set page size .
     * 
     * @param pageSize
     *            - page size,page size > 0.
     */
    void setPageSize(int pageSize);

    /**
     * get page index.
     * 
     * @return page index,first page index is 1.
     */
    int getPageIndex();

    /**
     * set page index.
     * 
     * @param pageIndex
     *            - page index, page index > 0 and first page index is 1.
     */
    void setPageIndex(int pageIndex);
    
    /**
     * get order by column.
     * 
     * @return order by column.
     */
    String[] getSortColumn();
    
    /**
     * set SQL sort column and sort type.
     * @param sortColumn - sort column.
     * @param sortType - sort type.
     * @param isNumber - 是否按照数值方式排序.
     */
    void setSortColumn(String sortColumn,SORT sortType,boolean isNumber);
    
    /**
     * set SQL sort column and sort type.
     * @param sortColumn - sort column.
     * @param sortType - sort type.
     */
    void setSortColumn(String sortColumn,SORT sortType);
    
    /**
     * get SQL order by type.
     * @return SQL order by type.
     */
    SORT getSortType();


	/**
	 * set SQL order by type.
	 * @param m_sortType - SQL order by type.
	 */
	void setSortType(SORT m_sortType);
	
    /**
     * set order by column.
     * 
     * @param sortColumn
     *            - sort column.
     */
    void setSortColumn(String[] sortColumn);

    /**
     * get SQL 'order by...' string.
     * 
     * @return SQL 'order by...' string.
     */
    String getOrderBy();

    /**
     * set a custom property as parameter 
     * @param key
     * @param value
     */
    void setCustomParamers(String key, Object value);

}
