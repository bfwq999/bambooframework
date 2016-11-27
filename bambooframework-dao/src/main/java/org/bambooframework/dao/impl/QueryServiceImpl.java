package org.bambooframework.dao.impl;

import java.io.Serializable;
import java.util.Map;

import org.bambooframework.dao.QueryService;
import org.bambooframework.dao.impl.cmd.GetCmd;
import org.bambooframework.dao.impl.interceptor.CommandExecutor;

public class QueryServiceImpl implements QueryService {
	protected CommandExecutor commandExecutor;

	public QueryServiceImpl(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
	}

	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}

	@Override
	public Map<String, Object> get(String tableName, Serializable pk) {
		return commandExecutor.execute(new GetCmd(tableName, pk));
	}
}
