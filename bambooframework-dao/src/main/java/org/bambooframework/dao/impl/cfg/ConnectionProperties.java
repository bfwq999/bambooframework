package org.bambooframework.dao.impl.cfg;

import javax.sql.DataSource;

public abstract class ConnectionProperties {

	String catalogFilter;

	String schemaFilter;

	boolean databaseMetaDataUpdate;

	String databaseCatalog;

	String databaseSchema;

	String name;

	boolean defaultDataSource;

	String databaseType;
	
	boolean configurationDataSource; //配置文件数据库

	public abstract DataSource getDataSource();

	public String getCatalogFilter() {
		return catalogFilter;
	}

	public void setCatalogFilter(String catalogFilter) {
		this.catalogFilter = catalogFilter;
	}

	public String getSchemaFilter() {
		return schemaFilter;
	}

	public void setSchemaFilter(String schemaFilter) {
		this.schemaFilter = schemaFilter;
	}

	public boolean isDatabaseMetaDataUpdate() {
		return databaseMetaDataUpdate;
	}

	public void setDatabaseMetaDataUpdate(boolean databaseMetaDataUpdate) {
		this.databaseMetaDataUpdate = databaseMetaDataUpdate;
	}

	public String getDatabaseCatalog() {
		return databaseCatalog;
	}

	public void setDatabaseCatalog(String databaseCatalog) {
		this.databaseCatalog = databaseCatalog;
	}

	public String getDatabaseSchema() {
		return databaseSchema;
	}

	public void setDatabaseSchema(String databaseSchema) {
		this.databaseSchema = databaseSchema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefaultDataSource() {
		return defaultDataSource;
	}

	public void setDefaultDataSource(boolean defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public boolean isConfigurationDataSource() {
		return configurationDataSource;
	}

	public void setConfigurationDataSource(boolean configurationDataSource) {
		this.configurationDataSource = configurationDataSource;
	}

}
