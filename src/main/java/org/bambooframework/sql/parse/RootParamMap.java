package org.bambooframework.sql.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class RootParamMap extends ParamMap {
	Map<String,Object> befores = new LinkedHashMap<String, Object>();
	Map<String,Object> afters = new LinkedHashMap<String, Object>();
	Map<String,Object> operate = new LinkedHashMap<String, Object>();
	
	public Object put(String type,String key, Param value) {
		if("before".equals(type)){
			befores.put(key, value);
		}else if("after".equals(type)){
			befores.put(key, value);
		}else{
			befores.put(key, value);
		}
		return super.put(key, value);
	}
	
	private List<Map.Entry<String, Object>> sortEntrySet(Map<String,Object> params){
		List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String,Object>>(params.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String,Object>>() {
			@Override
			public int compare(Entry<String, Object> o1,
					Entry<String, Object> o2) {
				Object val1 = o1.getValue();
				Object val2 = o2.getValue();
				if (val1 instanceof Param && val2 instanceof Param) {
					Param t1 = (Param) val1;
					Param t2 = (Param) val2;
					if(t1.dependBy(t2)){
						return -1;
					}
					return 0;
				}
				return 0;
			}
		});
		return  list;
	}
	
	public Set<Map.Entry<String, Object>> sortEntrySet() {
		Set<Map.Entry<String, Object>> sets =new LinkedHashSet<Map.Entry<String,Object>>();
		sets.addAll(sortEntrySet(befores));
		sets.addAll(sortEntrySet(befores));
		sets.addAll(sortEntrySet(befores));
		return sets;
	}
	
	public void clearOthers(){
		for(String key:befores.keySet()){
			this.remove(key);
		}
		for(String key:afters.keySet()){
			this.remove(key);
		}
	}
	
}
