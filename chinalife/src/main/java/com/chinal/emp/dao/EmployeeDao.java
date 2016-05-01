package com.chinal.emp.dao;

import java.util.List;

import org.durcframework.core.dao.BaseDao;
import org.durcframework.core.expression.ExpressionQuery;

import com.chinal.emp.entity.Employee;

public interface EmployeeDao extends BaseDao<Employee> {
	/**
	 * 条件查询
	 * 
	 * @param query
	 * @return
	 */
	List<Employee> findSimple(ExpressionQuery query);

	/**
	 * 条件查询
	 * 
	 * @param query
	 * @return
	 */
	List<Employee> findTree(ExpressionQuery query);
}