package org.bambooframework.sql.parse.entity;

import org.bambooframework.sql.ResultTable;
import org.bambooframework.sql.metadata.Column;

public class QueryResultColumn {
	
	Column column;
	
	ResultTable resultTable;
	
	String alias;
	
	String name;

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public ResultTable getResultTable() {
		return resultTable;
	}

	public void setResultTable(ResultTable resultTable) {
		this.resultTable = resultTable;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
