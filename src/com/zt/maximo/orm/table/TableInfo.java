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
package com.zt.maximo.orm.table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import com.zt.maximo.orm.exception.DbException;
import com.zt.maximo.orm.reflect.ClassUtils;
import com.zt.maximo.orm.reflect.FieldUtils;



public class TableInfo {

	private String className;
	private String tableName;
	
	/**
	 * 表主键
	 */
	private TableId id;
	
	public final HashMap<String, Property> propertyMap = new HashMap<String, Property>();
	
	private boolean checkDatabese;//在对实体进行数据库操作的时候查询是否已经有表了，只需查询一遍，用此标示
	
	
	private static final HashMap<String, TableInfo> tableInfoMap = new HashMap<String, TableInfo>();
	
	private TableInfo(){}
	
	/**
	 * 根据类获取对应的表结构
	 * 1.获取主键信息
	 * 2.获取其他column信息
	 * @param clazz
	 * @return
	 */
	public static  TableInfo get(Class<?> clazz){
		if(clazz == null) 
			throw new DbException("table info get error,because the clazz is null");
		
		TableInfo tableInfo = tableInfoMap.get(clazz.getName());
		if( tableInfo == null ){
			tableInfo = new TableInfo();
			
			tableInfo.setTableName(ClassUtils.getTableName(clazz));
			tableInfo.setClassName(clazz.getName());
			
			Field idField = ClassUtils.getPrimaryKeyField(clazz);
			if(idField != null){
				TableId id = new TableId();
				id.setColumn(FieldUtils.getColumnByField(idField));
				id.setFieldName(idField.getName());
				id.setSet(FieldUtils.getFieldSetMethod(clazz, idField));
				id.setGet(FieldUtils.getFieldGetMethod(clazz, idField));
				id.setDataType(idField.getType());
				
				tableInfo.setId(id);
			}else{
				throw new DbException("the class["+clazz+"]'s idField is null");
			}
			
			List<Property> pList = ClassUtils.getPropertyList(clazz);
			if(pList!=null){
				for(Property p : pList){
					if(p!=null)
						tableInfo.propertyMap.put(p.getColumn(), p);
				}
			}
			
			tableInfoMap.put(clazz.getName(), tableInfo);
		}
		
		if(tableInfo == null ) 
			throw new DbException("the class["+clazz+"]'s table is null");
		
		return tableInfo;
	}
	
	
	public static TableInfo get(String className){
		try {
			return get(Class.forName(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 获取表主键信息
	 * @return
	 */
	public TableId getId() {
		return id;
	}

	/**
	 * 设置表主键信息
	 * @param id
	 */
	public void setId(TableId id) {
		this.id = id;
	}

	public boolean isCheckDatabese() {
		return checkDatabese;
	}

	public void setCheckDatabese(boolean checkDatabese) {
		this.checkDatabese = checkDatabese;
	}

	
	
}
