package org.bambooframework.dao.impl.cfg.standalone;

import org.bambooframework.dao.impl.cfg.TransactionContext;
import org.bambooframework.dao.impl.cfg.TransactionContextFactory;
import org.bambooframework.dao.impl.interceptor.CommandContext;

public class StandaloneJdbcTransactionContextFactory implements
		TransactionContextFactory {

	@Override
	public TransactionContext openTransactionContext(
			CommandContext commandContext) {
		return null;
	}

}
