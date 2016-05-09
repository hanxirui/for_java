package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class Loginrecord {
	//
	private int id;
	// 账号
	private String empcode;
	// IP地址
	private String ip;
	// 登录时间
	private Date date;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	public String getEmpcode() {
		return this.empcode;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return this.ip;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return this.date;
	}

}