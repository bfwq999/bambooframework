package org.bambooframework.sql;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.bambooframework.sql.metadata.Column;
import org.bambooframework.sql.metadata.Database;
import org.bambooframework.sql.metadata.Table;

public class DatabaseUtils {
	static Map<String,Database> databases = new HashMap<String, Database>();
	static Database defaultDatabase = null;
	static DataSource defaultDataSource;
	
	public static DataSource getDataSource(String dbName){
		return defaultDataSource;
	}
	public static void addDataSource(DataSource dataSource){
		defaultDataSource = dataSource;
	}
	public static void setDefaultDatabase(Database database){
		defaultDatabase = database;
	}
	public static void addDatabases(Database database){
		databases.put(database.getName(), database);
		if(defaultDatabase == null){
			defaultDatabase = database;
		}
	}
	public static void addDatabases(Database database,boolean isDefault){
		addDatabases(database);
		if(isDefault){
			defaultDatabase = database;
		}
	}
	public static Database getDatabase(String name){
		if(name == null || "".equals(name)){
			return defaultDatabase;
		}
		return databases.get(name);
	}
	/**
	 * 是否是数据库名
	 * @author lei
	 * @param name
	 * @return
	 * @date 2016年6月11日
	 * @Description:
	 */
	public static boolean isDatabaseName(String name){
		return databases.containsKey(name);
	}
	
	/**
	 * 判断是否是表名
	 * @author lei
	 * @param name 格式: 1. 表名; 2. 数据库名.表名
	 * @return
	 * @date 2016年6月11日
	 * @Description:
	 */
	public static Table getTable(String name){
		String[] names = name.split("\\.");
		if(names.length == 1){
			//不带数据库名的
			return defaultDatabase.getTable(name);
		}else if(names.length == 2){
			//带数据库名的
			Database db = databases.get(names[0]);
			if(db == null){
				return null;
			}
			return db.getTable(names[1]);
		}
		return null;
	}
	
	
	/**
	 * 判断是否是字段名
	 * @author lei
	 * @param name 有三种格式：1.表.字段；2.数据库.表.字段
	 * @return
	 * @date 2016年6月12日
	 * @Description:
	 */
	public static Column getColumn(String name){
		String[] names = name.split("\\.");
		if(names.length==1){
			return null;
		}
		if(names.length == 2){
			Table table = defaultDatabase.getTable(names[0]);
			if(table == null){
				return null;
			}
			return table.getColumn(names[1]);
		}
		if(names.length == 3){
			Database database = getDatabase(names[0]);
			if(database == null){
				return null;
			}
			Table table = defaultDatabase.getTable(names[1]);
			if(table == null){
				return null;
			}
			return table.getColumn(names[1]);
		}
		return null;
	}
}
