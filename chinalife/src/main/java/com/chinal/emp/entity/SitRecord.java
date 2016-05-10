package com.chinal.emp.entity;

/**
  
*/
public class SitRecord {
	//
	private int id;
	// 客户经理
	private String empcode;
	// 经理名称
	private String empname;
	// 拜访时间
	private String visittime;
	// 客户
	private String idcardnum;
	// 拜访内容
	private String content;
	// 客户名称
	private String name;
	// 拜访性质
	private String type;

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

	public void setVisittime(String visittime) {
		this.visittime = visittime;
	}

	public String getVisittime() {
		return this.visittime;
	}

	public void setIdcardnum(String idcardnum) {
		this.idcardnum = idcardnum;
	}

	public String getIdcardnum() {
		return this.idcardnum;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

}