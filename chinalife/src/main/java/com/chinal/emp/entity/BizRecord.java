package com.chinal.emp.entity;

import java.util.Date;

/**
  
*/
public class BizRecord {
	// 
	private int id;
	// 邀约客户数
	private String yaoyueNum;
	// 到会客户数
	private String daohuiNum;
	// 当日回收件数
	private String receiveNum;
	// 当日回收保费
	private String receiveBaofei;
	// 到会率
	private String daohuilv;
	// 签单件数
	private String qiandanNum;
	// 签单率
	private String qiandanlv;
	// 回收率
	private String huishoulv;
	// 客户经理
	private String kehujingli;
	// 当日签单保费
	private String qiandanBaofei;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return this.id;
	}

	public void setYaoyueNum(String yaoyueNum){
		this.yaoyueNum = yaoyueNum;
	}

	public String getYaoyueNum(){
		return this.yaoyueNum;
	}

	public void setDaohuiNum(String daohuiNum){
		this.daohuiNum = daohuiNum;
	}

	public String getDaohuiNum(){
		return this.daohuiNum;
	}

	public void setReceiveNum(String receiveNum){
		this.receiveNum = receiveNum;
	}

	public String getReceiveNum(){
		return this.receiveNum;
	}

	public void setReceiveBaofei(String receiveBaofei){
		this.receiveBaofei = receiveBaofei;
	}

	public String getReceiveBaofei(){
		return this.receiveBaofei;
	}

	public void setDaohuilv(String daohuilv){
		this.daohuilv = daohuilv;
	}

	public String getDaohuilv(){
		return this.daohuilv;
	}

	public void setQiandanNum(String qiandanNum){
		this.qiandanNum = qiandanNum;
	}

	public String getQiandanNum(){
		return this.qiandanNum;
	}

	public void setQiandanlv(String qiandanlv){
		this.qiandanlv = qiandanlv;
	}

	public String getQiandanlv(){
		return this.qiandanlv;
	}

	public void setHuishoulv(String huishoulv){
		this.huishoulv = huishoulv;
	}

	public String getHuishoulv(){
		return this.huishoulv;
	}

	public void setKehujingli(String kehujingli){
		this.kehujingli = kehujingli;
	}

	public String getKehujingli(){
		return this.kehujingli;
	}

	public void setQiandanBaofei(String qiandanBaofei){
		this.qiandanBaofei = qiandanBaofei;
	}

	public String getQiandanBaofei(){
		return this.qiandanBaofei;
	}

}