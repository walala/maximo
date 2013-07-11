package com.zt.maximo.dao;

import java.util.List;

import android.content.Context;

import com.zt.maximo.service.domain.UserDo;

public class UserDao extends TxBaseDao {
	
	public UserDao(Context context) {
		super(context);
	}
	
	public UserDo getAutoLoginUser(){
		return db.findForObject(UserDo.class, "autoLogin=?", new String[]{true+""});
	}
	
	public UserDo getSavePasswordUser(){
		return db.findForObject(UserDo.class, "savePassword=?", new String[]{true+""});
	}
	
	public List<UserDo> getAllSavePasswordUser(){
		return db.findForList(UserDo.class, "savePassword=?", new String[]{true+""});
	}
	
	public long addUser(UserDo user){
		return db.insert(user);
	}
	
	public int updateUser(UserDo user){
		return db.update(user, "uid=?", new String[]{user.getUid().toString()});
	}
	
	public UserDo getUserByUid(Integer uid){
		return db.findForObject(UserDo.class, "uid=?", new String[]{uid.toString()});
	}
	
	public void updateAutoLogin(Integer uid,Boolean autoLogin){
		UserDo user = new UserDo();
		user.setAutoLogin(autoLogin);
		db.update(user, "uid=?", new String[]{uid.toString()});
	}
}
