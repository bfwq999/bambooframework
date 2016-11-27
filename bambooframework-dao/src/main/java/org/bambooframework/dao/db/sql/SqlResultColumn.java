package org.bambooframework.dao.db.sql;


public class SqlResultColumn implements SqlColumnable {
	
	protected SqlTableable table;
	protected SqlColumnable column;
	String id;
	
	public SqlResultColumn(SqlTableable table,SqlColumnable column) {
		this.table = table;
		this.column = column;
	}
	
	@Override
	public String toSql() {
		StringBuffer sql = new StringBuffer();
		sql.append(this.table.getAlias())
			.append(".")
			.append(this.getName());
		return sql.toString();
	}

	@Override
	public String getName() {
		return this.column.getId();
	}

	@Override
	public String getAlias() {
		return column.getAlias();
	}

	@Override
	public SqlTableable getTable() {
		return this.table;
	}

	public SqlColumnable getColumn() {
		return column;
	}

	@Override
	public SqlTableable getScope() {
		return null;
	}

	@Override
	public Query top() {
		return this.table.top();
	}

	@Override
	public String getId() {
		if(id == null){
			id = top().generateId();
		}
		return id;
	}
}
