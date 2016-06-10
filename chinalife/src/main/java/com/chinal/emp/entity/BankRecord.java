package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class BankRecord {
	// 
	private int id;
	// 银行名称
	private String bankname;
	// 银行代码
	private String bankcode;
	// 支行
	private String zhihangname;
	// 支行代码
	private String zhihangcode;
	// 网点
	private String wangdianname;
	// 网点代码
	private String wangdiancode;
	// 专管员
	private String mzhuanguanyuan;
	// 专管员代码
	private String mzhuanguanyuancode;
	// 
	private String szhuanguanyuan;
	// 专管员代码
	private String szhuanguanyuancode;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return this.id;
	}

	public void setBankname(String bankname){
		this.bankname = bankname;
	}

	public String getBankname(){
		return this.bankname;
	}

	public void setBankcode(String bankcode){
		this.bankcode = bankcode;
	}

	public String getBankcode(){
		return this.bankcode;
	}

	public void setZhihangname(String zhihangname){
		this.zhihangname = zhihangname;
	}

	public String getZhihangname(){
		return this.zhihangname;
	}

	public void setZhihangcode(String zhihangcode){
		this.zhihangcode = zhihangcode;
	}

	public String getZhihangcode(){
		return this.zhihangcode;
	}

	public void setWangdianname(String wangdianname){
		this.wangdianname = wangdianname;
	}

	public String getWangdianname(){
		return this.wangdianname;
	}

	public void setWangdiancode(String wangdiancode){
		this.wangdiancode = wangdiancode;
	}

	public String getWangdiancode(){
		return this.wangdiancode;
	}

	public void setMzhuanguanyuan(String mzhuanguanyuan){
		this.mzhuanguanyuan = mzhuanguanyuan;
	}

	public String getMzhuanguanyuan(){
		return this.mzhuanguanyuan;
	}

	public void setMzhuanguanyuancode(String mzhuanguanyuancode){
		this.mzhuanguanyuancode = mzhuanguanyuancode;
	}

	public String getMzhuanguanyuancode(){
		return this.mzhuanguanyuancode;
	}

	public void setSzhuanguanyuan(String szhuanguanyuan){
		this.szhuanguanyuan = szhuanguanyuan;
	}

	public String getSzhuanguanyuan(){
		return this.szhuanguanyuan;
	}

	public void setSzhuanguanyuancode(String szhuanguanyuancode){
		this.szhuanguanyuancode = szhuanguanyuancode;
	}

	public String getSzhuanguanyuancode(){
		return this.szhuanguanyuancode;
	}

}