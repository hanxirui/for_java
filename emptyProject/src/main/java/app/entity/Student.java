package app.entity;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class Student {

	@Min(1)
	private Integer id;
	@Size(message="学生姓名",max=50)
	@NotEmpty(message="学生姓名不能为空")
	private String name;
	private Integer politicsStatus = 1;
	private String nationality = "汉族";
	@Size(message="学号长度不能超过20",max=20)
	@NotEmpty(message="学号不能为空")
	private String stuNo;
	private Byte gender = 1;
	private Integer department = 16;
	@Size(message="请输入正确的地址",max=200)
	private String address;
	@Pattern(message="请输入正确的手机号",regexp="^[1]\\d{10}$")
	private String mobile;
	private Date registDate = new Date();
	private Date birthday;
	
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return this.id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	public void setPoliticsStatus(Integer politicsStatus) {
		this.politicsStatus = politicsStatus;
	}
	public Integer getPoliticsStatus() {
		return this.politicsStatus;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getNationality() {
		return this.nationality;
	}
	public void setStuNo(String stuNo) {
		this.stuNo = stuNo;
	}
	public String getStuNo() {
		return this.stuNo;
	}
	public void setGender(Byte gender) {
		this.gender = gender;
	}
	public Byte getGender() {
		return this.gender;
	}
	public void setDepartment(Integer department) {
		this.department = department;
	}
	public Integer getDepartment() {
		return this.department;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return this.address;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMobile() {
		return this.mobile;
	}
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}
	public Date getRegistDate() {
		return this.registDate;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Date getBirthday() {
		return this.birthday;
	}
	
	public String getGenderStr(){
		return gender == 1 ? "男" : "女";
	}
}
