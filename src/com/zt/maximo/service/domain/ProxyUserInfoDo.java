package com.zt.maximo.service.domain;

import java.io.Serializable;

/**
 * 
 * 用户信息
 * 
 */
public class ProxyUserInfoDo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7189600987142853947L;
	
	/**
	 * 用户uid
	 */
	private int uid;

	/**
	 * 用户昵称
	 */
	private String nick;

	/**
	 * 登录业务的结果
	 */
	private int requestCode;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}
	
}
