package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class CustomerBasic {
	// 
	private int id;
	// 姓名
	private String name;
	// 身份证号
	private String idcardnum;
	// 类型  1-原始；2-自营新拓；3-渠道新拓
	private int type;

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

	public void setIdcardnum(String idcardnum){
		this.idcardnum = idcardnum;
	}

	public String getIdcardnum(){
		return this.idcardnum;
	}

	public void setType(int type){
		this.type = type;
	}

	public int getType(){
		return this.type;
	}

}