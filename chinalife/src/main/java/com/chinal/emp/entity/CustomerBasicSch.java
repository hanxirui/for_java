package com.chinal.emp.entity;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.BsgridSearch;

public class CustomerBasicSch extends BsgridSearch {

	private Integer id;
	private String name;
	private String idcardnum;
	private Integer type;
	private String birthday;
	private String weddingDay;
	private String account;
	private String sex;
	private String laiyuan;
	private String note;
	private String addr;

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

	public void setIdcardnum(String idcardnum) {
		this.idcardnum = idcardnum;
	}

	@ValueField(column = "idcardnum")
	public String getIdcardnum() {
		return this.idcardnum;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@ValueField(column = "type")
	public Integer getType() {
		return this.type;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@ValueField(column = "birthday")
	public String getBirthday() {
		return this.birthday;
	}

	public void setWeddingDay(String weddingDay) {
		this.weddingDay = weddingDay;
	}

	@ValueField(column = "wedding_day")
	public String getWeddingDay() {
		return this.weddingDay;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@ValueField(column = "account")
	public String getAccount() {
		return this.account;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@ValueField(column = "sex")
	public String getSex() {
		return this.sex;
	}

	public void setLaiyuan(String laiyuan) {
		this.laiyuan = laiyuan;
	}

	@ValueField(column = "laiyuan")
	public String getLaiyuan() {
		return this.laiyuan;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@ValueField(column = "note")
	public String getNote() {
		return this.note;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

}