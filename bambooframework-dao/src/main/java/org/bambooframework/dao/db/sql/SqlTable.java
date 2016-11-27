package org.bambooframework.dao.db.sql;

import java.util.ArrayList;
import java.util.List;

import org.bambooframework.core.util.StringUtils;
import org.bambooframework.dao.db.metadata.Column;
import org.bambooframework.dao.db.metadata.DatabaseMetaData;
import org.bambooframework.dao.db.metadata.Table;
import org.bambooframework.dao.db.sql.parser.SqlParser;
import org.bambooframework.dao.exception.ParseSqlDataException;

public class SqlTable extends SqlBuilder implements Sqlable, SqlTableable {

	String alias;

	Table table;

	List<SqlColumnable> columns;
	
	/**
	 * 返回值中没有定义，但在查询条件中被引用的
	 */
	List<SqlColumnable> extendColumns;
	
	List<SqlRelTable> relTables;
	
	/**
	 * <pre>
	 * 格式:
	 * 1. 表名
	 * 2. {
	 *  	name:"表名",
	 *  	alias:"别名",
	 *  	columns:[colname,{name:"",alias:""},{alias:"",func:""},{查询语句}]
	 * 	  }
	 * </pre>
	 * @param source
	 * @param parent
	 */
	public SqlTable(DataMap source, Query parent) {
		super(source, parent, parent);
	}
	public SqlTable(String source, Query parent) {
		super(source, parent, parent);
	}
	public SqlTable(DataMap source, SqlTable parent) {
		super(source, parent, parent);
	}
	public SqlTable(String source, SqlTable parent) {
		super(source, parent, parent);
	}

	protected void compile() {
		this.columns = new ArrayList<SqlColumnable>(0);
		this.extendColumns = new ArrayList<SqlColumnable>(0);
		this.relTables = new ArrayList<SqlRelTable>();
		String database = top.getDatabase();
		if(source instanceof String){
			//表名是个字符串
			this.table = DatabaseMetaData.getTable(database, (String) source);
			if(this.table == null){
				throw new ParseSqlDataException("表不存在："+source);
			}
			for(Column col:this.table.getColumns()){
				this.columns.add(new SqlColumn(this,col,null));
			}
		}else{
			DataMap tableData = (DataMap) source;
			String name = (String) tableData.get("name");
			name = StringUtils.trim(name);
			this.table = DatabaseMetaData.getTable(database,name);
			if(this.table == null){
				throw new ParseSqlDataException("表不存在："+database+"."+name);
			}
			String alias = (String) tableData.get("alias");
			if(StringUtils.isEmpty(alias)){
				this.alias = name;
			}else{
				this.alias = StringUtils.trim(alias);
			}
			List<Object> columns = (List<Object>) tableData.get("columns");
			for(Object col:columns){
				Object resultCol = SqlParser.parseSqlTableColumn(this,this.getTable(),col);
				if(resultCol instanceof SqlColumnable){
					this.columns.add((SqlColumnable) resultCol);
				}else{
					this.relTables.add((SqlRelTable) resultCol);
				}
			}
		}
	}

	@Override
	public String toSql() {
		return null;
	}

	public String getAlias() {
		return this.alias;
	}
	@Override
	protected void link() {
		for(SqlColumnable column:this.columns){
			if(column instanceof Query){
				((Query)column).link();
			}
		}
	}
	public Table getTable() {
		return table;
	}
	public List<SqlColumnable> getColumns() {
		return columns;
	}
	
	@Override
	public SqlColumnable getColumn(String colName){
		for(SqlColumnable col:columns){
			if(col.getName().equals(colName)){
				return col;
			}
		}
		for(SqlColumnable col:extendColumns){
			if(col.getName().equals(colName)){
				return col;
			}
		}
		//返回字段中没有，则从表中获取
		Column column = this.table.getColumn(colName);
		if(column!=null){
			SqlColumn col = new SqlColumn(this, column, null);
			extendColumns.add(col);
			return col;
		}
		return null;
	}
	@Override
	public SqlColumnable findColumn(String colName,Sqlable caller) {
		SqlColumnable sqlColumn = null;
		String[] colNames =  colName.split("\\.");
		if(colNames.length==2){
			if(colNames[0].equals(this.alias)){
				sqlColumn = getColumn(colNames[1]);
			}
		}else{
			sqlColumn = getColumn(colName);
		}
		if(sqlColumn == null && this.scope != caller){
			return this.scope.findColumn(colName, this);
		}
		return sqlColumn;
	}
	@Override
	public String getName() {
		return table.getName();
	}
}
