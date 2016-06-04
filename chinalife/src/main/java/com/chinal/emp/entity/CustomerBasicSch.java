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
	private String kehujingli;
	private String sex;
	private String laiyuan;
	private String note;
	private String phone;
	private String addr;
	private String daorurenyuan;
	private String leibie;
	private String empname;
	private String emporgcode;
	private String emporgname;
	// 拜访次数，界面和查询用，持久化不用
	private String vcount;

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

	public void setKehujingli(String kehujingli) {
		this.kehujingli = kehujingli;
	}

	@ValueField(column = "kehujingli")
	public String getKehujingli() {
		return this.kehujingli;
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

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@ValueField(column = "phone")
	public String getPhone() {
		return this.phone;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@ValueField(column = "addr")
	public String getAddr() {
		return this.addr;
	}

	public void setDaorurenyuan(String daorurenyuan) {
		this.daorurenyuan = daorurenyuan;
	}

	@ValueField(column = "daorurenyuan")
	public String getDaorurenyuan() {
		return this.daorurenyuan;
	}

	public void setLeibie(String leibie) {
		this.leibie = leibie;
	}

	@ValueField(column = "leibie")
	public String getLeibie() {
		return this.leibie;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	@ValueField(column = "empname")
	public String getEmpname() {
		return this.empname;
	}

	public void setEmporgcode(String emporgcode) {
		this.emporgcode = emporgcode;
	}

	@ValueField(column = "emporgcode")
	public String getEmporgcode() {
		return this.emporgcode;
	}

	public void setEmporgname(String emporgname) {
		this.emporgname = emporgname;
	}

	@ValueField(column = "emporgname")
	public String getEmporgname() {
		return this.emporgname;
	}

	public String getVcount() {
		return vcount;
	}

	public void setVcount(String vcount) {
		this.vcount = vcount;
	}

}