package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class SitRecord {
	// 
	private int id;
	// 客户经理
	private String account;
	// 拜访时间
	private String visittime;
	// 客户
	private String idcardnum;
	// 拜访内容
	private String content;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return this.id;
	}

	public void setAccount(String account){
		this.account = account;
	}

	public String getAccount(){
		return this.account;
	}

	public void setVisittime(String visittime){
		this.visittime = visittime;
	}

	public String getVisittime(){
		return this.visittime;
	}

	public void setIdcardnum(String idcardnum){
		this.idcardnum = idcardnum;
	}

	public String getIdcardnum(){
		return this.idcardnum;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return this.content;
	}

}