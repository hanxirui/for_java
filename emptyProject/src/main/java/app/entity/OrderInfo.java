package app.entity;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import com.alibaba.fastjson.annotation.JSONField;

/**
  
*/
public class OrderInfo {
	// 
	private int orderId;
	@NotEmpty(message="城市不能为空")
	private String cityName;
	@NotEmpty(message="手机号不能为空")
	private String mobile;
	private String address;
	// 
	private Date createDate;

	public void setOrderId(int orderId){
		this.orderId = orderId;
	}

	public int getOrderId(){
		return this.orderId;
	}

	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	public String getCityName(){
		return this.cityName;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return this.mobile;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return this.address;
	}

	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}

	@JSONField(format="yyyy-MM-dd")
	public Date getCreateDate(){
		return this.createDate;
	}

}