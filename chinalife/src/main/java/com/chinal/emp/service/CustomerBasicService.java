package com.chinal.emp.service;

import java.util.List;

import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import com.chinal.emp.dao.CustomerBasicDao;
import com.chinal.emp.entity.CustomerBasic;

@Service
public class CustomerBasicService extends CrudService<CustomerBasic, CustomerBasicDao> {
	public void fenpeiCustomer(String cusIds, String empId) {
		CustomerBasic basic = new CustomerBasic();
		basic.setKehujingli(empId);
		basic.setIdcardnum(cusIds);
		this.getDao().fenpeiCustomer(basic);
	}

	/**
	 * 条件查询
	 * 
	 * @param query
	 * @return
	 */
	public List<CustomerBasic> findVisit(ExpressionQuery query) {
		return this.getDao().findVisit(query);
	}

	/**
	 * 查询总记录数
	 * 
	 * @param query
	 * @return
	 */
	public int findVisitCount(ExpressionQuery query) {
		return this.getDao().findVisitCount(query);
	}

}