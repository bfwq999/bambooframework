package org.bambooframework.dao.exception;

import org.bambooframework.core.exception.BambooException;

public class ParseSqlDataException extends BambooException {

	public ParseSqlDataException(String message,String...args) {
		super(message,args);
		
	}

}
