package org.bambooframework.dao.db.transaction.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.bambooframework.dao.db.DataSourceProxy;
import org.bambooframework.dao.db.transaction.Transaction;
import org.bambooframework.dao.db.transaction.TransactionIsolationLevel;
import org.bambooframework.dao.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcTransaction implements Transaction {

	private static final Logger log = LoggerFactory
			.getLogger(JdbcTransaction.class);

	Map<String, DataSourceProxy> dataSourceProxies = new HashMap<String, DataSourceProxy>();
	String defaultDataSourceName;
	protected Map<String, Connection> connections = new HashMap<String, Connection>(1);
	protected TransactionIsolationLevel level;
	protected boolean autoCommmit;

	public JdbcTransaction(Collection<DataSourceProxy> dataSourceProxiess,
			TransactionIsolationLevel desiredLevel, boolean desiredAutoCommit) {
		for (DataSourceProxy dataSourceProxy : dataSourceProxiess) {
			dataSourceProxies.put(dataSourceProxy.getName(), dataSourceProxy);
			if (dataSourceProxy.isDefaultDataSource()) {
				defaultDataSourceName = dataSourceProxy.getName();
			}
		}
		level = desiredLevel;
		autoCommmit = desiredAutoCommit;
	}

	public Connection getConnection() throws SQLException {
		return getConnection(defaultDataSourceName);
	}

	@Override
	public Connection getConnection(String database) throws SQLException {
		if(database == null){
			database = defaultDataSourceName;
		}
		Connection conn = connections.get(database);
		if (conn == null) {
			conn = openConnection(database);
			connections.put(database, conn);
		}
		return conn;
	}

	protected Connection openConnection(String database) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("Opening JDBC Connection");
		}
		DataSource dataSource = dataSourceProxies.get(database);
		if(dataSource == null){
			throw new SQLException("Cannot find DataSource:"+database);
		}
		Connection connection = dataSource.getConnection();
		if (level != null) {
			connection.setTransactionIsolation(level.getLevel());
		}
		setDesiredAutoCommit(connection,autoCommmit);
		return connection;
	}

	public void commit() throws SQLException {
		for(Connection connection:connections.values()){
			if (connection != null && !connection.getAutoCommit()) {
				if (log.isDebugEnabled()) {
					log.debug("Committing JDBC Connection [" + connection + "]");
				}
				connection.commit();
			}
		}
	}

	public void rollback() throws SQLException {
		for(Connection connection:connections.values()){
			if (connection != null && !connection.getAutoCommit()) {
				if (log.isDebugEnabled()) {
					log.debug("Rolling back JDBC Connection [" + connection + "]");
				}
				connection.rollback();
			}
		}
	}

	public void close() throws SQLException {
		for(Connection connection:connections.values()){
			if (connection != null) {
				resetAutoCommit(connection);
				if (log.isDebugEnabled()) {
					log.debug("Closing JDBC Connection [" + connection + "]");
				}
				connection.close();
			}
		}
	}

	protected void setDesiredAutoCommit(Connection connection,boolean desiredAutoCommit) {
		try {
			if (connection.getAutoCommit() != desiredAutoCommit) {
				if (log.isDebugEnabled()) {
					log.debug("Setting autocommit to " + desiredAutoCommit
							+ " on JDBC Connection [" + connection + "]");
				}
				connection.setAutoCommit(desiredAutoCommit);
			}
		} catch (SQLException e) {
			// Only a very poorly implemented driver would fail here,
			// and there's not much we can do about that.
			throw new TransactionException(
					"Error configuring AutoCommit.  "
							+ "Your driver may not support getAutoCommit() or setAutoCommit(). "
							+ "Requested setting: " + desiredAutoCommit
							+ ".  Cause: ", e);
		}
	}

	protected void resetAutoCommit(Connection connection) {
		try {
			if (!connection.getAutoCommit()) {
				// MyBatis does not call commit/rollback on a connection if just
				// selects were performed.
				// Some databases start transactions with select statements
				// and they mandate a commit/rollback before closing the
				// connection.
				// A workaround is setting the autocommit to true before closing
				// the connection.
				// Sybase throws an exception here.
				if (log.isDebugEnabled()) {
					log.debug("Resetting autocommit to true on JDBC Connection ["
							+ connection + "]");
				}
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			log.debug("Error resetting autocommit to true "
					+ "before closing the connection.  Cause: " + e);
		}
	}

}
