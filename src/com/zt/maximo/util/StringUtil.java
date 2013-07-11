package com.zt.maximo.util;

public class StringUtil {

	
	public static boolean isBlank(String src){
		return null==src||"".equals(src.trim());
	}
	
	public static boolean isNotBlank(String src){
		return null!=src&&(!"".equals(src.trim()));
	}
}
