package com.chinal.emp.entity;

/**
  
*/
public class Bizplatform {
	//
	private int id;
	//
	private String title;
	// 是否制式拜访
	private String zhishibaifang;
	// 彩页
	private String caiye;
	// 开始时间
	private String start;
	// 结束时间
	private String end;
	// 话术
	private String huashu;
	// 技术资料
	private String jishuziliao;
	// 其他
	private String others;
	// 维护人员
	private String empId;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setZhishibaifang(String zhishibaifang) {
		this.zhishibaifang = zhishibaifang;
	}

	public String getZhishibaifang() {
		return this.zhishibaifang;
	}

	public void setCaiye(String caiye) {
		this.caiye = caiye;
	}

	public String getCaiye() {
		return this.caiye;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getStart() {
		return this.start;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getEnd() {
		return this.end;
	}

	public void setHuashu(String huashu) {
		this.huashu = huashu;
	}

	public String getHuashu() {
		return this.huashu;
	}

	public void setJishuziliao(String jishuziliao) {
		this.jishuziliao = jishuziliao;
	}

	public String getJishuziliao() {
		return this.jishuziliao;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getOthers() {
		return this.others;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpId() {
		return this.empId;
	}

}