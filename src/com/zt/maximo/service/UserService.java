package com.zt.maximo.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.zt.maximo.F;
import com.zt.maximo.dao.UserDao;
import com.zt.maximo.dao.domain.UserDo;
import com.zt.maximo.service.domain.AppProxyResultDo;

public class UserService extends BaseService {

	private static UserService instance;

	public static UserService getInstance() {
		if (null == instance) {
			instance = new UserService();
		}
		return instance;
	}

	private UserService() {
	}

	public AppProxyResultDo login(String account, String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("account", account);
		map.put("password", password);
		return this.execute(login, map);
	}

	public AppProxyResultDo logout() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", F.user.getUid().toString());
		map.put("txid", F.user.getTxid());
		map.put("imei", F.user.getImei());
		return this.execute(logout, map);
	}

	public AppProxyResultDo autoLogin(Integer uid, String txid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid.toString());
		map.put("txid", txid);
		return this.execute(autoLogin, map);
	}

	public AppProxyResultDo reflushCode() {
		return this.execute(reflushCode, null);
	}

	public AppProxyResultDo getUserInfo(Integer uid, String txid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid.toString());
		map.put("txid", txid);
		return this.execute(getUserInfo, map);
	}

	public AppProxyResultDo getUserSetInfo(Integer uid, String txid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid.toString());
		map.put("txid", txid);
		return this.execute(getUserSetInfo, map);
	}

	public AppProxyResultDo updateUser(Integer uid, String txid, int countryId,
			String nick) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid.toString());
		map.put("txid", txid);
		map.put("countryId", Integer.toString(countryId));
		map.put("nick", nick);
		return this.execute(updateUser, map);
	}

	public AppProxyResultDo updateUserSummary(Integer uid, String txid,
			String summary) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", uid.toString());
		map.put("txid", txid);
		map.put("summary", summary);
		return this.execute(updateUserSummary, map);
	}

	public AppProxyResultDo getAddFriendModel() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", F.user.getUid().toString());
		map.put("txid", F.user.getTxid());
		return this.execute(getAddFriendModel, map);
	}

	public AppProxyResultDo updateUserIcon(String fileName, String uri) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", F.user.getUid().toString());
		map.put("txid", F.user.getTxid());
		map.put("fileName", fileName);
		return this.executeWithFile(updateUserIcon, map, new File(uri));
	}

	public UserDo saveOrUpdateUser(UserDo user, UserDao userDao) {
		UserDo tmp = userDao.getUserByUid(user.getUid());
		if (null == tmp) {
			// 插入数据库
			userDao.addUser(user);
			return user;
		} else {
			// 更新用户数据
			userDao.updateUser(user);
			return userDao.getUserByUid(user.getUid());
		}
	}

}
