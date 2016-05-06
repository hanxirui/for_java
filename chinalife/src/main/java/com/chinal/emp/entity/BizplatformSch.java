package com.chinal.emp.entity;

import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.support.BsgridSearch;

public class BizplatformSch extends BsgridSearch {

	private Integer id;
	private String title;
	private String url;
	private String clazz;
	private Integer start;
	private Integer end;

	public void setId(Integer id) {
		this.id = id;
	}

	@ValueField(column = "id")
	public Integer getId() {
		return this.id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ValueField(column = "title")
	public String getTitle() {
		return this.title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@ValueField(column = "url")
	public String getUrl() {
		return this.url;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	@ValueField(column = "clazz")
	public String getClazz() {
		return this.clazz;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	@ValueField(column = "start")
	public int getStart() {
		return this.start;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	@ValueField(column = "end")
	public Integer getEnd() {
		return this.end;
	}

}