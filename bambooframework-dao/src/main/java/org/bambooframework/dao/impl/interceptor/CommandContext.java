package org.bambooframework.dao.impl.interceptor;

import java.sql.SQLException;
import java.util.List;
import java.util.Stack;

import org.bambooframework.core.exception.BambooException;
import org.bambooframework.dao.DaoEngineConfiguration;
import org.bambooframework.dao.db.sql.DataMap;
import org.bambooframework.dao.db.sql.DbSqlSession;
import org.bambooframework.dao.db.transaction.Transaction;
import org.bambooframework.dao.db.transaction.TransactionIsolationLevel;
import org.bambooframework.dao.impl.cfg.TransactionPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandContext {

	private static Logger log = LoggerFactory.getLogger(CommandContext.class);
	protected Command<?> command;
	protected DaoEngineConfiguration daoEngineConfiguration;
	protected Throwable exception;
	protected boolean exceptionBubble =  true;
	protected DbSqlSession dbSqlSession;
	protected boolean transactionInited;
	Stack<Transaction> transactionStack = new Stack<Transaction>();
	
	protected DataMap commandContextData = new DataMap();
	
	public CommandContext(Command<?> command,
			DaoEngineConfiguration daoEngineConfiguration) {
		super();
		this.command = command;
		this.daoEngineConfiguration = daoEngineConfiguration;
	}
	
	public void exception(Throwable exception){
		this.exception = exception;
	}
	
	public Throwable getException(){
		return exception;
	}
	public void close() {
		  // rethrow the original exception if there was one
	    if (exception != null) {
	      if (exception instanceof Error) {
	        throw (Error) exception;
	      } else if (exception instanceof RuntimeException) {
	        throw (RuntimeException) exception;
	      } else {
	        throw new BambooException("exception while executing command " + command, exception);
	      }
	    }
	}

	public DaoEngineConfiguration getDaoEngineConfiguration() {
		return daoEngineConfiguration;
	}
	
	public Transaction newTransaction(TransactionIsolationLevel level,boolean autoCommit){
		return daoEngineConfiguration.getTransactionFactory().newTransaction(level,autoCommit);
	}

	public DbSqlSession getDbSqlSession() {
		if(dbSqlSession!=null && !transactionInited){
			//创建了sqlSession但没有初始化事务，不能使用dbSqlSession
			throw new BambooException("transaction not inited");
		}
		return dbSqlSession;
	}
	
	public DbSqlSession newDbSqlSession(){
		if(dbSqlSession != null){
			return dbSqlSession;
		}
		transactionInited = false;
		dbSqlSession = new DbSqlSession();
		return dbSqlSession;
	}
	public void removeDbSqlSession(){
		if(transactionInited){
			Transaction transaction = transactionStack.pop();
			//初始化事务了
			/**
			 * 最后进行事务提交或回滚
			 */
			if(dbSqlSession != null){
				try {
					dbSqlSession.flush();
				} catch (SQLException e) {
					log.error("flush exception:",e);
					exception(exception);
				}
				
				if(exception == null){
					if(transactionStack.isEmpty() || transaction != transactionStack.peek()){
						//事务栈空了或当前事务不等于上一个事务时
						try {
							dbSqlSession.commit();
						} catch (SQLException e) {
							exception(exception);
							log.error("commit exception:",e);
						}
					}
				}
				
				if(exception != null){
					try {
						//有异常
						dbSqlSession.rollback();
					} catch (SQLException e) {
						log.error("rollback exception:",e);
					}
					
				}
			}
		}
		if(!exceptionBubble){
			//异常不往外抛，清空异常值
			log.info("Cancel Exception Bubble:{}",exception.getMessage());
			exception(null);
		}
		transactionInited = false;
		dbSqlSession = null;
		resetExceptionBubble();
	}
	
	public void cancelExceptionBubble(){
		exceptionBubble = false;
	}
	public void resetExceptionBubble(){
		exceptionBubble = true;
	}
	
	public void initTransaction(TransactionPropagation transactionPropagation,TransactionIsolationLevel level){
		if(dbSqlSession == null){
			throw new BambooException("DbSqlSession not exists");
		}
		if(transactionInited){
			throw new BambooException("transaction has inited,cannot init twice");
		}
		Transaction transaction = null;
		//事务策略
		if(transactionPropagation == TransactionPropagation.REQUIRED){
			//使用已存在的
			if(!transactionStack.isEmpty()){
				transaction = transactionStack.peek();
			}
			if(transaction == null){
				if(level == null){
					level = TransactionIsolationLevel.READ_COMMITTED;
				}
				transaction = newTransaction(level,true);
			}
			transactionStack.push(transaction);
		}else if(transactionPropagation == TransactionPropagation.REQUIRES_NEW){
			//创建新的事务
			if(level == null){
				level = TransactionIsolationLevel.READ_COMMITTED;
			}
			transaction = newTransaction(level,true);
			transactionStack.push(transaction);
		}else{
			//不需要事务
			transaction = newTransaction(null,false);
			transactionStack.push(transaction);
		}
		dbSqlSession.setTransaction(transaction, transactionPropagation);
		transactionInited = true;
	}
	
	public DataMap addCommandContextData(String id,DataMap dataMap){
		commandContextData.add(id, dataMap);
		return commandContextData;
	}
	public void putCommandContextData(String id,DataMap dataMap){
		commandContextData.put(id, dataMap);
	}
	public void putCommandContextData(String id,List<? extends DataMap> dataMap){
		commandContextData.put(id, dataMap);
	}
}
