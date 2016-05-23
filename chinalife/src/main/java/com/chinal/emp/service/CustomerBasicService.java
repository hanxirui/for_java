package com.chinal.emp.service;

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
}