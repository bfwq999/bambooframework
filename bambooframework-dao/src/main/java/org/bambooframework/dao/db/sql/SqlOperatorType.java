package org.bambooframework.dao.db.sql;

public enum SqlOperatorType {
	EQ("equal"),
	EXISTS("EXISTS"),
	NOTEXISTS("NOT EXISTS"),
	IN("IN"),
	NOTIN("NOT IN"),
	LIKE("LIKE"),
	NOTLIKE("NOT LIKE");
	
	String value;

	private SqlOperatorType(String value) {
		this.value = value;
	}
}
