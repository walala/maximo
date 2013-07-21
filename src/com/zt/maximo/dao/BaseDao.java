package com.zt.maximo.dao;

import android.content.Context;
import android.util.Log;

import com.zt.maximo.F;
import com.zt.maximo.orm.MixDb;
import com.zt.maximo.orm.MixDb.DaoConfig;

public class BaseDao {

	protected MixDb db;
	
	public BaseDao(Context context){
		DaoConfig daoConfig = new DaoConfig();
		daoConfig.setContext(context);
		daoConfig.setDbName(F.DB_NAME);
		daoConfig.setDebug(F.DB_DEBUG);
		db = MixDb.create(daoConfig);
	}

	
	//创建表结构
	public void init(){
		//创建表语句
		Log.d("MixDb", " init table");
		
		//用户表
		String sqlUser = "CREATE TABLE if not exists  mixUser (_id INTEGER PRIMARY KEY autoincrement,uid INTEGER,account TEXT," +
				"password TEXT,nick TEXT,sex TEXT,defsite TEXT,status INTEGER,autoLogin TEXT,savePassword TEXT," +
				"lastLogin INTEGER,lastUpdate TEXT,imei TEXT);";
		
		//聊天记录
		String sqlRepair="CREATE TABLE if not exists  mixRepair (_id INTEGER PRIMARY KEY autoincrement,uid INTEGER,account TEXT," +
				"orderid INTEGER,content TEXT,ctime INTEGER,status INTEGER);";
		//String sqlRepairIndex="CREATE INDEX mixRecent ON mixRepair(uid,orderid,ctime);";
	
		db.execSql(sqlUser);
		db.execSql(sqlRepair);
		
	}
}
