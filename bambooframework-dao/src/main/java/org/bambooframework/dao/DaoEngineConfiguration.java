package org.bambooframework.dao;

import javax.sql.DataSource;

import org.bambooframework.dao.db.IdGenerator;
import org.bambooframework.dao.db.transaction.TransactionFactory;
import org.bambooframework.dao.impl.interceptor.CommandExecutor;


public interface DaoEngineConfiguration {
	
	CommandExecutor getCommandExecutor();
	
	String getDaoEngineName();
	
	DataSource getDataSource(String name);
	
	DataSource getDefaultDataSource();
	
	public String getDefaultDataSourceName();
	
	public TransactionFactory getTransactionFactory();
	
	public IdGenerator getIdGenerator();
	
}
