package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import com.chinal.emp.dao.CustomerExtrasDao;
import com.chinal.emp.entity.CustomerExtras;
import org.springframework.stereotype.Service;

@Service
public class CustomerExtrasService extends CrudService<CustomerExtras, CustomerExtrasDao> {

}