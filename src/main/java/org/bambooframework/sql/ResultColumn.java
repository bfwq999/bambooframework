package org.bambooframework.sql;

import org.bambooframework.sql.metadata.Column;

public class ResultColumn {
	Column column;
	ResultTable resultTable;
	String alias;
	private String id;	

	public ResultColumn(ResultTable resultTable, Column column) {
		super();
		this.resultTable = resultTable;
		this.column = column;
	}

	public String getName() {
		return this.column.getName();
	}

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}