package com.chinal.emp.entity;

/**
  
*/
public class Employee {
	//
	private int id;
	// 姓名
	private String name;
	// 职务
	private int role;
	// 密码
	private String password;
	// 账号
	private String account;
	// 工号
	private String code;
	// 身份证号
	private String pid;
	// 所属公司
	private String orgname;
	// 公司代码
	private String orgcode;
	// 性别
	private String sex;
	// 电话
	private String phone;
	// 入司时间
	private String jointime;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getRole() {
		return this.role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return this.account;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPid() {
		return this.pid;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getOrgname() {
		return this.orgname;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getOrgcode() {
		return this.orgcode;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return this.sex;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setJointime(String jointime) {
		this.jointime = jointime;
	}

	public String getJointime() {
		return this.jointime;
	}

}