package org.bambooframework.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bambooframework.sql.metadata.Table;

public class ResultTable {
	Map<String, ResultColumn> columns = new HashMap<String, ResultColumn>();
	ResultTable parent;
	String alias;
	Table table;
	private String id;

	Map<String, ResultTable> foreignTables = new HashMap<String, ResultTable>();
	Map<String, ResultTable> oneToMany = new HashMap<String, ResultTable>();

	public ResultTable(Table table) {
		this.table = table;
	}

	public String getName() {
		return table.getName();
	}

	public ResultTable getParent() {
		return parent;
	}

	public void setParent(ResultTable parent) {
		this.parent = parent;
	}
	public ResultColumn getPrimaryColumn(){
		for(ResultColumn col:columns.values()){
			if(col.getColumn().isPrimaryKey()){
				return col;
			}
		}
		return null;
	}

	public List<ResultTable> getPaths() {
		List<ResultTable> paths;
		if (this.parent == null) {
			paths = new ArrayList<ResultTable>();
		} else {
			paths = this.parent.getPaths();
		}
		paths.add(this);
		return paths;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void addColumns(String colName, ResultColumn column) {
		column.setId(colName);
		columns.put(colName, column);
	}

	public void addForeignTable(String colName, ResultTable table) {
		table.setId(colName);
		foreignTables.put(colName, table);
	}

	public void addOneToMany(String tableName, ResultTable table) {
		table.setId(tableName);
		oneToMany.put(tableName, table);
	}
	public boolean isForeign(String key){
		return foreignTables.containsKey(key);
	}
	public boolean isOneTonMany(String key){
		return oneToMany.containsKey(key);
	}

	public Collection<ResultColumn> columns() {
		return columns.values();
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Map<String, ResultTable> getForeignTables() {
		return foreignTables;
	}
	public Collection<ResultTable> oneToManys() {
		return oneToMany.values();
	}

	public String getId() {
		return id == null?alias:id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

