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
	// 类型 1-原始；2-自营新拓；3-渠道新拓
	private int type;
	// 生日
	private String birthday;
	// 结婚纪念日
	private String weddingDay;
	// 客户经理
	private String kehujingli;
	// 性别
	private String sex;
	// 初始来源
	private String laiyuan;
	// 爱好及特点
	private String note;
	// 
	private String phone;
	// 
	private String addr;
	// 导入人员
	private String daorurenyuan;
	// 类别
	private String leibie;

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

	public void setLaiyuan(String laiyuan){
		this.laiyuan = laiyuan;
	}

	public String getLaiyuan(){
		return this.laiyuan;
	}

	public void setNote(String note){
		this.note = note;
	}

	public String getNote(){
		return this.note;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return this.phone;
	}

	public void setAddr(String addr){
		this.addr = addr;
	}

	public String getAddr(){
		return this.addr;
	}

	public void setDaorurenyuan(String daorurenyuan){
		this.daorurenyuan = daorurenyuan;
	}

	public String getDaorurenyuan(){
		return this.daorurenyuan;
	}

	public void setLeibie(String leibie){
		this.leibie = leibie;
	}

	public String getLeibie(){
		return this.leibie;
	}

}