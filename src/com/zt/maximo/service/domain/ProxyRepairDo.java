package com.zt.maximo.service.domain;

import java.io.Serializable;

public class ProxyRepairDo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7755344948371182393L;
	
	private long id;
	private String name;
	private String title;
	private String info;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	

}
