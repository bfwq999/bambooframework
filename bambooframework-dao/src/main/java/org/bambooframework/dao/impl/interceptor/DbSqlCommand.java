package org.bambooframework.dao.impl.interceptor;

import org.bambooframework.dao.db.sql.DataMap;
import org.bambooframework.dao.db.transaction.TransactionIsolationLevel;
import org.bambooframework.dao.impl.cfg.TransactionPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DbSqlCommand<T> implements Command<T> {
	static Logger log = LoggerFactory.getLogger(DbSqlCommand.class);
	@Override
	public T execute(CommandContext commandContext) {
		try{
			TransactionPropagation transactionPropagation = getTransactionPropagation(commandContext);
			TransactionIsolationLevel level = getTransactionIsolationLevel(commandContext);
			commandContext.initTransaction(transactionPropagation, level);
			
			//执行前操作
			pre(commandContext);
			if(commandContext.getException() != null){
				throw commandContext.getException();
			}
			
			//执行DML
			T t = doExecute(commandContext);
			
			//刷新缓存
			commandContext.getDbSqlSession().flush();
			
			//DML成功后处理方法
			success(commandContext);
			if(commandContext.getException() != null){
				throw commandContext.getException();
			}
		
			return t;
		}catch(Throwable e){
			log.error("execute command",e);
			//异常处理
			try{
				fail(commandContext, e);
			}catch(Throwable ex){
				//如果e是从fail处抛出的，则往外抛，这样就可以回滚事务了
				//比如插入前插入日志，插入日志失败了也不影响插入,则fail可以不抛出异常
				commandContext.cancelExceptionBubble();
			}
			//即使fail中有异常也覆盖掉
			commandContext.exception(e);
		}
		return null;
	}
	
	public abstract void pre(CommandContext commandContext) throws Throwable;
	
	public abstract void success(CommandContext commandContext) throws Throwable;
	
	public abstract void fail(CommandContext commandContext,Throwable e) throws Throwable;
	
	public abstract T doExecute(CommandContext commandContext) throws Throwable;
	
	public abstract TransactionPropagation getTransactionPropagation(CommandContext commandContext);
	
	public abstract TransactionIsolationLevel getTransactionIsolationLevel(CommandContext commandContext);
	
	public abstract void setCommandData(DataMap dataMap);
}
