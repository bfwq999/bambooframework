package org.bambooframework.dao.db.transaction.jdbc;

import java.util.Collection;

import org.bambooframework.dao.db.DataSourceProxy;
import org.bambooframework.dao.db.transaction.Transaction;
import org.bambooframework.dao.db.transaction.TransactionFactory;
import org.bambooframework.dao.db.transaction.TransactionIsolationLevel;

public class JdbcTransactionFactory implements TransactionFactory {
	
	Collection<DataSourceProxy> dataSourceProxies;
	
	public JdbcTransactionFactory(Collection<DataSourceProxy> dataSourceProxies) {
		super();
		this.dataSourceProxies = dataSourceProxies;
	}


	public Transaction newTransaction(TransactionIsolationLevel level, boolean autoCommit) {
		return new JdbcTransaction(dataSourceProxies, level, autoCommit);
	}
}
