package com.chinal.emp.entity;

import java.util.Date;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.BsgridSearch;

public class LoginrecordSch extends BsgridSearch {

	private Integer id;
	private String empcode;
	private String ip;
	private Date date;

	public void setId(Integer id) {
		this.id = id;
	}

	@ValueField(column = "id")
	public Integer getId() {
		return this.id;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	@ValueField(column = "empcode")
	public String getEmpcode() {
		return this.empcode;
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