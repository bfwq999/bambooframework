package org.bambooframework.sql.parse.entity;

import java.util.ArrayList;
import java.util.List;

import org.bambooframework.sql.parse.SqlExpression;

public class QueryResultView extends QueryResultTable{
	
	QueryResultTable result;
	
	List<JoinTable> joins = new ArrayList<JoinTable>(0);
	
	SqlExpression where;
	
	SqlExpression orderBy;
	
	SqlExpression groupBy;
	
	public void addJoin(String type,QueryResultTable table,SqlExpression on){
		this.joins.add(new JoinTable(type, table,on));
	}
	
	public class JoinTable {
		String type;
		
		QueryResultTable table;
		
		SqlExpression on;
		
		public JoinTable(String type, QueryResultTable table,SqlExpression on) {
			this.type = type;
			this.table = table;
			this.on = on;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public QueryResultTable getTable() {
			return table;
		}

		public void setTable(QueryResultTable table) {
			this.table = table;
		}

		public SqlExpression getOn() {
			return on;
		}

		public void setOn(SqlExpression on) {
			this.on = on;
		}
	}

	public SqlExpression getWhere() {
		return where;
	}

	public void setWhere(SqlExpression where) {
		this.where = where;
	}

	public QueryResultTable getResult() {
		return result;
	}

	public void setResult(QueryResultTable result) {
		this.result = result;
	}

	public SqlExpression getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(SqlExpression orderBy) {
		this.orderBy = orderBy;
	}

	public SqlExpression getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(SqlExpression groupBy) {
		this.groupBy = groupBy;
	}

	public List<JoinTable> getJoins() {
		return joins;
	}
	int nameIndex = 1;
	String getNewAlias(){
		return "ARG"+(nameIndex++);
	}
}
