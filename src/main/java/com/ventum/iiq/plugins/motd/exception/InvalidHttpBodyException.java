/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.exception;

public class InvalidHttpBodyException extends Exception {
	//region Constructors
	public InvalidHttpBodyException() {
		super();
	}
	
	public InvalidHttpBodyException(String message) {
		super(message);
	}
	
	public InvalidHttpBodyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidHttpBodyException(Throwable cause) {
		super(cause);
	}
	
	protected InvalidHttpBodyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	//endregion Constructors
}
