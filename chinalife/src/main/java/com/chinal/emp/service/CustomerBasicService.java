package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import com.chinal.emp.dao.CustomerBasicDao;
import com.chinal.emp.entity.CustomerBasic;
import org.springframework.stereotype.Service;

@Service
public class CustomerBasicService extends CrudService<CustomerBasic, CustomerBasicDao> {

}