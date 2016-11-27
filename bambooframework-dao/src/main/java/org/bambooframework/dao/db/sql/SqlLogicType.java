package org.bambooframework.dao.db.sql;

public enum SqlLogicType {
	AND("AND"),
	OR("OR");
	
	String value;

	private SqlLogicType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	
}
