package org.bambooframework.sql.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataMap extends HashMap<String, Object> {
	
	@Override
	public Object get(Object key) {
		if(key != null){
			key = key.toString().toUpperCase();
		}
		return super.get(key);
	}
	@Override
	public Object put(String key, Object value) {
		if(key !=null ){
			key = key.toUpperCase();
		}
		return super.put(key, value);
	}
	
	public Object add(String key,DataMap value){
		if(key !=null ){
			key = key.toUpperCase();
		}
		Object obj = this.get(key);
		List<DataMap>  list;
		if(obj == null || !(obj instanceof List)){
			list = new ArrayList<DataMap>();
			this.put(key, list);
		}else{
			list = (List<DataMap>) obj;
		}
		list.add(value);
		return list;
	}
}
