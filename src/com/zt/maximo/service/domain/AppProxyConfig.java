package com.zt.maximo.service.domain;

public class AppProxyConfig {

	/**
	 * 用户登录成功标识
	 */
	public static final int USER_LOGIN_SUCCESS = 1;
	
	/**
	 * 用户登录失败标识
	 */
	public static final int USER_LOGIN_FAIL = 2;
	
	/**
	 * 用户登录需要验证码
	 */
	public static final int USER_LOGIN_CODE = 3;
	
	/**
	 * 服务调用结果
	 */
	public static final boolean SERVICE_REQUEST_RESULT = false;
	
	/**
	 * socket ip 
	 */
	public static String SOCKET_IP;
	/**
	 * socket port 
	 */
	public static int SOCKET_PORT;
	
	/**
	 * 无权调用此服务
	 */
	public static final String SERVICE_NOT_AUTHORIZED = "无权调用此服务";
	
	
	public static final String CREATE_PIC_URL = "user/key";
	
}
