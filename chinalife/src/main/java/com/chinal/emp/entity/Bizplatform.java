package com.chinal.emp.entity;

import java.util.List;

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
	private String startdate;
	// 结束时间
	private String enddate;
	// 话术
	private String huashu;
	// 技术资料
	private String jishuziliao;
	// 其他
	private String others;
	// 维护人员
	private String empcode;
	// 业务平台所属机构
	private String orgcode;

	private List<String> times;

	private List<String> noons;

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

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getStartdate() {
		return this.startdate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getEnddate() {
		return this.enddate;
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

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	public String getEmpcode() {
		return this.empcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getOrgcode() {
		return this.orgcode;
	}

	public List<String> getTimes() {
		return times;
	}

	public void setTimes(List<String> times) {
		this.times = times;
	}

	public List<String> getNoons() {
		return noons;
	}

	public void setNoons(List<String> noons) {
		this.noons = noons;
	}

}