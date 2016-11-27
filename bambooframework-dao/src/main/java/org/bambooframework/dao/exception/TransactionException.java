package org.bambooframework.dao.exception;

import org.bambooframework.core.exception.BambooException;

public class TransactionException extends BambooException {

	public TransactionException(String message, Throwable cause) {
		super(message, cause);
	}
	public TransactionException(String message) {
		super(message);
	}

}
