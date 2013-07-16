package com.zt.maximo;

import com.zt.maximo.dao.domain.UserDo;


public class F {
	
	public static final String PROXY_SERVER_URL = "http://127.0.0.1";
	
	public static final String DB_NAME = "mix.db";
	public static final boolean DB_DEBUG = true;
	
	public static volatile UserDo user = null;

}
