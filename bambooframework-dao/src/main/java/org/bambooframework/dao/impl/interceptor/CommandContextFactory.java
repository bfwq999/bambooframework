package org.bambooframework.dao.impl.interceptor;

import org.bambooframework.dao.DaoEngineConfiguration;

public class CommandContextFactory {

	protected DaoEngineConfiguration daoEngineConfiguration;

	public CommandContext createCommandContext(Command<?> command){
		return new CommandContext(command, daoEngineConfiguration);
	}
	
	public DaoEngineConfiguration getDaoEngineConfiguration() {
		return daoEngineConfiguration;
	}

	public void setDaoEngineConfiguration(
			DaoEngineConfiguration daoEngineConfiguration) {
		this.daoEngineConfiguration = daoEngineConfiguration;
	}
}
 