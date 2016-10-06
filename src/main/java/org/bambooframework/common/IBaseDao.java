package org.bambooframework.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IBaseDao<T> {
	
	T get(Serializable pk);
	
	void insert(T t);
	
	void update(T entity);
	
	void delete(Serializable pk);
	
	List<T> find(Map<String,?> condition);
	
	List<T> find(Map<String,?> condition,Pagination pagination);
	
}
