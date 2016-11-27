package org.bambooframework.dao.db.metadata;

import java.util.HashMap;
import java.util.Map;

import org.bambooframework.core.util.StringUtils;

/**
 * 数据库信息
 * @author lei
 * @date 2016年11月22日
 * @Description:
 */
public class DatabaseMetaData {

	protected static Map<String, Database> databases = new HashMap<String, Database>(1);

	protected static Database defaultDatabase;

	public static void addDatabase(String name, Database database) {
		databases.put(name, database);
	}

	public static void addDatabase(String name, Database database, boolean isDefault) {
		databases.put(name, database);
		if (isDefault) {
			defaultDatabase = database;
		}
	}

	public Database getDefaultDatabase() {
		return defaultDatabase;
	}

	public static void clear() {
		databases.clear();
		defaultDatabase = null;
	}

	/**
	 * 获取数据库
	 * @author lei
	 * @param name 如果为空，取默认数据库
	 * @return
	 * @date 2016年11月22日
	 * @Description:
	 */
	public static Database getDatabase(String name) {
		if (StringUtils.isEmpty(name)) {
			return defaultDatabase;
		}
		return databases.get(name);
	}

	/**
	 * 获取数据库
	 * @author lei
	 * @param dbName 如果dbName为空，则取默认数据库的表
	 * @param tableName
	 * @return
	 * @date 2016年11月22日
	 * @Description:
	 */
	public static Table getTable(String dbName, String tableName) {
		Database db = getDatabase(dbName);
		if (db == null) {
			return null;
		}
		return db.getTable(tableName);
	}
}
