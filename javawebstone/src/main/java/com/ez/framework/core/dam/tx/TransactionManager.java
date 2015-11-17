package com.ez.framework.core.dam.tx;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ez.framework.core.dam.DataSource;
import com.ez.framework.core.service.ServiceContainer;
import com.ez.framework.core.service.ServiceContainerException;

public final class TransactionManager {

	/**
	 * the data source for default transaction.
	 */
	private static String s_dataSource = DataSource.product.toString();

	/**
	 * 设置系统默认数据 系统默认数据为 product. 该方法只限在测试环境中使用.
	 * 
	 * @param dataSource - 默认数据源名称.
	 */
	public static void setGlobalDataSource(String dataSource) {
		try {
			DataSource t_dataSource = DataSource.valueOf(dataSource);
			s_dataSource = t_dataSource.toString();
		} catch (Throwable t_e) {
			s_dataSource = DataSource.product.toString();
		}
	}

	/**
	 * Constructors.
	 */
	private TransactionManager() {
	}

	private static PlatformTransactionManager getTransactionManager(final String dataSource) {
		try {
			return ServiceContainer.getDatabaseComponent(dataSource + ".transactionManager");
		} catch (ServiceContainerException e) {
			throw new RuntimeException("Can not get TransactionManager Bean.");
		}
	}

	/**
	 * 在默认数据源上开启事务.
	 * 
	 * @throws DBException
	 */
	public static void beginTransaction() {
		beginTransaction(s_dataSource.toString());
	}

	/**
	 * 在指定数据源上开启事务.
	 * 
	 * @param dataSource - 指定数据源名称.
	 * @throws DBException
	 */
	public static void beginTransaction(final String dataSource) {
		TransactionContext txContext = ThreadLocalManager.getInstance().getTransactionContext();
		if (txContext == null) {

			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setName(Thread.currentThread().getName());
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

			PlatformTransactionManager txManager = getTransactionManager(dataSource);
			TransactionStatus txStatus = txManager.getTransaction(def);

			txContext = ThreadLocalManager.getInstance().createTransactionContext();
			txContext.setTransactionStatus(txStatus);
			txContext.setDataSource(dataSource);

			txContext.bindToThread();

		} else {

			txContext.acquireNestedTransaction();
		}

	}

	/**
	 * 提交事务.
	 * 
	 * @throws DBException
	 */
	public static void commitTransaction() {

		TransactionContext txContext = ThreadLocalManager.getInstance().getTransactionContext();
		if (txContext == null) {
			throw new RuntimeException("Call beginTransaction() first, then call commitTransaction().");
		}

		if (txContext.hasNestedTransaction()) {
			txContext.releaseNestedTransaction();
			return;
		}

		try {

			PlatformTransactionManager txManager = getTransactionManager(txContext.getDataSource());
			txManager.commit(txContext.getTransactionStatus());

		} finally {
			ThreadLocalManager.getInstance().clear();
		}

	}

	/**
	 * 回滚事务.
	 * 
	 * @throws DBException
	 */
	public static void rollbackTransaction(){
		TransactionContext txContext = ThreadLocalManager.getInstance().getTransactionContext();
		if (txContext == null) {
			throw new RuntimeException("Call beginTransaction() first, then call rollbackTransaction().");
		}

		if (txContext.hasNestedTransaction()) {
			txContext.releaseNestedTransaction();
			throw new RuntimeException("Nested Transaction Rollback.");
		}

		try {
			PlatformTransactionManager txManager = getTransactionManager(txContext.getDataSource());
			txManager.rollback(txContext.getTransactionStatus());
		} finally {
			ThreadLocalManager.getInstance().clear();
		}
	}

	/**
	 * get data source from TX context.
	 * 
	 * @return data source.
	 */
	public static String getDataSource() {
		TransactionContext txContext = ThreadLocalManager.getInstance().getTransactionContext();
		if (txContext == null) {
			return s_dataSource;
		}

		return txContext.getDataSource();
	}

}
