package org.bambooframework.dao.impl.cfg;

import javax.sql.DataSource;


public class DataSourceConnectionProperties extends ConnectionProperties {

	protected DataSource dataSource;
	
	@Override
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
