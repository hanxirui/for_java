package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class Role {
	// 主键
	private int id;
	// 级别
	private int level;
	// 职务
	private String name;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return this.id;
	}

	public void setLevel(int level){
		this.level = level;
	}

	public int getLevel(){
		return this.level;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}

}