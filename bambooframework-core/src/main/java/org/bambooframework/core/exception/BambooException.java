package org.bambooframework.core.exception;

import java.text.MessageFormat;

public class BambooException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BambooException(String message, Throwable cause) {
		super(message, cause);
	}

	public BambooException(String message,String...arguments) {
		super(MessageFormat.format(message, arguments));
	}

	public BambooException(Throwable cause) {
		super(cause);
	}

}
