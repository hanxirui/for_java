package com.chinal.emp.entity;

/**
  
*/
public class Bizplatform {
	//
	private int id;
	//
	private String title;
	//
	private String url;
	//
	private String clazz;
	//
	private int start;
	//
	private int end;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getClazz() {
		return this.clazz;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStart() {
		return this.start;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getEnd() {
		return this.end;
	}

}