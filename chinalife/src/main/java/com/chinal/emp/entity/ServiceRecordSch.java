package com.chinal.emp.entity;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.BsgridSearch;

public class ServiceRecordSch extends BsgridSearch {

	private Integer id;
	private String idcardnum;
	private String servicetime;
	private String content;
	private String empcode;
	private String name;

	public void setId(Integer id) {
		this.id = id;
	}

	@ValueField(column = "id")
	public Integer getId() {
		return this.id;
	}

	public void setIdcardnum(String idcardnum) {
		this.idcardnum = idcardnum;
	}

	@ValueField(column = "idcardnum")
	public String getIdcardnum() {
		return this.idcardnum;
	}

	public void setServicetime(String servicetime) {
		this.servicetime = servicetime;
	}

	@ValueField(column = "servicetime")
	public String getServicetime() {
		return this.servicetime;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@ValueField(column = "content")
	public String getContent() {
		return this.content;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	@ValueField(column = "empcode")
	public String getEmpcode() {
		return this.empcode;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ValueField(column = "name")
	public String getName() {
		return this.name;
	}

}