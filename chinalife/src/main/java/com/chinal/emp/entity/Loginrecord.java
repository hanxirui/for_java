package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class Loginrecord {
	// 
	private int id;
	// 账号
	private String account;
	// IP地址
	private String ip;
	// 登录时间
	private Date date;

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

	public void setIp(String ip){
		this.ip = ip;
	}

	public String getIp(){
		return this.ip;
	}

	public void setDate(Date date){
		this.date = date;
	}

	public Date getDate(){
		return this.date;
	}

}