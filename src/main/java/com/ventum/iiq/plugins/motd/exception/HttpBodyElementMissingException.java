/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.exception;

@SuppressWarnings("unused")
public class HttpBodyElementMissingException extends Exception {
	//region Constructors
	public HttpBodyElementMissingException() {
		super();
	}
	
	public HttpBodyElementMissingException(String message) {
		super(message);
	}
	
	public HttpBodyElementMissingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public HttpBodyElementMissingException(Throwable cause) {
		super(cause);
	}
	
	protected HttpBodyElementMissingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	//endregion Constructors
}
