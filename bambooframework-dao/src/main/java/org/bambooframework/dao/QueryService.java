package org.bambooframework.dao;

import java.io.Serializable;
import java.util.Map;

public interface QueryService {
	Map<String, Object> get(String tableName,Serializable pk);
}
