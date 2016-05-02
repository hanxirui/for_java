package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class ServiceRecord {
	// 
	private int id;
	// 客户
	private String idcardnum;
	// 服务时间
	private String servicetime;
	// 服务内容
	private String content;
	// 客户经理
	private String account;
	// 客户名称
	private String name;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return this.id;
	}

	public void setIdcardnum(String idcardnum){
		this.idcardnum = idcardnum;
	}

	public String getIdcardnum(){
		return this.idcardnum;
	}

	public void setServicetime(String servicetime){
		this.servicetime = servicetime;
	}

	public String getServicetime(){
		return this.servicetime;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return this.content;
	}

	public void setAccount(String account){
		this.account = account;
	}

	public String getAccount(){
		return this.account;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}

}