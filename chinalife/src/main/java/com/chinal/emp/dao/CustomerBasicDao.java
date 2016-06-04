package com.chinal.emp.dao;

import java.util.List;

import org.durcframework.core.dao.BaseDao;
import org.durcframework.core.expression.ExpressionQuery;

import com.chinal.emp.entity.CustomerBasic;

public interface CustomerBasicDao extends BaseDao<CustomerBasic> {

	void fenpeiCustomer(CustomerBasic basic);

	/**
	 * 条件查询
	 * 
	 * @param query
	 * @return
	 */
	List<CustomerBasic> findVisit(ExpressionQuery query);

	/**
	 * 查询总记录数
	 * 
	 * @param query
	 * @return
	 */
	int findVisitCount(ExpressionQuery query);
}