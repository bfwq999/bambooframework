package org.bambooframework.sql.parse.entity;

import java.util.HashMap;
import java.util.Map;

import org.bambooframework.sql.metadata.Table;
import org.bambooframework.sql.parse.SqlExpression;

public class QueryResultTable  {

	String name;

	Table table;

	String alias;

	Map<String, Object> columns = new HashMap<String, Object>();

	public Map<String, Object> getColumns() {
		return columns;
	}

	public void addColumn(String alias, Object col) {
		this.columns.put(alias, col);
	}
	public void addOneToMany(String alias,QueryResultTable table,SqlExpression where,SqlExpression orderBy){
		this.columns.put(alias,new OneToManyTable(this,table, where, orderBy));
	}

	public void addColumn(String alias, String col) {
		this.columns.put(col, col);
	}

	public void addColumn(String col) {
		this.columns.put(col, col);
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public class OneToManyTable {
		QueryResultTable parent;
		QueryResultTable table;
		SqlExpression where;
		SqlExpression orderBy;
		public OneToManyTable() {
			super();
		}
		public OneToManyTable(QueryResultTable parent,QueryResultTable table, SqlExpression where,
				SqlExpression orderBy) {
			super();
			this.parent = parent;
			this.table = table;
			this.where = where;
			this.orderBy = orderBy;
		}
		public OneToManyTable(QueryResultTable table) {
			super();
			this.table = table;
		}
		public QueryResultTable getTable() {
			return table;
		}
		public void setTable(QueryResultTable table) {
			this.table = table;
		}
		public SqlExpression getWhere() {
			return where;
		}
		public void setWhere(SqlExpression where) {
			this.where = where;
		}
		public SqlExpression getOrderBy() {
			return orderBy;
		}
		public void setOrderBy(SqlExpression orderBy) {
			this.orderBy = orderBy;
		}
		public QueryResultTable getParent() {
			return parent;
		}
		public void setParent(QueryResultTable parent) {
			this.parent = parent;
		}
		
	}

}
