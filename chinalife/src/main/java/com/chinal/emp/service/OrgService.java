package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import com.chinal.emp.dao.OrgDao;
import com.chinal.emp.entity.Org;
import org.springframework.stereotype.Service;

@Service
public class OrgService extends CrudService<Org, OrgDao> {

}