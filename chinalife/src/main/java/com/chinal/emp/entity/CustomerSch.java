package com.chinal.emp.entity;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.BsgridSearch;

public class CustomerSch extends BsgridSearch {

	private Integer id;
	private String name;
	private String phone;
	private String address;
	private Integer type;
	private Integer fuzeren;
	private String fuzerenName;
	private Integer tuijianren;

	public void setId(Integer id) {
		this.id = id;
	}

	@ValueField(column = "id")
	public Integer getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ValueField(column = "name")
	public String getName() {
		return this.name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@ValueField(column = "phone")
	public String getPhone() {
		return this.phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ValueField(column = "address")
	public String getAddress() {
		return this.address;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@ValueField(column = "type")
	public Integer getType() {
		return this.type;
	}

	public void setFuzeren(Integer fuzeren) {
		this.fuzeren = fuzeren;
	}

	@ValueField(column = "fuzeren")
	public Integer getFuzeren() {
		return this.fuzeren;
	}

	public void setTuijianren(Integer tuijianren) {
		this.tuijianren = tuijianren;
	}

	@ValueField(column = "tuijianren")
	public Integer getTuijianren() {
		return this.tuijianren;
	}

	public String getFuzerenName() {
		return fuzerenName;
	}

	public void setFuzerenName(String fuzerenName) {
		this.fuzerenName = fuzerenName;
	}

}