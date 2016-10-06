package org.bambooframework.sql.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ParamMap extends DataMap  implements Param {

	Set<Param> dependby = new LinkedHashSet<Param>();
	
	protected String name;
	
	protected Param parent;

	public Param parent() {
		return parent;
	}

	public List<Param> getPaths() {
		List<Param> paths;
		if(this.parent == null){
			paths = new ArrayList<Param>();
		}else{
			paths = this.parent.getPaths();
		}
		paths.add(this);
		return paths;
	}

	public void addDependBy(Param ref){
		dependby.add(ref);
	}
	
	public boolean dependBy(Param ref){
		return dependby.contains(ref);
	}
	
	@Override
	public Object put(String key, Object value) {
		if(key !=null ){
			key = key.toUpperCase();
		}
		if(value instanceof Param){
			Param cVal = (Param) value;
			cVal.setParent(this);
			cVal.setName(name);
		}
		return super.put(key, value);
	}
	public String getName() {
		return name;
	}
	
	public Set<java.util.Map.Entry<String, Object>> sortEntrySet() {
		List<Map.Entry<String,Object>> tblDataList = 
				new ArrayList<Map.Entry<String,Object>>(super.entrySet());
		Collections.sort(tblDataList, new Comparator<Map.Entry<String,Object>>() {
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
		return new LinkedHashSet<Map.Entry<String,Object>>(tblDataList);
	}
	public Object add(String key,ParamMap value){
		if(key !=null ){
			key = key.toUpperCase();
		}
		Object obj = this.get(key);
		List<ParamMap>  list;
		if(obj == null || !(obj instanceof List)){
			list = new ArrayList<ParamMap>();
			this.put(key, list);
		}else{
			list = (List<ParamMap>) obj;
		}
		list.add(value);
		ParamMap cVal = (ParamMap) value;
		cVal.parent = this;
		cVal.name = key;
		return list;
	}
	
	public String getTableName(){
		return name.split("\\$")[0];
	}
	public String getDbName(){
		String[] names = name.split("\\$");
		if(names.length>1){
			return names[1];
		}
		return names[0];
	}

	@Override
	public Object get(String name) {
		return super.get(name);
	}
	
	public DataMap get(int index){
		return null;
	}

	@Override
	public void setParent(Param param) {
		this.parent = param;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}
