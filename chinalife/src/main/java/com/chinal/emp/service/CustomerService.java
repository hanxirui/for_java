package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import com.chinal.emp.dao.CustomerDao;
import com.chinal.emp.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends CrudService<Customer, CustomerDao> {

}