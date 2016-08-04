package com.chinal.emp.entity;

import java.util.Date;

/**
  分配历史
*/
public class Fenpeilishi {
	// 分配人编号
	private String fenpeirenCode;
	// 分配人姓名
	private String fenpeirenName;
	// 客户经理编号
	private String kehujingliCode;
	// 客户经理姓名
	private String kehujingliName;
	// 客户身份证号
	private String customerId;
	// 客户名称
	private String customerName;
	// 分配时间
	private String fenpeishijian;

	public void setFenpeirenCode(String fenpeirenCode){
		this.fenpeirenCode = fenpeirenCode;
	}

	public String getFenpeirenCode(){
		return this.fenpeirenCode;
	}

	public void setFenpeirenName(String fenpeirenName){
		this.fenpeirenName = fenpeirenName;
	}

	public String getFenpeirenName(){
		return this.fenpeirenName;
	}

	public void setKehujingliCode(String kehujingliCode){
		this.kehujingliCode = kehujingliCode;
	}

	public String getKehujingliCode(){
		return this.kehujingliCode;
	}

	public void setKehujingliName(String kehujingliName){
		this.kehujingliName = kehujingliName;
	}

	public String getKehujingliName(){
		return this.kehujingliName;
	}

	public void setCustomerId(String customerId){
		this.customerId = customerId;
	}

	public String getCustomerId(){
		return this.customerId;
	}

	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public String getCustomerName(){
		return this.customerName;
	}

	public void setFenpeishijian(String fenpeishijian){
		this.fenpeishijian = fenpeishijian;
	}

	public String getFenpeishijian(){
		return this.fenpeishijian;
	}

}