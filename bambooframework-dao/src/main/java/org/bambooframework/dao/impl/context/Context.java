package org.bambooframework.dao.impl.context;

import java.util.Stack;

import org.bambooframework.dao.impl.interceptor.CommandContext;
import org.bambooframework.dao.DaoEngineConfiguration;

public class Context {
	protected static ThreadLocal<Stack<DaoEngineConfiguration>> daoEngineConfigurationStackThreadLocal = new ThreadLocal<Stack<DaoEngineConfiguration>>();
	protected static ThreadLocal<Stack<CommandContext>> commandContextThreadLocal = new ThreadLocal<Stack<CommandContext>>();

	public static CommandContext getCommandContext() {
		Stack<CommandContext> stack = getStack(commandContextThreadLocal);
		if (stack.isEmpty()) {
			return null;
		}
		return stack.peek();
	}

	public static DaoEngineConfiguration getProcessEngineConfiguration() {
		Stack<DaoEngineConfiguration> stack = getStack(daoEngineConfigurationStackThreadLocal);
		if (stack.isEmpty()) {
			return null;
		}
		return stack.peek();
	}

	protected static <T> Stack<T> getStack(ThreadLocal<Stack<T>> threadLocal) {
		Stack<T> stack = threadLocal.get();
		if (stack == null) {
			stack = new Stack<T>();
			threadLocal.set(stack);
		}
		return stack;
	}

	public static void setCommandContext(CommandContext commandContext) {
		getStack(commandContextThreadLocal).push(commandContext);
	}

	public static void removeCommandContext() {
		getStack(commandContextThreadLocal).pop();
	}

	public static void setDaoEngineConfiguration(
			DaoEngineConfiguration daoEngineConfiguration) {
		getStack(daoEngineConfigurationStackThreadLocal).push(
				daoEngineConfiguration);
	}

	public static void removeDaoEngineConfiguration() {
		getStack(daoEngineConfigurationStackThreadLocal).pop();
	}
}
