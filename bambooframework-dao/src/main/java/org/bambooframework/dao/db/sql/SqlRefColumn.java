package org.bambooframework.dao.db.sql;

/**
 * 字段引用
 * @author lei
 * @date 2016年11月27日
 * @Description:
 */
public class SqlRefColumn extends SqlBuilder implements SqlColumnable {
	
	protected SqlColumnable column;
	
	public SqlRefColumn(String source, SqlBuilder parent, SqlTableable scope) {
		super(source, parent, scope);
	}
	public SqlRefColumn(DataMap source, SqlBuilder parent,
			SqlTableable scope) {
		super(source, parent, scope);
	}

	@Override
	public String toSql() {
		return this.column.getTable().getAlias() + "." + this.column.getName();
	}
	@Override
	protected void compile() {
	}
	@Override
	protected void link() {
		if(source instanceof String){
			this.column = this.scope.findColumn((String)source,this);
		}else{
			DataMap source = (DataMap) this.source;
		}
	}
	@Override
	public String getName() {
		return this.column.getName();
	}
	@Override
	public String getAlias() {
		return this.column.getName();
	}
	@Override
	public SqlTableable getTable() {
		return this.column.getTable();
	}
	@Override
	public String getId() {
		return this.column.getId();
	}
}
