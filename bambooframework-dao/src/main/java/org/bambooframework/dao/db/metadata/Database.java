package org.bambooframework.dao.db.metadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Database {

	private int id;
	private String name;
	
	private final Map<String, Table> tables = new HashMap<String, Table>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void addTable(Table table) {
		tables.put(table.getName().toUpperCase(Locale.ENGLISH), table);
	}

	public Table getTable(String name) {
		return tables.get(name.toUpperCase(Locale.ENGLISH));
	}

	public String[] getTableNames() {
		return tables.keySet().toArray(new String[tables.size()]);
	}
	public Collection<Table> getTables() {
		return tables.values();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
