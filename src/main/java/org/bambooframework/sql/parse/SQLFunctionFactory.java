package org.bambooframework.sql.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLFunctionFactory {
	private  Map<String,Class> functionRegister = new HashMap<String,Class>();
	
	
	
	public SQLFunctionFactory() {
		
	}
	
	public void init(){
		functionRegister.put("to_date",ToDate.class);
	}

	public SQLFunction getFunction(String name,String dbType,List<Object> args){
		Class clazz = functionRegister.get(name);
		SQLFunction func;
		try {
			func = (SQLFunction) clazz.newInstance();
			func.setArgs(args);
			func.setDatabaseType(dbType);
			return func;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
