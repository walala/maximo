package com.zt.maximo;

import com.zt.maximo.dao.domain.UserDo;


public class F {
	
	public static final String PROXY_SERVER_URL = "http://10.0.2.2";
	
	public static final String DB_NAME = "mix.db";
	public static final boolean DB_DEBUG = true;
	
	public static volatile UserDo user = null;

}
