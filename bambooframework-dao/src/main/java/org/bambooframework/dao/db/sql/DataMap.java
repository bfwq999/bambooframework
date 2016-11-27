package org.bambooframework.dao.db.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataMap extends HashMap<String, Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6833975368655403859L;

	@Override
	public Object get(Object key) {
		if(key != null){
			key = key.toString().toUpperCase();
		}
		return super.get(key);
	}
	@Override
	public DataMap put(String key, Object value) {
		if(key !=null ){
			key = key.toUpperCase();
		}
		super.put(key, value);
		return this;
	}
	
	public DataMap add(String key,Object value){
		if(key !=null ){
			key = key.toUpperCase();
		}
		Object obj = this.get(key);
		List<Object>  list;
		if(obj == null || !(obj instanceof List)){
			list = new ArrayList<Object>();
			this.put(key, list);
		}else{
			list = (List<Object>) obj;
		}
		list.add(value);
		return this;
	}
}
