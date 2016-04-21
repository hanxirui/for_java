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
	// 服务时间
	private String content;
	// 客户经理
	private String account;

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

}