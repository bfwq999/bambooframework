package org.bambooframework.dao.impl.cfg;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.bambooframework.core.exception.BambooException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcConnectionProperties extends ConnectionProperties{
	
	static Logger log = LoggerFactory.getLogger(JdbcConnectionProperties.class);
	protected String databaseType;
	protected String driverClassName = "org.h2.Driver";
	protected String url = "jdbc:h2:tcp://localhost/~/activiti";

	protected String username = "sa";
	protected String password = "";

	protected int maxActiveConnections;
	protected int maxIdleConnections;
	protected int maxCheckoutTime;
	protected int maxWaitTime;
	protected boolean pingEnabled = false;
	protected String pingQuery = null;
	
	protected DataSource dataSource;
	
	
	@Override
	public DataSource getDataSource() {
		if(dataSource != null){
			return dataSource;
		}
		if ( driverClassName == null || url == null
				|| username == null) {
			throw new BambooException(
					"DataSource or JDBC properties have to be specified in a process engine configuration");
		}
		if(log.isDebugEnabled()){
			log.debug("initializing datasource to db: {}", url);
		}
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(driverClassName);
		basicDataSource.setUrl(url);
		basicDataSource.setUsername(username);
		basicDataSource.setPassword(password);
		dataSource = basicDataSource;
		return dataSource;
	}
	
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getMaxActiveConnections() {
		return maxActiveConnections;
	}
	public void setMaxActiveConnections(int maxActiveConnections) {
		this.maxActiveConnections = maxActiveConnections;
	}
	public int getMaxIdleConnections() {
		return maxIdleConnections;
	}
	public void setMaxIdleConnections(int maxIdleConnections) {
		this.maxIdleConnections = maxIdleConnections;
	}
	public int getMaxCheckoutTime() {
		return maxCheckoutTime;
	}
	public void setMaxCheckoutTime(int maxCheckoutTime) {
		this.maxCheckoutTime = maxCheckoutTime;
	}
	public int getMaxWaitTime() {
		return maxWaitTime;
	}
	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}
	public boolean isPingEnabled() {
		return pingEnabled;
	}
	public void setPingEnabled(boolean pingEnabled) {
		this.pingEnabled = pingEnabled;
	}
	public String getPingQuery() {
		return pingQuery;
	}
	public void setPingQuery(String pingQuery) {
		this.pingQuery = pingQuery;
	}
	public int getPingConnectionNotUsedFor() {
		return pingConnectionNotUsedFor;
	}
	public void setPingConnectionNotUsedFor(int pingConnectionNotUsedFor) {
		this.pingConnectionNotUsedFor = pingConnectionNotUsedFor;
	}
	public int getDefaultTransactionIsolationLevel() {
		return defaultTransactionIsolationLevel;
	}
	public void setDefaultTransactionIsolationLevel(
			int defaultTransactionIsolationLevel) {
		this.defaultTransactionIsolationLevel = defaultTransactionIsolationLevel;
	}
	protected int pingConnectionNotUsedFor;
	protected int defaultTransactionIsolationLevel;
	
	public String getDatabaseType() {
		return databaseType;
	}
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
	
}
