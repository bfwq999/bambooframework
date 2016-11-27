package org.bambooframework.dao.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.bambooframework.core.exception.BambooException;
import org.bambooframework.core.util.StringUtils;
import org.bambooframework.dao.DaoEngine;
import org.bambooframework.dao.DaoEngineConfiguration;
import org.bambooframework.dao.DaoEngines;
import org.bambooframework.dao.db.DataSourceProxy;
import org.bambooframework.dao.db.IdGenerator;
import org.bambooframework.dao.db.transaction.TransactionFactory;
import org.bambooframework.dao.db.transaction.jdbc.JdbcTransactionFactory;
import org.bambooframework.dao.impl.cfg.BeansConfigurationHelper;
import org.bambooframework.dao.impl.cfg.CommandExecutorImpl;
import org.bambooframework.dao.impl.cfg.ConnectionProperties;
import org.bambooframework.dao.impl.cfg.DatabaseMetaDataLoader;
import org.bambooframework.dao.impl.cfg.DefaultDatabaseMetaDataLoader;
import org.bambooframework.dao.impl.cfg.TransactionContextFactory;
import org.bambooframework.dao.impl.cfg.standalone.StandaloneJdbcTransactionContextFactory;
import org.bambooframework.dao.impl.interceptor.CommandConfig;
import org.bambooframework.dao.impl.interceptor.CommandContextFactory;
import org.bambooframework.dao.impl.interceptor.CommandContextInterceptor;
import org.bambooframework.dao.impl.interceptor.CommandExecutor;
import org.bambooframework.dao.impl.interceptor.CommandInterceptor;
import org.bambooframework.dao.impl.interceptor.CommandInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoEngineConfigurationImpl implements
		DaoEngineConfiguration {

	private static Logger log = LoggerFactory
			.getLogger(DaoEngineConfigurationImpl.class);

	protected String daoEngineName = DaoEngines.NAME_DEFAULT;
	
	protected List<ConnectionProperties> connectionProperties;
	
	protected Map<String,DataSourceProxy> dataSourceProxies = new HashMap<String, DataSourceProxy>(1);
	
	protected String defaultDataSourceName;
	protected String configurationDataSourceName;
	
	protected Map<Object, Object> beans;

	protected CommandExecutor commandExecutor;

	protected CommandConfig defaultComandConfig;

	protected CommandInterceptor commandInvoker;

	protected List<CommandInterceptor> commandInterceptors;
	
	protected TransactionFactory transactionFactory;
	
	protected CommandContextFactory commandContextFactory;
	protected TransactionContextFactory transactionContextFactory;
	
	protected DatabaseMetaDataLoader databaseMetaDataLoader;
	
	protected IdGenerator idGenerator;

	public static DaoEngineConfigurationImpl createDaoEngineConfigurationFromInputStream(
			InputStream inputStream) {
		return createDaoEngineConfigurationFromInputStream(inputStream,
				"daoEngineConfiguration");
	}

	public static DaoEngineConfigurationImpl createDaoEngineConfigurationFromInputStream(
			InputStream inputStream, String beanName) {
		return BeansConfigurationHelper
				.parseProcessEngineConfigurationFromInputStream(inputStream,
						beanName);
	}

	public DaoEngineConfiguration setBeans(Map<Object, Object> beans) {
		this.beans = beans;
		return this;
	}

	public String getDaoEngineName() {
		return daoEngineName;
	}

	public DaoEngineConfiguration setDaoEngineName(String daoEngineName) {
		this.daoEngineName = daoEngineName;
		return this;
	}

	protected CommandInterceptor createTransactionInterceptor(){return null;}

	public DaoEngine buildDaoEngine() {
		init();
		return new DaoEngineImpl(this);
	}

	protected void init() {
		initDataSource();
		initIdGenerator();
		initCommandExecutors();
		initCommandContextFactory();
		initTransactionContextFactory();
		initTransactionFactory();
		loadDatabaseMetadata();
	}
	
	private void initIdGenerator() {
		if(idGenerator == null){
			idGenerator = new IdGenerator(getConfigurationDataSource());
		}
	}

	protected void loadDatabaseMetadata() {
		if(databaseMetaDataLoader == null){
			databaseMetaDataLoader = new DefaultDatabaseMetaDataLoader();
		}
		databaseMetaDataLoader.setDaoEngineConfiguration(this);
		databaseMetaDataLoader.loadDatabaseMetadata();
	}

	protected void initTransactionFactory() {
		if(transactionFactory == null){
			transactionFactory = new JdbcTransactionFactory(dataSourceProxies.values());
		}
	}

	protected void initCommandContextFactory() {
		if(commandContextFactory == null){
			commandContextFactory = new CommandContextFactory();
			commandContextFactory.setDaoEngineConfiguration(this);
		}
	}

	protected void initTransactionContextFactory() {
		if (transactionContextFactory==null) {
		      transactionContextFactory = new StandaloneJdbcTransactionContextFactory();
		}
	}

	protected void initCommandExecutors() {
		initDefaultConfig();
		initCommandInvoker();
		initCommandInterceptors();
		initCommandExecutor();
	}

	protected void initCommandInvoker() {
		if (commandInvoker == null) {
			commandInvoker = new CommandInvoker();
		}
	}

	protected void initDefaultConfig() {
		if (defaultComandConfig == null) {
			defaultComandConfig = new CommandConfig();
		}
	}

	protected void initCommandInterceptors() {
		if (commandInterceptors == null) {
			commandInterceptors = new ArrayList<CommandInterceptor>();
			commandInterceptors.addAll(getDefaultCommandInterceptors());
			commandInterceptors.add(commandInvoker);
		}
	}

	protected List<CommandInterceptor> getDefaultCommandInterceptors(){
		List<CommandInterceptor> interceptors = new ArrayList<CommandInterceptor>();
	    CommandInterceptor transactionInterceptor = createTransactionInterceptor();
	    if (transactionInterceptor != null) {
	      interceptors.add(transactionInterceptor);
	    }
	    interceptors.add(new CommandContextInterceptor(this));
	    return interceptors;
	}
	protected void initCommandExecutor() {
		if (commandExecutor == null) {
			CommandInterceptor first = initInterceptorChain(commandInterceptors);
			commandExecutor = new CommandExecutorImpl(defaultComandConfig,
					first);
		}
	}

	protected CommandInterceptor initInterceptorChain(
			List<CommandInterceptor> chain) {
		if (chain == null || chain.isEmpty()) {
			throw new BambooException(
					"invalid command interceptor chain configuration: " + chain);
		}
		for (int i = 0; i < chain.size() - 1; i++) {
			chain.get(i).setNext(chain.get(i + 1));
		}
		return chain.get(0);
	}

	protected void initDataSource() {
		for(ConnectionProperties prop:connectionProperties){
			DataSourceProxy ds = new DataSourceProxy(prop);
			dataSourceProxies.put(prop.getName(), ds);
			if(prop.isDefaultDataSource() && defaultDataSourceName==null){
				//第一个默认为true的
				defaultDataSourceName = prop.getName();
			}
			if(configurationDataSourceName == null && prop.isConfigurationDataSource()){
				configurationDataSourceName = prop.getName();;
			}
		}
		if(dataSourceProxies.isEmpty()){
			throw new BambooException("not set datasource!");
		}
		if(StringUtils.isEmpty(defaultDataSourceName)){
			//没设置默认数据库，取第一个
			defaultDataSourceName = connectionProperties.get(0).getName();
		}
		if(configurationDataSourceName == null){
			//如果没有设置配置文件数据库，取默认数据库
			configurationDataSourceName = defaultDataSourceName;
		}
	}

	@Override
	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}

	public String getDefaultDataSourceName() {
		return defaultDataSourceName;
	}

	public void setDefaultDataSourceName(String defaultDataSourceName) {
		this.defaultDataSourceName = defaultDataSourceName;
	}

	@Override
	public DataSource getDataSource(String name) {
		return dataSourceProxies.get(name);
	}

	@Override
	public DataSourceProxy getDefaultDataSource() {
		return dataSourceProxies.get(defaultDataSourceName);
	}
	
	public Collection<DataSourceProxy> getDataSources(){
		return dataSourceProxies.values();
	}

	public TransactionFactory getTransactionFactory() {
		return transactionFactory;
	}

	public void setTransactionFactory(TransactionFactory transactionFactory) {
		this.transactionFactory = transactionFactory;
	}

	public String getConfigurationDataSourceName() {
		return configurationDataSourceName;
	}
	public DataSourceProxy getConfigurationDataSource() {
		return dataSourceProxies.get(configurationDataSourceName);
	}

	public IdGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public void setConnectionProperties(
			List<ConnectionProperties> connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

}
