package com.chinal.emp.entity;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.BsgridSearch;

public class EmployeeSch extends BsgridSearch {

	private Integer id;
	private String name;
	private Integer role;
	private String password;
	private String account;
	private String code;
	private String pid;
	private String orgname;
	private String orgcode;
	private String sex;
	private String phone;
	private String jointime;

	public void setId(Integer id) {
		this.id = id;
	}

	@ValueField(column = "id")
	public Integer getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ValueField(column = "name")
	public String getName() {
		return this.name;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	@ValueField(column = "role")
	public Integer getRole() {
		return this.role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ValueField(column = "password")
	public String getPassword() {
		return this.password;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@ValueField(column = "account")
	public String getAccount() {
		return this.account;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ValueField(column = "code")
	public String getCode() {
		return this.code;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@ValueField(column = "pid")
	public String getPid() {
		return this.pid;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	@ValueField(column = "orgname")
	public String getOrgname() {
		return this.orgname;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	@ValueField(column = "orgcode")
	public String getOrgcode() {
		return this.orgcode;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@ValueField(column = "sex")
	public String getSex() {
		return this.sex;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@ValueField(column = "phone")
	public String getPhone() {
		return this.phone;
	}

	public void setJointime(String jointime) {
		this.jointime = jointime;
	}

	@ValueField(column = "jointime")
	public String getJointime() {
		return this.jointime;
	}

}