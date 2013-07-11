package com.zt.maximo;

import com.zt.maximo.service.domain.UserDo;


public class F {
	
	public static final String PROXY_SERVER_URL = "http://127.0.0.1";
	
	public static final String DB_NAME = "tx.db";
	public static final boolean DB_DEBUG = true;
	
	public static volatile UserDo user = null;

}
