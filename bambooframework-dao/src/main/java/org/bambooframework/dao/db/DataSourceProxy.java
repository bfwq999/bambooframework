package org.bambooframework.dao.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.bambooframework.dao.impl.cfg.ConnectionProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataSourceProxy implements DataSource {

	private static Logger log = LoggerFactory.getLogger(DataSourceProxy.class);

	protected DataSource dataSource;
	
	ConnectionProperties connectionProperties;
	
	public DataSourceProxy(ConnectionProperties connectionProperties) {
		this.connectionProperties = connectionProperties;
		this.dataSource = connectionProperties.getDataSource();
	}
	

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		dataSource.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		dataSource.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return dataSource.getLoginTimeout();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return dataSource.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return dataSource.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return dataSource.getConnection(username, password);
	}


	public String getCatalogFilter() {
		return connectionProperties.getCatalogFilter();
	}

	public String getSchemaFilter() {
		return connectionProperties.getSchemaFilter();
	}

	public boolean isDatabaseMetaDataUpdate() {
		return connectionProperties.isDatabaseMetaDataUpdate();
	}

	public String getDatabaseCatalog() {
		return connectionProperties.getDatabaseCatalog();
	}

	public String getDatabaseSchema() {
		return connectionProperties.getDatabaseSchema();
	}

	public String getName() {
		return connectionProperties.getName();
	}

	public boolean isDefaultDataSource() {
		return connectionProperties.isDefaultDataSource();
	}

	public String getDatabaseType() {
		return connectionProperties.getDatabaseType();
	}

	public boolean isConfigurationDataSource() {
		return connectionProperties.isConfigurationDataSource();
	}

}
