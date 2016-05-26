package com.chinal.emp.entity;

/**
  
*/
public class CustomerExtras {
	//
	private int id;
	// 身份证号
	private String idcardnum;
	// 电话
	private String phone;
	// 手机
	private String mobile;
	// 车品牌
	private String carBand;
	// 车牌号
	private String carNum;
	// 住址
	private String addr;
	// 维护人
	private String empcode;
	// 维护人
	private String empname;
	// 维护日期
	private String insertDate;

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

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setCarBand(String carBand) {
		this.carBand = carBand;
	}

	public String getCarBand() {
		return this.carBand;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getCarNum() {
		return this.carNum;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	public String getEmpcode() {
		return this.empcode;
	}

	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}

	public String getInsertDate() {
		return this.insertDate;
	}

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

}