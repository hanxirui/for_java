package com.chinal.emp.entity;

import java.util.Date;

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
	// 生日
	private String birthday;
	// 车品牌
	private String carBand;
	// 车牌号
	private String carNum;
	// 结婚纪念日
	private String weddingDay;
	// 特点及爱好
	private String note;
	// 维护人
	private String account;
	// 维护日期
	private String insertDate;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return this.id;
	}

	public void setIdcardnum(String idcardnum){
		this.idcardnum = idcardnum;
	}

	public String getIdcardnum(){
		return this.idcardnum;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return this.phone;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return this.mobile;
	}

	public void setBirthday(String birthday){
		this.birthday = birthday;
	}

	public String getBirthday(){
		return this.birthday;
	}

	public void setCarBand(String carBand){
		this.carBand = carBand;
	}

	public String getCarBand(){
		return this.carBand;
	}

	public void setCarNum(String carNum){
		this.carNum = carNum;
	}

	public String getCarNum(){
		return this.carNum;
	}

	public void setWeddingDay(String weddingDay){
		this.weddingDay = weddingDay;
	}

	public String getWeddingDay(){
		return this.weddingDay;
	}

	public void setNote(String note){
		this.note = note;
	}

	public String getNote(){
		return this.note;
	}

	public void setAccount(String account){
		this.account = account;
	}

	public String getAccount(){
		return this.account;
	}

	public void setInsertDate(String insertDate){
		this.insertDate = insertDate;
	}

	public String getInsertDate(){
		return this.insertDate;
	}

}