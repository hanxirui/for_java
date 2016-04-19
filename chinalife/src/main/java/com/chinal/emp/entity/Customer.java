package com.chinal.emp.entity;

/**
  
*/
public class Customer {
	//
	private int id;
	// 姓名
	private String name;
	// 手机
	private String phone;
	// 地址
	private String address;
	// 购买保险类型
	private int type;
	//
	private int fuzeren;
	//
	private int tuijianren;

	private String fuzerenName;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public void setFuzeren(int fuzeren) {
		this.fuzeren = fuzeren;
	}

	public int getFuzeren() {
		return this.fuzeren;
	}

	public void setTuijianren(int tuijianren) {
		this.tuijianren = tuijianren;
	}

	public int getTuijianren() {
		return this.tuijianren;
	}

	public String getFuzerenName() {
		return fuzerenName;
	}

	public void setFuzerenName(String fuzerenName) {
		this.fuzerenName = fuzerenName;
	}

}