package org.bambooframework.dao.impl.interceptor;

import org.bambooframework.dao.DaoEngineConfiguration;
import org.bambooframework.dao.impl.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandContextInterceptor extends AbstractCommandInterceptor {

	private static Logger log = LoggerFactory
			.getLogger(CommandContextInterceptor.class);

	protected DaoEngineConfiguration daoEngineConfiguration;
	
	public CommandContextInterceptor(
			DaoEngineConfiguration daoEngineConfiguration) {
		super();
		this.daoEngineConfiguration = daoEngineConfiguration;
	}

	@Override
	public <T> T execute(CommandConfig config, Command<T> command) {
		boolean contextReused = false;
		CommandContext commandContext = Context.getCommandContext();
		if (commandContext == null) {
			commandContext = new CommandContext(command, daoEngineConfiguration);
		}else{
			if(log.isDebugEnabled()){
				log.debug("Valid context found. Reusing it for the current command '{}'", command.getClass().getCanonicalName());
			}
			contextReused = true;
		}
		
		try{
			//创建一个新的DbSqlSession
			commandContext.newDbSqlSession();
			//push to stack
			Context.setCommandContext(commandContext);
			Context.setDaoEngineConfiguration(this.daoEngineConfiguration);
			return next.execute(config, command);
		}catch(Exception exception){
			commandContext.exception(exception);
		}finally{
			try{
				if(!contextReused){
					commandContext.close();
				}
			}finally{
				commandContext.removeDbSqlSession();
				Context.removeCommandContext();
				Context.removeDaoEngineConfiguration();
			}
		}
		return null;
	}

}
