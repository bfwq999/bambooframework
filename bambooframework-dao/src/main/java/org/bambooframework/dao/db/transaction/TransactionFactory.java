package org.bambooframework.dao.db.transaction;


public interface TransactionFactory {
	  Transaction newTransaction(TransactionIsolationLevel level, boolean autoCommit);
}
