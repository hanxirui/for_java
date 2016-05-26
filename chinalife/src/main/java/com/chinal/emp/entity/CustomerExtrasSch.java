package com.chinal.emp.entity;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.BsgridSearch;

public class CustomerExtrasSch extends BsgridSearch {

	private Integer id;
	private String idcardnum;
	private String phone;
	private String mobile;
	private String carBand;
	private String carNum;
	private String addr;
	private String empcode;
	private String empname;

	private String insertDate;

	public void setId(Integer id) {
		this.id = id;
	}

	@ValueField(column = "id")
	public Integer getId() {
		return this.id;
	}

	public void setIdcardnum(String idcardnum) {
		this.idcardnum = idcardnum;
	}

	@ValueField(column = "idcardnum")
	public String getIdcardnum() {
		return this.idcardnum;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@ValueField(column = "phone")
	public String getPhone() {
		return this.phone;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@ValueField(column = "mobile")
	public String getMobile() {
		return this.mobile;
	}

	public void setCarBand(String carBand) {
		this.carBand = carBand;
	}

	@ValueField(column = "car_band")
	public String getCarBand() {
		return this.carBand;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	@ValueField(column = "car_num")
	public String getCarNum() {
		return this.carNum;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@ValueField(column = "addr")
	public String getAddr() {
		return this.addr;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	@ValueField(column = "empcode")
	public String getEmpcode() {
		return this.empcode;
	}

	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}

	@ValueField(column = "insert_date")
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