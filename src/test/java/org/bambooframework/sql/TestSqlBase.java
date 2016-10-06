package org.bambooframework.sql;

import org.bambooframework.sql.parse.DataMap;
import org.bambooframework.utils.TestBase;


public class TestSqlBase extends TestBase{
	
	public static Object getValueInTableDataMap(DataMap tableDataMap,String key){
		String[] names = key.split("\\.");
		Object obj = tableDataMap;
		for(String name:names){
			obj = ((DataMap)obj).get(name);
			if(obj == null){
				return null;
			}
		}
		return obj;
	}
}
