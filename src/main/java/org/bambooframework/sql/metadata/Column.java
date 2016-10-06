/*
 *    Copyright 2009-2012 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.bambooframework.sql.metadata;

import org.bambooframework.common.CodeConstants;

public class Column {

	private int id;
	private String name;
	private int type;
	private int length;
	private int precision;
	private int tableId;
	private String defaultValue;
	private String primaryKey;
	private String comment;
	private Column foreignKey;
	private Table table;

	public Column(Table table) {
		super();
		this.table = table;
	}
	
	public Table getTable(){
		return table;
	}
	
	public Database getDatabase(){
		return table.getDatabase();
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final Column column = (Column) o;

		if (type != column.type)
			return false;
		if (name != null ? !name.equals(column.name) : column.name != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = (name != null ? name.hashCode() : 0);
		result = 29 * result + type;
		return result;
	}

	public String getName() {
		return name;
	}
	public String getFullName(){
		return this.getTable().getName() + "." + this.getName();
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}
	public boolean isPrimaryKey(){
		return CodeConstants.YESNO_Y.equals(primaryKey);
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Column getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(Column foreignKey) {
		this.foreignKey = foreignKey;
	}
}
