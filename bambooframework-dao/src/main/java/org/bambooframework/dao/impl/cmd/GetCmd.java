package org.bambooframework.dao.impl.cmd;

import java.io.Serializable;
import java.util.Map;

import org.bambooframework.dao.impl.interceptor.Command;
import org.bambooframework.dao.impl.interceptor.CommandContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetCmd implements Command<Map> {
	
	private static Logger log = LoggerFactory
			.getLogger(GetCmd.class);

	String tableName;
	
	Serializable pk;
	
	public GetCmd(String tableName, Serializable pk) {
		super();
		this.tableName = tableName;
		this.pk = pk;
	}

	@Override
	public Map execute(CommandContext commandContext) {
		log.warn("tableName:{},pk:{}", tableName,pk);
		return null;
	}

}
