package com.ez.framework.core.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.ez.framework.core.dam.DBType;
import com.ez.framework.core.dam.IDam;
import com.ez.framework.core.pojo.BaseQueryParam;
import com.ez.framework.core.pojo.IPojo;
import com.ez.framework.core.pojo.PageDataPojo;
import com.mchange.util.AssertException;

public final class BaseDAOHelp {

	/**
	 * <code>S_LOGGER</code> - Logger Object.
	 */
	private static final Logger S_LOGGER = Logger.getLogger(BaseDAOHelp.class);

	/**
	 * Constructors.
	 */
	private BaseDAOHelp() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {method description}.
	 * 
	 * @param <T>
	 * @param pojo
	 * @param sqlKey
	 * @param dam
	 * @return
	 * @throws DAOException
	 * @throws AssertException
	 */
	protected static <T extends IPojo> T doInsertPojo(T pojo, String sqlKey, IDam dam) {

		Assert.notNull(sqlKey, "BaseDAOHelp.doInsertPojo() method parameter sqlKey is null.");

		Assert.notNull(pojo, "BaseDAOHelp.doInsertPojo() parameter - pojo is null .");

		Assert.notNull(dam, "BaseDAOHelp.doInsertPojo() parameter - dam is null .");

		dam.insert(sqlKey, pojo);
		return pojo;

	}

	/**
	 * {method description}.
	 * 
	 * @param <T>
	 * @param pojo
	 * @param sqlKey
	 * @param dam
	 * @return
	 * @throws DAOException
	 * @throws AssertException
	 */
	protected static <T extends IPojo> int doBatchInsertPojo(List<T> pojo, String sqlKey, IDam dam) {

		Assert.notNull(sqlKey, "BaseDAOHelp.doBatchInsertPojo() parameter - sqlKey is null .");

		Assert.notNull(pojo, "BaseDAOHelp.doBatchInsertPojo() parameter - pojo is null .");

		Assert.notNull(dam, "BaseDAOHelp.doBatchInsertPojo() parameter - dam is null .");

		return dam.batchInsert(sqlKey, pojo);

	}

	/**
	 * {method description}.
	 * 
	 * @param <T>
	 * @param pojos
	 * @param sqlKey
	 * @param dam
	 * @return
	 * @throws DAOException
	 */
	protected static <T extends IPojo> int doBatchUpdatePojo(List<T> pojos, String sqlKey, IDam dam) {

		Assert.notNull(sqlKey, "BaseDAOHelp.doBatchUpdatePojo() parameter - sqlKey is null .");

		Assert.notNull(pojos, "BaseDAOHelp.doBatchUpdatePojo() parameter - pojo is null .");

		Assert.notNull(dam, "BaseDAOHelp.doBatchUpdatePojo() parameter - dam is null .");

		int t_rtn = -1;

		for (T pojo : pojos) {
			t_rtn += dam.update(sqlKey, pojo);
		}
		return t_rtn;

	}

	/**
	 * update POJO object.
	 * 
	 * @param pojo - POJO object.
	 * @return POJO object after update.
	 * @throws DAOException
	 */
	protected static <T extends IPojo> T doUpdatePojo(T pojo, String sqlKey, IDam dam) {

		Assert.notNull(sqlKey, "BaseDAOHelp.doUpdatePojo() parameter - sqlKey is null .");

		Assert.notNull(pojo, "BaseDAOHelp.doUpdatePojo() parameter - pojo is null .");

		Assert.notNull(dam, "BaseDAOHelp.doUpdatePojo() parameter - dam is null .");

		dam.update(sqlKey, pojo);

		return pojo;

	}

	/**
	 * {method description}.
	 * 
	 * @param <T>
	 * @param id
	 * @param sqlKey
	 * @param dam
	 * @return
	 * @throws DAOException
	 * @throws AssertException
	 */
	protected static <T extends IPojo> String doDeleteByID(String id, String sqlKey, IDam dam) {

		Assert.notNull(sqlKey, "BaseDAOHelp.doDeleteByID() parameter - sqlKey is null .");

		Assert.notNull(id, "BaseDAOHelp.doDeleteByID() parameter - id is null .");

		Assert.notNull(dam, "BaseDAOHelp.doDeleteByID() parameter - dam is null .");

		dam.update(sqlKey, id);

		return id;

	}

	/**
	 * batch delete POJO object by POJOs object ID.
	 * 
	 * @param id - POJO Object id.
	 * @return POJO object id after delete.
	 * @throws DAOException
	 */
	protected static int doBatchDeleteByIDs(List<String> ids, String sqlKey, IDam dam) {

		Assert.notNull(sqlKey, "BaseDAOHelp.doBatchDeleteByIDs() parameter - sqlKey is null .");

		Assert.notEmpty(ids, "BaseDAOHelp.doBatchDeleteByIDs() parameter - ids is null .");

		Assert.notNull(dam, "BaseDAOHelp.doBatchDeleteByIDs() parameter - dam is null .");

		return dam.batchDeleteByIds(sqlKey, ids);

	}

	/**
	 * delete all POJO object.
	 * 
	 * @throws DAOException
	 */
	protected static int doDeleteAll(String sqlKey, IDam dam) {

		Assert.notNull(sqlKey, "BaseDAOHelp.doBatchDeleteByIDs() parameter - sqlKey is null .");

		Assert.notNull(dam, "BaseDAOHelp.doBatchDeleteByIDs() parameter - dam is null .");

		return dam.delete(sqlKey, null);

	}

	/**
	 * delete POJO object for query.
	 * 
	 * @param queryPojo - query POJO object.
	 * @throws DAOException
	 */
	protected static int doDeleteByQuery(IQueryParam queryPojo, String sqlKey, IDam dam) {

		Assert.notNull(sqlKey, "BaseDAOHelp.doDeleteByQuery() parameter - sqlKey is null .");

		Assert.notNull(queryPojo, "BaseDAOHelp.doDeleteByQuery() parameter - queryPojo is null .");

		Assert.notNull(dam, "BaseDAOHelp.doDeleteByQuery() parameter - dam is null .");

		return dam.update(sqlKey, queryPojo);

	}

	/**
	 * {method description}.
	 * 
	 * @param <T>
	 * @param pojoID
	 * @param sqlKey
	 * @param dam
	 * @return
	 * @throws DAOException
	 * @throws AssertException
	 */
	@SuppressWarnings("unchecked")
	protected static <T extends IPojo> T doSelectByID(String pojoID, String sqlKey, IDam dam) {

		Assert.notNull(sqlKey, "BaseDAOHelp.doSelectByID() parameter - sqlKey is null .");

		Assert.notNull(pojoID, "BaseDAOHelp.doSelectByID() parameter - queryPojo is null .");

		Assert.notNull(dam, "BaseDAOHelp.doSelectByID() parameter - dam is null .");

		return (T) dam.selectOne(sqlKey, pojoID);

	}

	/**
	 * {method description}.
	 * 
	 * @param <T>
	 * @param queryPojo
	 * @param sqlKey
	 * @param dam
	 * @return
	 * @throws DAOException
	 * @throws AssertException
	 */
	@SuppressWarnings("unchecked")
	protected static <T extends IPojo> T doSelectOneByQuery(IQueryParam queryPojo, String sqlKey, IDam dam) {

		Assert.notNull(sqlKey, "BaseDAOHelp.doSelectOneByQuery() parameter - sqlKey is null .");

		Assert.notNull(queryPojo, "BaseDAOHelp.doSelectOneByQuery() parameter - queryPojo is null .");

		Assert.notNull(dam, "BaseDAOHelp.doSelectOneByQuery() parameter - dam is null .");

		return (T) dam.selectOne(sqlKey, queryPojo);

	}

	/**
	 * {method description}.
	 * 
	 * @param <T>
	 * @param sqlKey
	 * @param dam
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends IPojo> List<T> doSelectAll(String sqlKey, IDam dam) {

		Assert.notNull(sqlKey, "BaseDAOHelp.doSelectAll() parameter - sqlKey is null .");

		Assert.notNull(dam, "BaseDAOHelp.doSelectAll() parameter - dam is null .");

		return (List<T>) dam.selectList(sqlKey, null);

	}

	/**
	 * {method description}.
	 * 
	 * @param <T>
	 * @param queryPojo
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	protected static <T extends IPojo> List<T> doSelectListByQP(IQueryParam queryPojo, String sqlKey, IDam dam) {

		Assert.notNull(sqlKey, "BaseDAOHelp.doSelectList() parameter - sqlKey is null .");

		Assert.notNull(dam, "BaseDAOHelp.doSelectList() parameter - dam is null .");

		return (List<T>) dam.selectList(sqlKey, queryPojo);

	}

	/**
	 * {method description}.
	 * 
	 * @param <T>
	 * @param queryMap
	 * @param sqlKey
	 * @param dam
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	protected static <T extends IPojo> List<T> doSelectListByMap(Map<?, ?> queryMap, String sqlKey, IDam dam) {
		Assert.notEmpty(queryMap, "BaseDAOHelp.doSelectList() parameter - queryMap is null .");

		Assert.notNull(sqlKey, "BaseDAOHelp.doSelectList() parameter - sqlKey is null .");

		Assert.notNull(dam, "BaseDAOHelp.doSelectList() parameter - dam is null .");

		return (List<T>) dam.selectList(sqlKey, queryMap);

	}

	/**
	 * {method description}.
	 * 
	 * @param <T>
	 * @param queryPojo
	 * @param sqlKey
	 * @param dam
	 * @return
	 * @throws DAOException
	 */
	protected static <T extends IPojo> PageDataPojo<T> doPageSelect(final IQueryParam queryPojo, IDam dam) {
		Assert.notNull(queryPojo, "BaseDAOHelp.doPageSelect() parameter - queryPojo is null .");

		Assert.notNull(dam, "BaseDAOHelp.doPageSelect() parameter - dam is null .");

		int t_count = (Integer) dam.selectOne(queryPojo.getPageCountSqlId(), queryPojo);

		if (t_count == 0) {
			if (S_LOGGER.isDebugEnabled()) {
				S_LOGGER.debug("doSelectPageList count is 0.");
			}
			PageDataPojo<T> pageDataPojo = new PageDataPojo<T>(queryPojo.getPageSize(), queryPojo.getPageIndex(), 0);
			return pageDataPojo;
		}

		PageDataPojo<T> pageDataPojo = new PageDataPojo<T>(queryPojo.getPageSize(), queryPojo.getPageIndex(), t_count);

		BaseQueryParam t_tmpAbsPojo = (BaseQueryParam) queryPojo;

		if (dam.getDBType().equalsIgnoreCase(DBType.mysql.toString())) {
			t_tmpAbsPojo.setStartIndex(pageDataPojo.getMysqlLimitIndex());
			t_tmpAbsPojo.setEndIndex(pageDataPojo.getMysqlLimitOffset());
		} else if (dam.getDBType().equalsIgnoreCase(DBType.oracle.toString())) {
			t_tmpAbsPojo.setStartIndex(pageDataPojo.getOracleStartIndex());
			t_tmpAbsPojo.setEndIndex(pageDataPojo.getOracleEndIndex());
		} else if (dam.getDBType().equalsIgnoreCase(DBType.mssqlserver.toString())) {
			t_tmpAbsPojo.setStartIndex(pageDataPojo.getMsSqlServerStartIndex());
			t_tmpAbsPojo.setEndIndex(pageDataPojo.getMsSqlServerEndIndex());
		} else {
			throw new RuntimeException("Pageable query does not support " + dam.getDBType() + " database.");
		}

		@SuppressWarnings("unchecked")
		List<T> tmpList = (List<T>) dam.selectList(queryPojo.getPageSqlId(), t_tmpAbsPojo);
		pageDataPojo.setPageData(tmpList);

		return pageDataPojo;

	}

}
