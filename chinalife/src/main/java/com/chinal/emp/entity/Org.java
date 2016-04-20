package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class Org {
	// 
	private int id;
	// 名称
	private String name;
	// 机构号
	private String code;

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

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return this.code;
	}

}