package com.chinal.emp.service;

import org.durcframework.core.service.CrudService;
import com.chinal.emp.dao.LoginrecordDao;
import com.chinal.emp.entity.Loginrecord;
import org.springframework.stereotype.Service;

@Service
public class LoginrecordService extends CrudService<Loginrecord, LoginrecordDao> {

}