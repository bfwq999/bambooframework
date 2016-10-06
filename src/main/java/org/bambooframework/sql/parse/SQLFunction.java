package org.bambooframework.sql.parse;

import java.util.List;

public abstract class SQLFunction implements Sql {

	protected List<Object> args;
	
	protected String dbType;

	public void setArgs(List<Object> args) {
		this.args = args;
	}

	public void setDatabaseType(String dbType) {
		this.dbType = dbType;
	}
	
}
