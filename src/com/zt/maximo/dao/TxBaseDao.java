package com.zt.maximo.dao;

import android.content.Context;
import android.util.Log;

import com.zt.maximo.F;
import com.zt.maximo.orm.TxDb;
import com.zt.maximo.orm.TxDb.DaoConfig;

public class TxBaseDao {

	protected TxDb db;
	
	public TxBaseDao(Context context){
		DaoConfig daoConfig = new DaoConfig();
		daoConfig.setContext(context);
		daoConfig.setDbName(F.DB_NAME);
		daoConfig.setDebug(F.DB_DEBUG);
		db = TxDb.create(daoConfig);
	}

	
	//创建表结构
	public void init(){
		//创建表语句  用户表
//		db.execSql("DROP TABLE IF EXISTS txUser");
//		db.execSql("DROP TABLE IF EXISTS txChat");
//		db.execSql("DROP TABLE IF EXISTS txGroup");
//		db.execSql("DROP TABLE IF EXISTS txfriends");
//		db.execSql("DROP TABLE IF EXISTS sqlTxRecent");
		
		Log.d("TxBaseDao", " init table");
		
		String sqltxUse = "CREATE TABLE if not exists  txUser (_id INTEGER PRIMARY KEY autoincrement,uid INTEGER,txid TEXT,account TEXT," +
				"password TEXT,autoLogin TEXT,savePassword TEXT," +
				"nick TEXT,sex INTEGER,point INTEGER,countryId INTEGER,cityId INTEGER," +
				"provinceId INTEGER,birthday INTEGER,pic TEXT,picLocal TEXT,summary TEXT," +
				"vip INTEGER,friendUpdate INTEGER,lastLogin INTEGER,lastUpdate TEXT,imei TEXT);";
		
		//好友表
		String sqlTxfriends="CREATE TABLE if not exists  txfriends (_id INTEGER PRIMARY KEY autoincrement,uid INTEGER,friendUid INTEGER,nick TEXT," +
				"sex INTEGER,remark TEXT,vip INTEGER,groupId INTEGER,point INTEGER,pic TEXT,picLocal TEXT," +
				"terminal INTEGER,summary TEXT,online TEXT,radio TEXT,brithday INTEGER,countryId INTEGER,cityId INTEGER,provinceId INTEGER,isFriend TEXT);";
		//String sqlTxfriendsIndex="CREATE INDEX txfriends ON testtable(uid,friendUid,point,vip);";
		//用户分组表
		String sqlTxGroup="CREATE TABLE if not exists  txGroup (_id INTEGER PRIMARY KEY autoincrement,uid INTEGER,groupId INTEGER,groupName TEXT,members INTEGER);";
		//最近聊天记录表
		String sqlTxRecent="CREATE TABLE if not exists  txRecent (_id INTEGER PRIMARY KEY autoincrement,uid INTEGER,friendUid INTEGER,content TEXT,ctime INTEGER,news TEXT,num INTEGER);";
		//String sqlTxRecentIndex="CREATE INDEX txRecent ON testtable(uid,friendUid,ctime,new);";
		
		//聊天记录
		String sqlTxChat="CREATE TABLE if not exists  txChat (_id INTEGER PRIMARY KEY autoincrement,uid INTEGER,friendUid INTEGER,content TEXT,receive TEXT,ctime INTEGER,state TEXT,form INTEGER,sd TEXT);";
		//String sqlTxChatIndex="CREATE INDEX txRecent ON testtable(uid,friendUid,ctime);";
		
		//好友请求表 txFriendRequest
		String sqlTxFriendRequest="CREATE TABLE if not exists  txFriendRequest (_id INTEGER PRIMARY KEY autoincrement ,uid INTEGER ," +
				"friendUid INTEGER ,content TEXT,pic TEXT,sex INTEGER ,result TEXT,ctime INTEGER ,picLocal TEXT  );";
		//地域表
		String sqlTxLocation = "create table if not exists TxLocation(_id integer primary key autoincrement, lid INTEGER,name TEXT,fid INTEGER);";
		
		//用户扩展表
//		String sqlTxExpand="create table if not exists txExpand(id INTEGER PRIMARY KEY autoincrement ,uid INTEGER ,name TEXT,val TEXT);";//Expand
		
		
	
		db.execSql(sqltxUse);
		db.execSql(sqlTxfriends);
		db.execSql(sqlTxGroup);
		db.execSql(sqlTxRecent);
		db.execSql(sqlTxChat);
		db.execSql(sqlTxFriendRequest);
		db.execSql(sqlTxLocation);
		
//		db.execSql(sqlTxExpand);
		
//		db.insert(entity)
	}
	public void initExpand(){
		//用户扩展表
		Log.d("TxBaseDao", " init Expand");
		String sqlTxExpand="create table if not exists txExpand(id INTEGER PRIMARY KEY autoincrement ,uid INTEGER ,name TEXT,val TEXT);";//Expand
		db.execSql(sqlTxExpand);
	}
	
}
