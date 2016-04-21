package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class InsuranceRecord {
	// 
	private int id;
	// 机构
	private String orgCode;
	// 保险单号
	private String insuranceNum;
	// 投保单号
	private String touInsuranceNum;
	// 险种代码
	private String typeCode;
	// 渠道
	private String qudao;
	// 保费
	private String insuranceFei;
	// 满期金额
	private String totalInsuranceFei;
	// 满期日期
	private String manqiDate;
	// 缴费方式
	private String feiType;
	// 缴费期间
	private String feiQijian;
	// 保险期间
	private String insuranceQijian;
	// 保单状态
	private String state;
	// 投保人
	private String customerIdcardnum;
	// 地址
	private String customerAddr;
	// 电话
	private String customerPhone;
	// 手机
	private String customerMobile;
	// 业务员姓名
	private String account;
	// 工号
	private String accountCode;
	// 银行
	private String bankName;
	// 银行账号
	private String bankCardNum;
	// 被保险人
	private String beibaoxianren;
	// 受益人
	private String shouyiren;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return this.id;
	}

	public void setOrgCode(String orgCode){
		this.orgCode = orgCode;
	}

	public String getOrgCode(){
		return this.orgCode;
	}

	public void setInsuranceNum(String insuranceNum){
		this.insuranceNum = insuranceNum;
	}

	public String getInsuranceNum(){
		return this.insuranceNum;
	}

	public void setTouInsuranceNum(String touInsuranceNum){
		this.touInsuranceNum = touInsuranceNum;
	}

	public String getTouInsuranceNum(){
		return this.touInsuranceNum;
	}

	public void setTypeCode(String typeCode){
		this.typeCode = typeCode;
	}

	public String getTypeCode(){
		return this.typeCode;
	}

	public void setQudao(String qudao){
		this.qudao = qudao;
	}

	public String getQudao(){
		return this.qudao;
	}

	public void setInsuranceFei(String insuranceFei){
		this.insuranceFei = insuranceFei;
	}

	public String getInsuranceFei(){
		return this.insuranceFei;
	}

	public void setTotalInsuranceFei(String totalInsuranceFei){
		this.totalInsuranceFei = totalInsuranceFei;
	}

	public String getTotalInsuranceFei(){
		return this.totalInsuranceFei;
	}

	public void setManqiDate(String manqiDate){
		this.manqiDate = manqiDate;
	}

	public String getManqiDate(){
		return this.manqiDate;
	}

	public void setFeiType(String feiType){
		this.feiType = feiType;
	}

	public String getFeiType(){
		return this.feiType;
	}

	public void setFeiQijian(String feiQijian){
		this.feiQijian = feiQijian;
	}

	public String getFeiQijian(){
		return this.feiQijian;
	}

	public void setInsuranceQijian(String insuranceQijian){
		this.insuranceQijian = insuranceQijian;
	}

	public String getInsuranceQijian(){
		return this.insuranceQijian;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return this.state;
	}

	public void setCustomerIdcardnum(String customerIdcardnum){
		this.customerIdcardnum = customerIdcardnum;
	}

	public String getCustomerIdcardnum(){
		return this.customerIdcardnum;
	}

	public void setCustomerAddr(String customerAddr){
		this.customerAddr = customerAddr;
	}

	public String getCustomerAddr(){
		return this.customerAddr;
	}

	public void setCustomerPhone(String customerPhone){
		this.customerPhone = customerPhone;
	}

	public String getCustomerPhone(){
		return this.customerPhone;
	}

	public void setCustomerMobile(String customerMobile){
		this.customerMobile = customerMobile;
	}

	public String getCustomerMobile(){
		return this.customerMobile;
	}

	public void setAccount(String account){
		this.account = account;
	}

	public String getAccount(){
		return this.account;
	}

	public void setAccountCode(String accountCode){
		this.accountCode = accountCode;
	}

	public String getAccountCode(){
		return this.accountCode;
	}

	public void setBankName(String bankName){
		this.bankName = bankName;
	}

	public String getBankName(){
		return this.bankName;
	}

	public void setBankCardNum(String bankCardNum){
		this.bankCardNum = bankCardNum;
	}

	public String getBankCardNum(){
		return this.bankCardNum;
	}

	public void setBeibaoxianren(String beibaoxianren){
		this.beibaoxianren = beibaoxianren;
	}

	public String getBeibaoxianren(){
		return this.beibaoxianren;
	}

	public void setShouyiren(String shouyiren){
		this.shouyiren = shouyiren;
	}

	public String getShouyiren(){
		return this.shouyiren;
	}

}