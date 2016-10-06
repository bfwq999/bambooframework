package org.bambooframework.sql.parse;

import java.util.Map;

public interface Parser {
	public Param parser(Map<String, Object> data);
}
