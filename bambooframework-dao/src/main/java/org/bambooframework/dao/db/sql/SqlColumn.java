package org.bambooframework.dao.db.sql;

import org.bambooframework.dao.db.metadata.Column;

public class SqlColumn implements Sqlable, SqlColumnable {

	String id;
	String alias;
	Column column;
	SqlTableable sqlTable;

	public SqlColumn(SqlTableable sqlTable, Column column, String alias) {
		this.sqlTable = sqlTable;
		this.column = column;
		if (alias == null) {
			this.alias = column.getName();
		}else{
			this.alias = alias;
		}
	}

	@Override
	public String toSql() {
		return this.getTable().getAlias()+"."+this.getName();
	}

	@Override
	public String getName() {
		return this.column.getName();
	}

	@Override
	public String getAlias() {
		return this.alias;
	}

	@Override
	public SqlTableable getTable() {
		return this.sqlTable;
	}

	@Override
	public SqlTableable getScope() {
		return this.sqlTable;
	}

	@Override
	public String getId() {
		if(id == null){
			id = top().generateId();
		}
		return id;
	}

	@Override
	public Query top() {
		return this.sqlTable.top();
	}


}
