package com.zt.maximo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.zt.maximo.F;

public class PropertiesUtil {
	
	private static final String fileName = "mixprop";

	private SharedPreferences sp;
	private Editor editor;

	public PropertiesUtil(Context context) {
		sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	public void setString(SpKey e,String value){
		editor.putString(e.getText(), value);
		editor.commit();
	}
	
	public String getString(SpKey e,String defValue){
		return sp.getString(e.getText(), defValue);
	}
	
	public void setInt(SpKey e,int value){
		editor.putInt(e.getText(), value);
		editor.commit();
	}
	
	public int getInt(SpKey e,int defValue){
		return sp.getInt(e.getText(), defValue);
	}
	public void setLong(SpKey e,long value){
		editor.putLong(e.getText(), value);
		editor.commit();
	}
	public long getLong(SpKey e,long defValue){
		return sp.getLong(e.getText(), defValue);
	}
	public void setBoolean(SpKey e,boolean value){
		editor.putBoolean(e.getText(), value);
		editor.commit();
	}
	
	public boolean getBoolean(SpKey e,boolean defValue){
		return sp.getBoolean(e.getText(), defValue);
	}
	
	/**
	 * 返回用户是否为首次登陆，并将首次登陆状态设置为true
	 * @return
	 */
	public boolean isFirstLogin(){
		boolean result = sp.getBoolean(F.user.getUid().toString(), true);
		if (result) {
			editor.putBoolean(F.user.getUid().toString(), false);
			editor.commit();
		}
		return result;
	}
	
	public static enum SpKey {
		
		uid("uid"),
		imei("imei"),
		autoLogin("autoLogin"),
		savePassword("savePassword"),
		account("account"),
		password("password"),
		nick("nick"),
		isFirst("isFirst"),
		version("version"),
		versioninfo("versioninfo"),
		newerHelp("newerHelp"),
		isExpandFirst("isExpandFirst")
		;
		
		String text;
		
		private SpKey(String text){
			this.text = text;
		}
		
		public String getText(){
			return this.text;
		}
	}
}


