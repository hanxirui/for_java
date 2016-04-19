package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import com.chinal.emp.dao.EmployeeDao;
import com.chinal.emp.entity.Employee;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends CrudService<Employee, EmployeeDao> {

}