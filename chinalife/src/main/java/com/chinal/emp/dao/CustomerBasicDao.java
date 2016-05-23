package com.chinal.emp.dao;

import org.durcframework.core.dao.BaseDao;

import com.chinal.emp.entity.CustomerBasic;

public interface CustomerBasicDao extends BaseDao<CustomerBasic> {

	void fenpeiCustomer(CustomerBasic basic);
}