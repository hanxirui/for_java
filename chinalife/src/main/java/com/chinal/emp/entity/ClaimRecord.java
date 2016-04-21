package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class ClaimRecord {
	// 
	private int id;
	// 客户身份证号
	private String idcardnum;
	// 投诉原因
	private String reason;
	// 涉及保单
	private String insuranceid;
	// 客户经理
	private String firstaccount;
	// 处理内容
	private String firstcontent;
	// 赔偿金额
	private String firstclaim;
	// 处理时间
	private String firsttime;
	// 区域经理
	private String secondaccount;
	// 处理内容
	private String secondcontent;
	// 处理时间
	private String secondtime;
	// 赔偿金额
	private String secondclaim;
	// 部门经理
	private String thirdaccount;
	// 处理内容
	private String thirdcontent;
	// 赔偿金额
	private String thirdclaim;
	// 处理时间
	private String thirdtime;
	// 经理室
	private String fourthaccount;
	// 处理内容
	private String fourthcontent;
	// 赔偿金额
	private String fourthclaim;
	// 赔偿时间
	private String fourthtime;
	// 投诉时间
	private String claimtime;

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

	public void setReason(String reason){
		this.reason = reason;
	}

	public String getReason(){
		return this.reason;
	}

	public void setInsuranceid(String insuranceid){
		this.insuranceid = insuranceid;
	}

	public String getInsuranceid(){
		return this.insuranceid;
	}

	public void setFirstaccount(String firstaccount){
		this.firstaccount = firstaccount;
	}

	public String getFirstaccount(){
		return this.firstaccount;
	}

	public void setFirstcontent(String firstcontent){
		this.firstcontent = firstcontent;
	}

	public String getFirstcontent(){
		return this.firstcontent;
	}

	public void setFirstclaim(String firstclaim){
		this.firstclaim = firstclaim;
	}

	public String getFirstclaim(){
		return this.firstclaim;
	}

	public void setFirsttime(String firsttime){
		this.firsttime = firsttime;
	}

	public String getFirsttime(){
		return this.firsttime;
	}

	public void setSecondaccount(String secondaccount){
		this.secondaccount = secondaccount;
	}

	public String getSecondaccount(){
		return this.secondaccount;
	}

	public void setSecondcontent(String secondcontent){
		this.secondcontent = secondcontent;
	}

	public String getSecondcontent(){
		return this.secondcontent;
	}

	public void setSecondtime(String secondtime){
		this.secondtime = secondtime;
	}

	public String getSecondtime(){
		return this.secondtime;
	}

	public void setSecondclaim(String secondclaim){
		this.secondclaim = secondclaim;
	}

	public String getSecondclaim(){
		return this.secondclaim;
	}

	public void setThirdaccount(String thirdaccount){
		this.thirdaccount = thirdaccount;
	}

	public String getThirdaccount(){
		return this.thirdaccount;
	}

	public void setThirdcontent(String thirdcontent){
		this.thirdcontent = thirdcontent;
	}

	public String getThirdcontent(){
		return this.thirdcontent;
	}

	public void setThirdclaim(String thirdclaim){
		this.thirdclaim = thirdclaim;
	}

	public String getThirdclaim(){
		return this.thirdclaim;
	}

	public void setThirdtime(String thirdtime){
		this.thirdtime = thirdtime;
	}

	public String getThirdtime(){
		return this.thirdtime;
	}

	public void setFourthaccount(String fourthaccount){
		this.fourthaccount = fourthaccount;
	}

	public String getFourthaccount(){
		return this.fourthaccount;
	}

	public void setFourthcontent(String fourthcontent){
		this.fourthcontent = fourthcontent;
	}

	public String getFourthcontent(){
		return this.fourthcontent;
	}

	public void setFourthclaim(String fourthclaim){
		this.fourthclaim = fourthclaim;
	}

	public String getFourthclaim(){
		return this.fourthclaim;
	}

	public void setFourthtime(String fourthtime){
		this.fourthtime = fourthtime;
	}

	public String getFourthtime(){
		return this.fourthtime;
	}

	public void setClaimtime(String claimtime){
		this.claimtime = claimtime;
	}

	public String getClaimtime(){
		return this.claimtime;
	}

}