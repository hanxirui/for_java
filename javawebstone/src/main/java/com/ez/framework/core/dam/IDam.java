package com.ez.framework.core.dam;

import java.util.List;

public interface IDam {
	
	int DEFAULT_BATCH_SIZE = 1000;

	/**
	 * MyBatis SQL mapping 名字空间.
	 * DAO中, 默认值为 getClass().getName()
	 * @param nameSpace
	 */
	void setSqlMapperNameSpace(String nameSpace);

	/**
	 * 查询单条数据.
	 * 如果返回结果为多条数据将抛出异常.
	 * @param statement
	 * @param parameter
	 * @return
	 * @throws DBException.
	 */
	Object selectOne(String statement, Object parameter);

	/**
	 * 查询多条数据.
	 * @param statement
	 * @param parameter
	 * @return
	 * @throws DBException.
	 */
	@SuppressWarnings("rawtypes")
	List selectList(String statement, Object parameter);

	/**
	 * 插入语句.
	 * @param statement
	 * @param parameter
	 * @return
	 * @throws DBException.
	 */
	int insert(String statement, Object parameter);

	/**
	 * 更新语句.
	 * @param statement
	 * @param parameter
	 * @return
	 * @throws DBException.
	 */
	int update(String statement, Object parameter);

	/**
	 * 删除语句.
	 * @param statement
	 * @param parameter
	 * @return
	 * @throws DBException.
	 */
	int delete(String statement, Object parameter);

	/**
	 * 返回当前连接的数据库
	 * @return @see DBType
	 * @throws DBException.
	 */
	String getDBType();

	/**
	 * 使用Statement.addBatch(SQL) 和  Statement.executeBatch()
	 * 执行插入语句.
	 * 注意: BATCH模式不能与SIMPLE模式在同一事务中使用.
	 * @param statement
	 * @param parameter
	 * @return
	 * @throws DBException
	 */
	int batchInsert(String statement, Object parameter);
	
	/**
	 * 调用batchInsert()方法可以插入的最大条数.
	 * @return 返回值小于0, 则表示无限制.
	 */
	int getBatchInsertMaxSize();

	/**
	 * 使用Statement.addBatch(SQL) 和  Statement.executeBatch()
	 * 执行删除语句.
	 * 注意: BATCH模式不能与SIMPLE模式在同一事务中使用.
	 * @param statement
	 * @param ids
	 * @return
	 * @throws DBException
	 */
	int batchDeleteByIds(String statement, Object ids);

}
