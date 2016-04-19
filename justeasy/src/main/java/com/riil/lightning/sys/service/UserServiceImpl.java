package com.riil.lightning.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.riil.lightning.common.service.CrudService;
import com.riil.lightning.sys.dao.UserDao;
import com.riil.lightning.sys.entity.User;

@Service
public class UserServiceImpl extends CrudService<UserDao, User> {

	@Autowired
	private UserDao userDao;
	
	
}
