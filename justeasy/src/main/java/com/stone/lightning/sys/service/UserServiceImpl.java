package com.stone.lightning.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stone.lightning.common.service.CrudService;
import com.stone.lightning.sys.dao.UserDao;
import com.stone.lightning.sys.entity.User;

@Service
public class UserServiceImpl extends CrudService<UserDao, User> {

	@Autowired
	private UserDao userDao;
	
	
}
