package com.chinal.emp.entity;

/**
  
*/
public class CustomerBasic {
	//
	private int id;
	// 姓名
	private String name;
	// 身份证号
	private String idcardnum;
	// 类型 1-发放；2-新拓
	private int type = 1;
	// 生日
	private String birthday;
	// 结婚纪念日
	private String weddingDay;
	// 客户经理code
	private String kehujingli;
	// 性别
	private String sex;
	// 初始来源 1 发放 2 自营新拓 3 渠道新拓
	private String laiyuan = "1";
	// 爱好及特点
	private String note;
	// 电话
	private String phone;
	// 地址
	private String addr;
	// 导入人员
	private String daorurenyuan;
	// 类别
	private String leibie = "1";
	// 客户经理
	private String empname;
	// 机构编号
	private String emporgcode;
	// 机构
	private String emporgname;

	private String beizhu;

	// 拜访次数，界面和查询用，持久化不用
	private String vcount;

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

	public void setIdcardnum(String idcardnum) {
		this.idcardnum = idcardnum;
	}

	public String getIdcardnum() {
		return this.idcardnum;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthday() {
		return this.birthday;
	}

	public void setWeddingDay(String weddingDay) {
		this.weddingDay = weddingDay;
	}

	public String getWeddingDay() {
		return this.weddingDay;
	}

	public void setKehujingli(String kehujingli) {
		this.kehujingli = kehujingli;
	}

	public String getKehujingli() {
		return this.kehujingli;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return this.sex;
	}

	public void setLaiyuan(String laiyuan) {
		this.laiyuan = laiyuan;
	}

	public String getLaiyuan() {
		return this.laiyuan;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote() {
		return this.note;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setDaorurenyuan(String daorurenyuan) {
		this.daorurenyuan = daorurenyuan;
	}

	public String getDaorurenyuan() {
		return this.daorurenyuan;
	}

	public void setLeibie(String leibie) {
		this.leibie = leibie;
	}

	public String getLeibie() {
		return this.leibie;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public String getEmpname() {
		return this.empname;
	}

	public void setEmporgcode(String emporgcode) {
		this.emporgcode = emporgcode;
	}

	public String getEmporgcode() {
		return this.emporgcode;
	}

	public void setEmporgname(String emporgname) {
		this.emporgname = emporgname;
	}

	public String getEmporgname() {
		return this.emporgname;
	}

	public String getVcount() {
		return vcount;
	}

	public void setVcount(String vcount) {
		this.vcount = vcount;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

}