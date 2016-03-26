package org.bambooframework.config;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.bambooframework.common.BaseMybatisInterceptor;
import org.bambooframework.common.IdGenerator;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class IdGeneratorInterceptor extends BaseMybatisInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();
		MetaObject metaStatementHandler = getActualTarget(statementHandler);
		String[] ids = (String[])metaStatementHandler.getValue("delegate.mappedStatement.keyProperties");
		
		Class idType = metaStatementHandler.getGetterType("delegate.parameterHandler.parameterObject."+ids[0]);
		Integer nextId = IdGenerator.getNextId();
		if(idType == Integer.class){
			metaStatementHandler.setValue("delegate.parameterHandler.parameterObject."+ids[0],nextId);
		}else{
			metaStatementHandler.setValue("delegate.parameterHandler.parameterObject."+ids[0],nextId.toString());
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		// 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的 次数
		if (target instanceof StatementHandler) {
			MetaObject metaStatementHandler = getActualTarget(target);
			
			if(!"INSERT".equalsIgnoreCase((String)metaStatementHandler.getValue("delegate.mappedStatement.sqlCommandType.name"))){
				return target;
			}
			
			String[] ids = (String[])metaStatementHandler.getValue("delegate.mappedStatement.keyProperties");
			if(ids==null || ids.length!=1){
				//没有主键的,不处理
				return target;
			}
			
			if(metaStatementHandler.getValue("delegate.mappedStatement.keyGenerator") instanceof NoKeyGenerator
				&& metaStatementHandler.getValue("delegate.parameterHandler.parameterObject."+ids[0]) == null){
				//插入,且没有指定主键生成策略,且主键值为空时
				return Plugin.wrap(target, this);
			}
		}
		return target;
	}

	@Override
	public void setProperties(Properties properties) {

	}
}
