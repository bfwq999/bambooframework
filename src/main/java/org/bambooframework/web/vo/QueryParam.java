package org.bambooframework.web.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bambooframework.config.QueryParamSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 查询参数对象
 * @author lei
 * @date 2015年8月29日
 * @Description:
 * 	类似map功能
 */
@JsonSerialize(using = QueryParamSerializer.class) 
public class QueryParam  implements Serializable {
	private static final long serialVersionUID = -2508439954113536022L;
	
	HashMap<String, Object> innerMap;
	
    public QueryParam(int initialCapacity) {
    	innerMap = new HashMap<String, Object>(initialCapacity);
    }

    public QueryParam() {
    	innerMap = new HashMap<String, Object>();
    }

    public int size() {
        return innerMap.size();
    }
    public boolean isEmpty() {
    	 return innerMap.isEmpty();
    }
    
    public Object get(Object key) {
       return innerMap.get(key);
    }

    public boolean containsKey(Object key) {
        return innerMap.containsKey(key);
    }

    public Object put(String key, Object value) {
    	if(value!=null && value instanceof String && "".equals(((String)value).trim())){
    		//将空字符串强制转为null
    		value = null;
    	}
    	return innerMap.put(key, value);
    }
    
    public Set<String> keySet() {
        return innerMap.keySet();
    }
    
    public Set<Map.Entry<String,Object>> entrySet() {
        return innerMap.entrySet();
    }

	public HashMap<String, Object> getInnerMap() {
		return innerMap;
	}
	
	/**
	 * 得到一个值是str类型的map
	 * @author lei
	 * @return
	 * @date 2015年10月8日
	 * @Description:
	 */
	public HashMap<String,String> getStringMap(){
		HashMap<String,String> map = new HashMap<String,String>();
		for(Map.Entry<String, Object> m:innerMap.entrySet()){
			map.put(m.getKey(), m.getValue()==null?null:m.getValue().toString());
		}
		return map;
	}
}
