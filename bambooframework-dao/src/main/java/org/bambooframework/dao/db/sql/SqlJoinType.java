package org.bambooframework.dao.db.sql;

public enum SqlJoinType {
	INNER("INNER JOIN"),
	LEFT("LEFT OUTER JOIN"),
	RIGHT("RIGHT OUTER JOIN"),
	FULL("FULL JOIN"),
	CROSS("CROSS JOIN");

	private SqlJoinType(String joinTypeValue) {
		this.joinTypeValue = joinTypeValue;
	}

	private String joinTypeValue;

	public String getJoinTypeValue() {
		return joinTypeValue;
	}

	public void setJoinTypeValue(String joinTypeValue) {
		this.joinTypeValue = joinTypeValue;
	}
}
