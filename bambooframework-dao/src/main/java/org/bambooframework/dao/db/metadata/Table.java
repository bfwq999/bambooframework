package org.bambooframework.dao.db.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Table {
	private int id;
	private String name;
	private String catalog;
	private String schema;
	private final Map<String, Column> columns = new HashMap<String, Column>();
	private Column primaryKey;
	private Database database;
	
	/**
	 * 
	 * @author lei
	 * @param targetColumnName 格式为表名.字段名
	 * @return
	 * @date 2016年6月14日
	 * @Description:
	 */
	public Column getForeignColumn(String targetColumnName){
		String[] names = targetColumnName.split("\\.");
		for(Column col:columns.values()){
			if(col.getForeignKey() != null 
					&& names[0].equals(col.getForeignKey().getTable().getName())
					&& names[1].equals(col.getForeignKey().getName())){
				return col;
			}
		}
		return null;
	}
	/**
	 * 
	 * @author lei
	 * @param targetColumnName 格式为表名.字段名
	 * @return
	 * @date 2016年6月14日
	 * @Description:
	 */
	public Column getForeignColumn(Column targetCol){
		String tableName = targetCol.getTable().getName();
		String colName = targetCol.getName();
		for(Column col:columns.values()){
			if(col.getForeignKey() != null 
					&& tableName.equals(col.getForeignKey().getTable().getName())
					&& colName.equals(col.getForeignKey().getName())){
				return col;
			}
		}
		return null;
	}
	/**
	 * 
	 * @author lei
	 * @param targetColumnName 格式为表名.字段名
	 * @return
	 * @date 2016年6月14日
	 * @Description:
	 */
	public List<Column> getColumnRef(String refTableName){
		List<Column> cols = new ArrayList<Column>();
		for(Column col:columns.values()){
			if(col.getForeignKey() != null 
					&& refTableName.equals(col.getForeignKey().getTable().getName())){
				cols.add(col);
			}
		}
		return cols;
	}
	
	public Table(Database database) {
		this.database = database;
	}
	public Database getDatabase(){
		return database;
	}
	public String getName() {
		return name;
	}
	
	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void addColumn(Column col) {
		columns.put(col.getName().toUpperCase(Locale.ENGLISH), col);
	}

	public Column getColumn(String name) {
		return columns.get(name.toUpperCase(Locale.ENGLISH));
	}

	public String[] getColumnNames() {
		return columns.keySet().toArray(new String[columns.size()]);
	}

	public void setPrimaryKey(Column column) {
		primaryKey = column;
	}

	public Column getPrimaryKey() {
		return primaryKey;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Collection<Column> getColumns(){
		return columns.values();
	}
	public void setName(String name) {
		this.name = name;
	}
}
