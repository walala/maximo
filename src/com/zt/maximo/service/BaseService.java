package com.zt.maximo.service;

import java.io.File;
import java.util.Map;

import com.zt.maximo.F;
import com.zt.maximo.service.domain.AppProxyResultDo;
import com.zt.maximo.util.HttpUtil;
import com.zt.maximo.util.JsonUtil;
import com.zt.maximo.util.StringUtil;

public class BaseService {
	
	/**
	 * 通用
	 */
	private final static String SERVER_URL = F.PROXY_SERVER_URL;
	
	/**
	 * 用户
	 */
	//登录
	protected static final String login = "/zt/rest/user/login";
	
	//退出登录
	protected static final String logout = "/user/logout";
	
	//自动登录
	protected static final String autoLogin = "/user/autologin";
	//刷新验证码的url,返回key,url
	protected static final String reflushCode = "/user/reflushCode";
	//获取用户自己信息
	protected static final String getUserInfo = "/user/single";
	//获取用户设置信息
	protected static final String getUserSetInfo = "/user/setInfo";
	//修改用户信息
	protected static final String updateUser = "/user/modify";
	//获取用户加友设置
	protected static final String getAddFriendModel = "/user/getAddFriendModel";
	//修改用户头像
	protected static final String updateUserIcon = "/user/updateIcon";
	//修改用户个性介绍
	protected static final String updateUserSummary = "/user/modifySummary";

	
	protected AppProxyResultDo execute(String method,Map<String, String> args) {
		String json = HttpUtil.post(SERVER_URL+method, args);
		if(StringUtil.isBlank(json)){
			AppProxyResultDo ard = new AppProxyResultDo();
			ard.setError(true);
			ard.setErrorMessage("服务端响应异常");
			return ard;
		}
		AppProxyResultDo aprd = (AppProxyResultDo)JsonUtil.Json2T(json, AppProxyResultDo.class) ;
		if (null==aprd) {
			AppProxyResultDo ard = new AppProxyResultDo();
			ard.setError(true);
			ard.setErrorMessage("服务端响应异常");
			return ard;
		}
		return aprd;
	}
	
	protected AppProxyResultDo executeWithFile(String method,Map<String, String> args,File file) {
		String json = HttpUtil.post(SERVER_URL+method, args,file);
		if(StringUtil.isBlank(json)){
			AppProxyResultDo ard = new AppProxyResultDo();
			ard.setError(true);
			ard.setErrorMessage("服务端响应异常");
			return ard;
		}
		AppProxyResultDo aprd = (AppProxyResultDo)JsonUtil.Json2T(json, AppProxyResultDo.class) ;
		if (null==aprd) {
			AppProxyResultDo ard = new AppProxyResultDo();
			ard.setError(true);
			ard.setErrorMessage("服务端响应异常");
			return ard;
		}
		return aprd;
	}
}
