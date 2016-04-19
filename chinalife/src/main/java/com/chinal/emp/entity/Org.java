package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class Org {
	// 
	private int id;
	// 名称
	private String name;
	// 类型
	private String type;

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

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return this.type;
	}

}