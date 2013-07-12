/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zt.maximo.orm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zt.maximo.orm.sqlite.CursorUtils;
import com.zt.maximo.orm.sqlite.DbModel;
import com.zt.maximo.orm.sqlite.SqlBuilder;
import com.zt.maximo.orm.sqlite.SqlInfo;
import com.zt.maximo.orm.table.KeyValue;
import com.zt.maximo.orm.table.TableInfo;
import com.zt.maximo.util.JsonUtil;
import com.zt.maximo.util.StringUtil;

/**
 * 支持数据类型:String,Integer,Float,Double,Long,Boolean("0","1")
 * @author qin
 *
 */
public class MixDb {
	
	private static final String TAG = "MixDb";
	
	private static HashMap<String, MixDb> daoMap = new HashMap<String, MixDb>();
	
	private SQLiteDatabase db;
	private DaoConfig config;
	
	private MixDb(DaoConfig config){
		if(config == null)
			throw new RuntimeException("daoConfig is null");
		if(config.getContext() == null)
			throw new RuntimeException("android context is null");
		this.db = new SqliteDbHelper(config.getContext().getApplicationContext(), config.getDbName(), config.getDbVersion(),config.getDbUpdateListener()).getWritableDatabase();
		this.config = config;
	}
	
	
	private synchronized static MixDb getInstance(DaoConfig daoConfig) {
		MixDb dao = daoMap.get(daoConfig.getDbName());
		if(dao == null){
			dao = new MixDb(daoConfig);
			daoMap.put(daoConfig.getDbName(), dao);
		}
		return dao;
	}
	
	public SQLiteDatabase getOriginDb() {
		return db;
	}
	
	/**
	 * 创建FinalDb
	 * @param context
	 */
	public static MixDb create(Context context){
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		
		return getInstance(config);
		
	}
	
	/**
	 * 创建FinalDb
	 * @param context
	 * @param isDebug 是否是debug模式（debug模式进行数据库操作的时候将会打印sql语句）
	 */
	public static MixDb create(Context context,boolean isDebug){
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDebug(isDebug);
		return getInstance(config);
	}
	
	/**
	 * 创建FinalDb
	 * @param context
	 * @param dbName 数据库名称
	 */
	public static MixDb create(Context context,String dbName){
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDbName(dbName);
		
		return getInstance(config);
	}
	
	/**
	 * 创建 FinalDb
	 * @param context
	 * @param dbName 数据库名称
	 * @param isDebug 是否为debug模式（debug模式进行数据库操作的时候将会打印sql语句）
	 */
	public static MixDb create(Context context,String dbName,boolean isDebug){
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDbName(dbName);
		config.setDebug(isDebug);
		return getInstance(config);
	}
	
	/**
	 * 创建 FinalDb
	 * @param context 上下文
	 * @param dbName 数据库名字
	 * @param isDebug 是否是调试模式：调试模式会log出sql信息
	 * @param dbVersion 数据库版本信息
	 * @param dbUpdateListener 数据库升级监听器：如果监听器为null，升级的时候将会清空所所有的数据
	 * @return
	 */
	public static MixDb create(Context context,String dbName,boolean isDebug,int dbVersion,DbUpdateListener dbUpdateListener){
		/**
		 * TODO:测试在数据库升级后(android版本升级后,应用数据库状态)
		 */
		DaoConfig config = new DaoConfig();
		config.setContext(context);
		config.setDbName(dbName);
		config.setDebug(isDebug);
		config.setDbVersion(dbVersion);
		config.setDbUpdateListener(dbUpdateListener);
		return getInstance(config);
	}
	
	/**
	 * 创建FinalDb
	 * @param daoConfig
	 * @return
	 */
	public static MixDb create(DaoConfig daoConfig){
		return getInstance(daoConfig);
	}
	

	
	/**
	 * 保存数据库
	 * @param entity
	 */
//	public void save(Object entity){
//		checkTableExist(entity.getClass());
//		exeSqlInfo(SqlBuilder.buildInsertSql(entity));
//	}
	
	public void execSql(String sql,Object[] args){
		db.execSQL(sql, args);
	}
	
	public void execSql(String sql){
		db.execSQL(sql);
	}
	
	/**
	 * 保存数据到数据库,速度要比save快<br />
	 * <b>注意：</b><br />
	 * 保存成功后，entity的主键将被赋值（或更新）为数据库的主键， 只针对自增长的id有效
	 * @param entity 要保存的数据
	 * @return  ture： 保存成功    false:保存失败
	 */
	public long insert(Object entity){
//		checkTableExist(entity.getClass());
		ContentValues cv = getNotNullContentValues(entity);
		if (null==cv) {
			return -1;
		}else{
			return db.insert(TableInfo.get(entity.getClass()).getTableName(), null, cv);
		}
	}
	
	/**
	 * 把List<KeyValue>数据存储到ContentValues
	 * @param list
	 * @param cv
	 */
	private void insertContentValues(List<KeyValue> list , ContentValues cv){
		if(list!=null && cv!=null){
			for(KeyValue kv : list){
				cv.put(kv.getKey(), kv.getValue().toString());
			}
		}else{
			Log.w(TAG, "insertContentValues: List<KeyValue> is empty or ContentValues is empty!");
		}
		
	}
	
	/**
	 * 更新数据 （主键ID必须不能为空）
	 * @param entity
	 */
//	public void updateById(Object entity){
//		checkTableExist(entity.getClass());
//		exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity));
//	}
	
	/**
	 * 根据条件更新数据
	 * @param entity
	 * @param strWhere 条件为空的时候，将会更新所有的数据
	 */
//	public void update(Object entity,String strWhere){
//		checkTableExist(entity.getClass());
//		exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity, strWhere));
//	}
	
	/**
	 * 根据条件更新数据
	 * @param entity
	 * @param strWhere 条件为空的时候，将会更新所有的数据
	 */
	public int update(Object entity,String strWhere,String[] args){
//		checkTableExist(entity.getClass());
		TableInfo tableInfo = TableInfo.get(entity.getClass());
		ContentValues cv = getNotNullContentValues(entity);
		return db.update(tableInfo.getTableName(), cv, strWhere, args);
	}
	
	private ContentValues getNotNullContentValues(Object entity){
		List<KeyValue> entityKvList = SqlBuilder.getSaveKeyValueListByEntity(entity);
		if(entityKvList != null && entityKvList.size() > 0){
			ContentValues cv = new ContentValues();
			insertContentValues(entityKvList,cv);
			return cv;
		}else {
			return null;
		}
	}
	
	/**
	 * 删除数据
	 * @param entity  entity的主键不能为空
	 */
//	public void delete(Object entity) {
//		checkTableExist(entity.getClass());
//		exeSqlInfo(SqlBuilder.buildDeleteSql(entity));
//	}
	
	/**
	 * 根据主键删除数据
	 * @param clazz 要删除的实体类
	 * @param id 主键值
	 */
//	public void delete(Class<?> clazz , Object id) {
//		checkTableExist(clazz);
//		exeSqlInfo(SqlBuilder.buildDeleteSql(clazz, id));
//	}
	
	public int delete(Class<?> clazz , String strWhere,String[] args) {
//		checkTableExist(clazz);
		return db.delete(TableInfo.get(clazz).getTableName(), strWhere, args);
	}
	
	/**
	 * 根据条件删除数据
	 * @param clazz
	 * @param strWhere 条件为空的时候 将会删除所有的数据
	 */
//	public void deleteByWhere(Class<?> clazz , String strWhere ) {
//		checkTableExist(clazz);
//		String sql = SqlBuilder.buildDeleteSql(clazz, strWhere);
//		debugSql(sql);
//		db.execSQL(sql);
//	}
//	
	
	private void exeSqlInfo(SqlInfo sqlInfo){
		if(sqlInfo!=null){
			debugSql(sqlInfo.getSql());
			debugSql("args:"+JsonUtil.Object2Json(sqlInfo.getBindArgsAsStringArray()));
			db.execSQL(sqlInfo.getSql(),sqlInfo.getBindArgsAsArray());
		}else{
			Log.e(TAG, "sava error:sqlInfo is null");
		}
	}
	
	/**
	 * 根据主键查找数据
	 * @param id
	 * @param clazz
	 */
//	public <T> T findForObject(Object id ,Class<T> clazz){
//		checkTableExist(clazz);
//		SqlInfo sqlInfo = SqlBuilder.getSelectSqlAsSqlInfo(clazz, id);
//		if(sqlInfo!=null){
//			debugSql(sqlInfo.getSql());
//			Cursor cursor = db.rawQuery(sqlInfo.getSql(), sqlInfo.getBindArgsAsStringArray());
//			try {
//				if(cursor.moveToNext()){
//					return CursorUtils.getEntity(cursor, clazz);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}finally{
//				cursor.close();
//			}
//		}
//		return null;
//	}
	
	/**
	 * 查找所有的数据
	 * @param clazz
	 */
	public <T> List<T> findAllForList(Class<T> clazz){
//		checkTableExist(clazz);
		return findForListBySql(clazz,SqlBuilder.getSelectSQL(clazz));
	}
	
	/**
	 * 查找所有数据
	 * @param clazz
	 * @param orderBy 排序的字段
	 */
	public <T> List<T> findAllForList(Class<T> clazz,String orderBy){
//		checkTableExist(clazz);
		return findForListBySql(clazz,SqlBuilder.getSelectSQL(clazz)+" ORDER BY "+orderBy);
	}
	
	/**
	 * 根据条件查找所有数据
	 * @param clazz
	 * @param strWhere 条件为空的时候查找所有数据
	 */
//	public <T> List<T> findForList(Class<T> clazz,String strWhere){
//		checkTableExist(clazz);
//		return findAllBySql(clazz,SqlBuilder.getSelectSQLByWhere(clazz,strWhere));
//	}
//	
	/**
	 * 根据条件查找所有数据
	 * @param clazz
	 * @param strWhere 条件为空的时候查找所有数据
	 * @param orderBy 排序字段
	 */
//	public <T> List<T> findForList(Class<T> clazz,String strWhere,String orderBy){
//		checkTableExist(clazz);
//		return findAllBySql(clazz,SqlBuilder.getSelectSQLByWhere(clazz,strWhere)+" ORDER BY '"+orderBy+"' DESC");
//	}
	
	public <T> List<T> findForList(Class<T> clazz,String strWhere,String[] args,String orderBy){
//		checkTableExist(clazz);
		String sql = SqlBuilder.getSelectSQLByWhere(clazz, strWhere);
		if (StringUtil.isNotBlank(orderBy)) {
			sql+=" ORDER BY "+orderBy;
		}
		debugSql(sql);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, args);
			List<T> results = new ArrayList<T>();
			while (cursor.moveToNext()) {
				results.add(CursorUtils.getEntity(cursor, clazz));
			}
			return results;
		} catch (Exception e) {
			return null;
		} finally{
			if (null!=cursor) {
				cursor.close();
				cursor = null;
			}
		}
	}
	
	public <T> List<T> findForList(Class<T> clazz,String strWhere,String[] args){
		return findForList(clazz, strWhere, args, null);
	}
	
	/**
	 * 根据条件查找所有数据
	 * @param clazz
	 * @param strSQL
	 */
	private <T> List<T> findForListBySql(Class<T> clazz,String strSQL){
//		checkTableExist(clazz);
		debugSql(strSQL);
		Cursor cursor = db.rawQuery(strSQL, null);
		try {
			List<T> list = new ArrayList<T>();
			while(cursor.moveToNext()){
				T t = CursorUtils.getEntity(cursor, clazz);
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null)
				cursor.close();
			cursor=null;
		}
		return null;
	}
	
	/**
	 * 根据条件查询一条记录
	 * @param clazz
	 * @param strWhere
	 * @return
	 */
	public <T> T findForObject(Class<T> clazz,String strWhere,String[] args){
//		checkTableExist(clazz);
		String sql = SqlBuilder.getSelectSQLByWhere(clazz, strWhere);
		debugSql(sql);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql,args);
			if (cursor.moveToFirst()) {
				return CursorUtils.getEntity(cursor, clazz);
			}else {
				return null;
			}
		} catch (Exception e) {
			return null;
		} finally{
			if (null!=cursor) {
				cursor.close();
				cursor = null;
			}
		}
		
	}
	
	/**
	 * 根据sql语句查找数据
	 * @param strSQL
	 */
	public DbModel findForDbModelBySQL(String strSQL){
		debugSql(strSQL);
		Cursor cursor = db.rawQuery(strSQL,null);
		try {
			if(cursor.moveToNext()){
				return CursorUtils.getDbModel(cursor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cursor.close();
		}
		return null;
	}
	
	public List<DbModel> findForDbModelListBySQL(String strSQL){
		debugSql(strSQL);
		Cursor cursor = db.rawQuery(strSQL,null);
		List<DbModel> dbModelList = new ArrayList<DbModel>();
		try {
			while(cursor.moveToNext()){
				dbModelList.add(CursorUtils.getDbModel(cursor));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cursor.close();
		}
		return dbModelList;
	}
	
//	private void checkTableExist(Class<?> clazz){
//		if(!tableIsExist(TableInfo.get(clazz))){
//			String sql = SqlBuilder.getCreatTableSQL(clazz);
//			debugSql(sql);
//			db.execSQL(sql);
//		}
//	}
	
	private boolean tableIsExist(TableInfo table){
		if(table.isCheckDatabese())
			return true;
		
        Cursor cursor = null;
        try {
                String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='"+table.getTableName()+"' ";
                debugSql(sql);
                cursor = db.rawQuery(sql, null);
                if(cursor!=null && cursor.moveToNext()){
                        int count = cursor.getInt(0);
                        if(count>0){
                        	table.setCheckDatabese(true);
                            return true;
                        }
                }
                
        } catch (Exception e) {
                e.printStackTrace();
        }finally{
        	if(cursor!=null)
        		cursor.close();
        	cursor=null;
        }
        
        return false;
	}
	
	
	private void debugSql(String sql){
		if(config!=null && config.isDebug())
			android.util.Log.d("Debug SQL", ">>>>>>  "+sql);
	}
	
	public static class DaoConfig{
		private Context context = null;//android上下文
		private String dbName = "afinal.db";//数据库名字
		private int dbVersion = 1;//数据库版本
		private boolean debug = true;
		private DbUpdateListener dbUpdateListener;
		
		public Context getContext() {
			return context;
		}
		public void setContext(Context context) {
			this.context = context;
		}
		public String getDbName() {
			return dbName;
		}
		public void setDbName(String dbName) {
			this.dbName = dbName;
		}
		public int getDbVersion() {
			return dbVersion;
		}
		public void setDbVersion(int dbVersion) {
			this.dbVersion = dbVersion;
		}
		public boolean isDebug() {
			return debug;
		}
		public void setDebug(boolean debug) {
			this.debug = debug;
		}
		public DbUpdateListener getDbUpdateListener() {
			return dbUpdateListener;
		}
		public void setDbUpdateListener(DbUpdateListener dbUpdateListener) {
			this.dbUpdateListener = dbUpdateListener;
		}
		
	}
	
	
	class SqliteDbHelper extends SQLiteOpenHelper {
		
		private DbUpdateListener mDbUpdateListener;
		public SqliteDbHelper(Context context, String name,int version, DbUpdateListener dbUpdateListener) {
			super(context, name, null, version);
			this.mDbUpdateListener = dbUpdateListener;
		}

		public void onCreate(SQLiteDatabase db) {
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(mDbUpdateListener!=null){
				mDbUpdateListener.onUpgrade(db, oldVersion, newVersion);
			}else{ //清空所有的数据信息
				Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table'", null);
				if(cursor!=null){
					while(cursor.moveToNext()){
						db.execSQL("DROP TABLE "+cursor.getString(0));
					}
				}
				if(cursor!=null){
					cursor.close();
					cursor=null;
				}
			}
		}

	}
	
	public interface DbUpdateListener{
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}

}
