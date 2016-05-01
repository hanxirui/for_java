package com.chinal.emp.service;

import java.util.Collections;
import java.util.List;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import com.chinal.emp.dao.EmployeeDao;
import com.chinal.emp.entity.Employee;

@Service
public class EmployeeService extends CrudService<Employee, EmployeeDao> {
	/**
	 * 条件查询
	 * 
	 * @param query
	 * @return
	 */
	public List<Employee> findSimple(ExpressionQuery query) {
		List<Employee> list = this.getDao().findSimple(query);
		if (list == null) {
			list = Collections.emptyList();
		}
		return list;
	}

	/**
	 * 条件查询
	 * 
	 * @param query
	 * @return
	 */
	public List<Employee> findTree(ExpressionQuery query) {
		List<Employee> list = this.getDao().findTree(query);
		if (list == null) {
			list = Collections.emptyList();
		}
		return list;
	}
}