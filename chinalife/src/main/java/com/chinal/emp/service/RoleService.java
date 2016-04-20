package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import com.chinal.emp.dao.RoleDao;
import com.chinal.emp.entity.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends CrudService<Role, RoleDao> {

}