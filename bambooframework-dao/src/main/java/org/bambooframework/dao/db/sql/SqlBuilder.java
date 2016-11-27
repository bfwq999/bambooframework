package org.bambooframework.dao.db.sql;


public abstract class SqlBuilder implements Sqlable {
	
	Object source;
	
	SqlBuilder parent;
	
	SqlTableable scope;
	
	Query top;

	public SqlBuilder(Object source, SqlBuilder parent, SqlTableable scope) {
		this.source = source;
		this.parent = parent;
		this.scope = scope;
		if(parent != null){
			this.top = parent.top();
		}else{
			this.top =  (Query)this;
		}
		compile();
		if(parent == null){
			link();
		}
	}
	
	protected abstract void compile();
	
	protected abstract void link();
	
	public SqlBuilder getParent() {
		return parent;
	}

	public String getDatabase() {
		return top.getDatabase();
	}
	
	public SqlTableable getScope(){
		return scope;
	}
	
	public Query top(){
		return top;
	}

	@Override
	public String toSql() {
		return null;
	}
}
