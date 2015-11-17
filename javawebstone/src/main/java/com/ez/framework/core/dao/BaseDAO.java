package com.ez.framework.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.springframework.beans.factory.annotation.Autowired;

import com.ez.framework.core.dam.DataSource;
import com.ez.framework.core.dam.IDam;
import com.ez.framework.core.dam.mybatis.DAMFactory;
import com.ez.framework.core.dam.tx.TransactionManager;
import com.ez.framework.core.pojo.IPojo;
import com.ez.framework.core.pojo.PageDataPojo;

public abstract class BaseDAO<T extends IPojo> {

	/**
	 * <code>S_SQLKEY_INSERT</code> - SQL key:insert.
	 */
	protected static final String S_SQLKEY_INSERT = "insert";

	/**
	 * <code>S_SQLKEY_BATCH_INSERT</code> - SQL key:batchInsert.
	 */
	protected static final String S_SQLKEY_BATCH_INSERT = "batchInsert";

	/**
	 * <code>S_SQLKEY_BATCH_UPDATE</code> - SQL key:batchUpdate.
	 */
	protected static final String S_SQLKEY_BATCH_UPDATE = "batchUpdate";

	/**
	 * <code>S_SQLKEY_UPDATE</code> - SQL key:update_by_id.
	 */
	protected static final String S_SQLKEY_UPDATE_BY_ID = "update_by_id";

	/**
	 * <code>S_SQLKEY_DELETEBYID</code> - SQL key:delete_by_id.
	 */
	protected static final String S_SQLKEY_DELETE_BY_ID = "delete_by_id";

	/**
	 * <code>S_SQLKEY_BATCH_DELETE_BY_IDS</code> - SQL key:batch_delete_by_ids.
	 */
	protected static final String S_SQLKEY_BATCH_DELETE_BY_IDS = "batch_delete_by_ids";

	/**
	 * <code>S_SQLKEY_DELETEALL</code> - SQL key:delete_all.
	 */
	protected static final String S_SQLKEY_DELETE_ALL = "delete_all";

	/**
	 * <code>S_SQLKEY_DELETE_BY_QUERY</code> - SQL key:delete_by_query.
	 */
	protected static final String S_SQLKEY_DELETE_BY_QUERY = "delete_by_query";

	/**
	 * <code>S_SQLKEY_SELECT_BY_ID</code> - SQL key:select_by_id.
	 */
	protected static final String S_SQLKEY_SELECT_BY_ID = "select_by_id";

	/**
	 * <code>S_SQLKEY_SELECT_ONE_BY_QUERY</code> - SQL key:select_one_by_query.
	 */
	protected static final String S_SQLKEY_SELECT_ONE_BY_QUERY = "select_one_by_query";

	/**
	 * <code>S_SQLKEY_SELECT_LIST_BY_QUERY</code> - SQL key:select_list_by_query by query.
	 */
	protected static final String S_SQLKEY_SELECT_LIST_BY_QUERY = "select_list_by_query";

	/**
	 * <code>S_SQLKEY_SELECT_PAGELIST_BY_QUERY</code> - SQL key: select_pagelist_by_query.
	 */
	protected static final String S_SQLKEY_SELECT_PAGELIST_BY_QUERY = "select_pagelist_by_query";

	/**
	 * <code>S_SQLKEY_SELECT_PAGELIST_COUNT_BY_QUERY</code> - SQL key: select_pagelist_count_by_query.
	 */
	protected static final String S_SQLKEY_SELECT_PAGELIST_COUNT_BY_QUERY = "select_pagelist_count_by_query";

	/**
	 * <code>S_SQLKEY_SELECTALL</code> - SQL key: select_all.
	 */
	protected static final String S_SQLKEY_SELECT_ALL = "select_all";
	
	/**
	 * <code>S_SELECT_COUNT_BY_QUERY</code> - SQL key: select_count_by_query.
	 */
	protected static final String S_SELECT_COUNT_BY_QUERY = "select_count_by_query";

	protected String className;

	private DataSource m_dataSource = DataSource.none;

	@Autowired
	private DAMFactory ibatisDAMFactory;

	public BaseDAO() {
		className = this.getClass().getName();
	}
	
	/**
	 * @param queryPojo - query condition POJO object.
	 * @return count number
	 */
	public int countByQuery(IQueryParam queryPojo) {
		if (null != queryPojo) {
			Integer countInteger =  (Integer)getDam().selectOne(S_SELECT_COUNT_BY_QUERY, queryPojo);
			return countInteger.intValue();
		}
		return 0;
	}

	/**
	 * insert POJO object to DB.
	 * 
	 * @param pojo - POJO object.
	 * @return POJO object after insert.
	 * @throws DAOException
	 */
	public T doInsertPojo(T pojo) {

		return this.doInsertPojo(pojo, S_SQLKEY_INSERT);

	}

	/**
	 * insert POJO object to DB.
	 * 
	 * @param pojo
	 * @param sqlKey
	 * @return
	 * @throws DAOException
	 */
	public T doInsertPojo(T pojo, String sqlKey) {

		IDam t_dam = getDam();

		return BaseDAOHelp.doInsertPojo(pojo, sqlKey, t_dam);

	}

	/**
	 * insert POJO objects to DB.
	 * 
	 * @param pojos - POJO object list.
	 * @return insert POJO number.
	 * @throws DAOException
	 */
	public int doInsertPojo(Collection<T> pojos) {
		return doInsertPojo(pojos, S_SQLKEY_INSERT);
	}

	/**
	 * insert POJO objects to DB.
	 * 
	 * @param pojo - POJO object list.
	 * @param sqlKey - SQLMap SQL key.
	 * @return insert POJO number.
	 * @throws DAOException
	 */
	public int doInsertPojo(Collection<T> pojos, String sqlKey) {

		IDam t_dam = getDam();

		int t_rtn = -1;

		for (T t_pojo : pojos) {
			BaseDAOHelp.doInsertPojo(t_pojo, sqlKey, t_dam);
			t_rtn++;
		}

		return t_rtn;

	}

	/**
	 * insert POJO objects to DB.
	 * 
	 * @param pojos - POJO object list.
	 * @return insert POJO number.
	 * @throws DAOException
	 */
	public int doBatchInsertPojo(List<T> pojo) {
		return doBatchInsertPojo(pojo, S_SQLKEY_BATCH_INSERT);
	}

	/**
	 * insert POJO objects to DB.
	 * 
	 * @param pojo - POJO object list.
	 * @param sqlKey - SQLMap SQL key.
	 * @return insert POJO number.
	 * @throws DAOException
	 */
	public int doBatchInsertPojo(List<T> pojo, String sqlKey) {

		IDam t_dam = getDam(ExecutorType.BATCH);

		return BaseDAOHelp.doBatchInsertPojo(pojo, sqlKey, t_dam);

	}

	/**
	 * update POJO object.
	 * 
	 * @param pojo - POJO object.
	 * @return POJO object after update.
	 * @throws DAOException
	 */
	public T doUpdatePojo(T pojo) {
		return doUpdatePojo(pojo, S_SQLKEY_UPDATE_BY_ID);
	}

	/**
	 * update POJO object with special SQL mapper key.
	 * 
	 * @param pojo
	 * @param sqlKey
	 * @return
	 * @throws DAOException
	 */
	public T doUpdatePojo(T pojo, String sqlKey) {

		IDam t_dam = getDam();
		BaseDAOHelp.doUpdatePojo(pojo, sqlKey, t_dam);

		return pojo;
	}

	/**
	 * update POJO object list.
	 * 
	 * @param pojos
	 * @return
	 * @throws DAOException
	 */
	public int doUpdatePojo(List<T> pojos) {
		return doUpdatePojo(pojos, S_SQLKEY_BATCH_UPDATE);
	}

	/**
	 * update POJO object list with special SQL mapper key.
	 * 
	 * @param pojos
	 * @param sqlKey
	 * @return
	 * @throws DAOException
	 */
	public int doUpdatePojo(List<T> pojos, String sqlKey) {

		IDam t_dam = getDam();
		return BaseDAOHelp.doBatchUpdatePojo(pojos, sqlKey, t_dam);

	}

	/**
	 * @param pojo
	 * @return
	 * @throws DAOException
	 */
	public int doBatchUpdatePojo(List<T> pojos) {
		return doBatchUpdatePojo(pojos, S_SQLKEY_BATCH_UPDATE);
	}

	/**
	 * {method description}.
	 * 
	 * @param pojos
	 * @param sqlKey
	 * @return
	 * @throws DAOException
	 */
	public int doBatchUpdatePojo(List<T> pojos, String sqlKey) {

		IDam t_dam = getDam(ExecutorType.BATCH);
		return BaseDAOHelp.doBatchUpdatePojo(pojos, sqlKey, t_dam);

	}

	/**
	 * delete POJO object by POJO object ID.
	 * 
	 * @param id - POJO Object id.
	 * @return POJO object id after delete.
	 * @throws DAOException
	 */
	public String doDeleteByID(String id) {

		return this.doDeleteByID(id, S_SQLKEY_DELETE_BY_ID);

	}

	/**
	 * {method description}.
	 * 
	 * @param id
	 * @param sqlKey
	 * @return
	 * @throws DAOException
	 */
	public String doDeleteByID(String id, String sqlKey) {

		IDam t_dam = getDam();

		BaseDAOHelp.doDeleteByID(id, sqlKey, t_dam);

		return id;

	}

	/**
	 * delete POJOs object IDs.
	 * 
	 * @param id - POJO Object id.
	 * @return POJO object id after delete.
	 * @throws DAOException
	 */
	public int doDeleteByIDs(List<String> ids) {
		return doDeleteByIDs(ids, S_SQLKEY_BATCH_DELETE_BY_IDS);
	}

	/**
	 * delete rows in one SQL.
	 * 
	 * @param ids
	 * @param sqlKey
	 * @return
	 * @throws DAOException
	 */
	public int doDeleteByIDs(List<String> ids, String sqlKey) {
		IDam t_dam = getDam();
		return BaseDAOHelp.doBatchDeleteByIDs(ids, sqlKey, t_dam);

	}

	/**
	 * batch delete POJO object by POJOs object ID.
	 * 
	 * @param id - POJO Object id.
	 * @return POJO object id after delete.
	 * @throws DAOException
	 */
	public int doBatchDeleteByIDs(List<String> ids) {
		return doBatchDeleteByIDs(ids, S_SQLKEY_BATCH_DELETE_BY_IDS);
	}

	/**
	 * {method description}.
	 * 
	 * @param ids
	 * @param sqlKey
	 * @return
	 * @throws DAOException
	 */
	public int doBatchDeleteByIDs(List<String> ids, String sqlKey) {

		IDam t_dam = getDam(ExecutorType.BATCH);

		return BaseDAOHelp.doBatchDeleteByIDs(ids, sqlKey, t_dam);

	}

	/**
	 * delete all POJO object.
	 * 
	 * @throws DAOException
	 */
	public void doDeleteAll() {
		doDeleteAll(S_SQLKEY_DELETE_ALL);
	}

	/**
	 * {method description}.
	 * 
	 * @param sqlKey
	 * @throws DAOException
	 */
	public void doDeleteAll(String sqlKey) {
		IDam t_dam = getDam();
		BaseDAOHelp.doDeleteAll(sqlKey, t_dam);

	}

	/**
	 * delete POJO object for query.
	 * 
	 * @param queryPojo - query POJO object.
	 * @throws DAOException
	 */
	public void doDeleteByQuery(IQueryParam queryPojo) {
		doDeleteByQuery(queryPojo, S_SQLKEY_DELETE_BY_QUERY);
	}

	/**
	 * {method description}.
	 * 
	 * @param queryPojo
	 * @param sqlKey
	 * @throws DAOException
	 */
	public void doDeleteByQuery(IQueryParam queryPojo, String sqlKey) {
		IDam t_dam = getDam();
		BaseDAOHelp.doDeleteByQuery(queryPojo, sqlKey, t_dam);

	}

	/**
	 * select POJO object by POJO id.
	 * 
	 * @param pojoID - POJO id.
	 * @return POJO object or null.
	 * @throws DAOException
	 */

	public T doSelectByID(String pojoID) {
		return (T) doSelectByID(pojoID, S_SQLKEY_SELECT_BY_ID);
	}

	/**
	 * {method description}.
	 * 
	 * @param pojoID
	 * @param sqlKey
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public T doSelectByID(String pojoID, String sqlKey) {
		IDam t_dam = getDam();
		return (T) BaseDAOHelp.doSelectByID(pojoID, sqlKey, t_dam);

	}

	/**
	 * select one POJO object by query.
	 * 
	 * @param queryPojo - query condition POJO object.
	 * @return POJO object or null.
	 * @throws DAOException
	 */

	public T doSelectOneByQuery(IQueryParam queryPojo) {
		return (T) doSelectOneByQuery(queryPojo, S_SQLKEY_SELECT_ONE_BY_QUERY);
	}

	/**
	 * {method description}.
	 * 
	 * @param queryPojo
	 * @param sqlId
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public T doSelectOneByQuery(IQueryParam queryPojo, String sqlId) {

		IDam t_dam = getDam();
		return (T) BaseDAOHelp.doSelectOneByQuery(queryPojo, sqlId, t_dam);

	}

	/**
	 * Select all POJO object.
	 * 
	 * @return all POJO object collection or null.
	 * @throws DAOException
	 */
	public List<T> doSelectAll() {
		return doSelectAll(S_SQLKEY_SELECT_ALL);
	}

	/**
	 * {method description}.
	 * 
	 * @param sqlKey
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public List<T> doSelectAll(String sqlKey) {

		IDam t_dam = getDam();
		return (List<T>) BaseDAOHelp.doSelectListByQP(null, sqlKey, t_dam);

	}

	/**
	 * select POJO object collection by query.
	 * 
	 * @param queryPojo - query condition POJO object.
	 * @return POJO object collection or null.
	 * @throws DAOException
	 */
	public List<T> doSelectList(IQueryParam queryPojo) {
		return doSelectList(queryPojo, S_SQLKEY_SELECT_LIST_BY_QUERY);
	}

	/**
	 * {method description}.
	 * 
	 * @param queryPojo
	 * @param sqlKey
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public List<T> doSelectList(IQueryParam queryPojo, String sqlKey) {

		IDam t_dam = getDam();

		return (List<T>) BaseDAOHelp.doSelectListByQP(queryPojo, sqlKey, t_dam);

	}

	/**
	 * {method description}.
	 * 
	 * @param queryMap
	 * @return
	 * @throws DAOException
	 */
	public List<T> doSelectList(Map<?, ?> queryMap) {

		return this.doSelectList(queryMap, S_SQLKEY_SELECT_LIST_BY_QUERY);

	}

	/**
	 * {method description}.
	 * 
	 * @param queryMap
	 * @param sqlKey
	 * @return
	 * @throws DAOException
	 */

	@SuppressWarnings("unchecked")
	public List<T> doSelectList(Map<?, ?> queryMap, String sqlKey) {

		IDam t_dam = getDam();

		return (List<T>) BaseDAOHelp.doSelectListByMap(queryMap, sqlKey, t_dam);

	}

	/**
	 * Pagination search function
	 * 
	 * @param queryPojo pagination search condition All POJOs extend from this class.
	 * @return PageDataPojo pagination result object
	 * @throws DAOException
	 */
	public PageDataPojo<T> doPageSelect(final IQueryParam queryPojo) {
		IDam t_dam = this.getDam();

		return BaseDAOHelp.doPageSelect(queryPojo, t_dam);

	}

	/**
	 * if called TransactionManager.beginTransaction(DataSource), the returned DAM used the data source specified in TX.
	 * if not, will use the product data source.
	 * 
	 * @return DAM instance.
	 * @throws DBException
	 */
	protected IDam getDam() {
		return getDam(ExecutorType.SIMPLE);
	}

	/**
	 * fetch DAM with specified data source.
	 * 
	 * @return DAM instance.
	 * @throws DBException
	 */
	protected IDam getDam(DataSource dataSource) {
		return getDam(dataSource, ExecutorType.SIMPLE);
	}

	/**
	 * 获取iBatis DAM.
	 * 
	 * @return IDam
	 * @throws DBException
	 */
	protected IDam getDam(ExecutorType type) {

		return getDam(getDataSourceFromContext(), type);
	}

	protected IDam getDam(DataSource dataSource, ExecutorType type) {

		return ibatisDAMFactory.getDAM(dataSource, type, className);
	}

	protected DataSource getDataSourceFromContext() {
		DataSource t_dataSource;

		if (m_dataSource == DataSource.none) {
			t_dataSource = DataSource.valueOf(TransactionManager.getDataSource());
		} else {
			t_dataSource = m_dataSource;
		}

		return t_dataSource;
	}

}
