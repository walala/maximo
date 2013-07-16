package com.zt.maximo.dao.domain;

import java.io.Serializable;

import com.zt.maximo.orm.annotation.Id;
import com.zt.maximo.orm.annotation.Table;
import com.zt.maximo.orm.annotation.Transient;


@Table("txUser")
public class UserDo implements Serializable {

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	Long _id;
	/**
	 * UID
	 */
	Integer uid;
	/**
	 * z
	 */
	String txid;
	/**
	 * 账号
	 */
	String account;
	/**
	 * 密码
	 */
	String password;
	/**
	 * 自动登录
	 */
	Boolean autoLogin;
	/**
	 * 保存密码
	 */
	Boolean savePassword;
	/**
	 * 昵称
	 */
	String nick;
	/**
	 * 性别
	 */
	Integer sex;
	/**
	 * 最后手工登录时间
	 */
	Long lastLogin;
	/**
	 * 设备号
	 */
	String imei;

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getAutoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(Boolean autoLogin) {
		this.autoLogin = autoLogin;
	}

	public Boolean getSavePassword() {
		return savePassword;
	}

	public void setSavePassword(Boolean savePassword) {
		this.savePassword = savePassword;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Long getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
}
