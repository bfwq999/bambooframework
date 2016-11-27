package org.bambooframework.dao.impl.cfg;

import org.bambooframework.dao.impl.interceptor.CommandContext;

public interface TransactionContextFactory {

	TransactionContext openTransactionContext(CommandContext commandContext);
}
