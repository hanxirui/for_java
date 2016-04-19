package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class Employee {
	// 
	private int id;
	// 姓名
	private String name;
	// 角色
	private int role;
	// 密码
	private String password;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return this.id;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}

	public void setRole(int role){
		this.role = role;
	}

	public int getRole(){
		return this.role;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return this.password;
	}

}