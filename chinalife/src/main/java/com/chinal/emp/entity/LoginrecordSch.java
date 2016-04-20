package com.chinal.emp.entity;

import java.util.Date;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.BsgridSearch;

public class LoginrecordSch extends BsgridSearch {

	private Integer id;
	private String account;
	private String ip;
	private Date date;

	public void setId(Integer id) {
		this.id = id;
	}

	@ValueField(column = "id")
	public Integer getId() {
		return this.id;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@ValueField(column = "account")
	public String getAccount() {
		return this.account;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@ValueField(column = "ip")
	public String getIp() {
		return this.ip;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@ValueField(column = "date")
	public Date getDate() {
		return this.date;
	}

}