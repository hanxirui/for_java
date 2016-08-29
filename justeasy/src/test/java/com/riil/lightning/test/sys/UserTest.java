package com.stone.lightning.test.sys;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.stone.lightning.common.persistence.Page;
import com.stone.lightning.sys.entity.User;
import com.stone.lightning.sys.service.SystemServiceImpl;
import com.stone.lightning.sys.service.UserServiceImpl;
import com.stone.lightning.test.BaseTest;

public class UserTest extends BaseTest {

	UserServiceImpl service;
	@Test
	public void findUserTest(){
		service = ctx.getBean(UserServiceImpl.class);
		List<User> userList = service.findList(new User());
		Assert.assertEquals(1, userList.size());
	}
	@Test
	public void createUserTest(){
		service = ctx.getBean(UserServiceImpl.class);
		User user = new User();
		user.setIsNewRecord(true);
		user.setName("系统管理员1");
		user.setLoginName("admin");
		user.setPassword(SystemServiceImpl.entryptPassword("123456"));
		
		service.save(user);
		
	}
	@Test
	public void saveUserTest(){
		service = ctx.getBean(UserServiceImpl.class);
		
		User user = service.selectOne("59c23637f78d43c1a9e92c5ea623fc47");
		user.setLoginIp("127.0.0.1");
		user.setLoginDate(new Date());
		
//		User user = new User();
//		user.setId("59c23637f78d43c1a9e92c5ea623fc47");
//		user.setIsNewRecord(true);
//		user.setName("系统管理员1");
//		user.setLoginName("admin");
//		user.setPassword("123456");
		
		service.save(user);
	}
	@Test
	public void findPageListTest(){
		service = ctx.getBean(UserServiceImpl.class);
		Page<User> page = new Page<>();
		page.setPageSize(20);
		page.setPageNo(1);
		
		User user = new User();
		user.setLoginFlag("1");
		
		Page<User> result = service.findPage(page, user);
	}
}
