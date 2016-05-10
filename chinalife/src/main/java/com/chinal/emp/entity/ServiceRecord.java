package com.chinal.emp.entity;

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
	private String empcode;
	// 客户名称
	private String name;
	// 经理名称
	private String empname;
	// 拜访性质
	private String type;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setIdcardnum(String idcardnum) {
		this.idcardnum = idcardnum;
	}

	public String getIdcardnum() {
		return this.idcardnum;
	}

	public void setServicetime(String servicetime) {
		this.servicetime = servicetime;
	}

	public String getServicetime() {
		return this.servicetime;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	public String getEmpcode() {
		return this.empcode;
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