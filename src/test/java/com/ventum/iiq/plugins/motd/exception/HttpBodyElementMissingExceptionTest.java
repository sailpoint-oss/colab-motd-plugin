/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.exception;

import org.junit.Test;

public class HttpBodyElementMissingExceptionTest {
	
	@Test(expected = HttpBodyElementMissingException.class)
	public void testConstructor() throws HttpBodyElementMissingException {
		throw new HttpBodyElementMissingException();
	}
	
	@Test(expected = HttpBodyElementMissingException.class)
	public void testConstructorString() throws HttpBodyElementMissingException {
		throw new HttpBodyElementMissingException("Message");
	}
	
	@Test(expected = HttpBodyElementMissingException.class)
	public void testConstructorStringThrowable() throws HttpBodyElementMissingException {
		throw new HttpBodyElementMissingException("Message", new Exception("This is a cause."));
	}
	
	@Test(expected = HttpBodyElementMissingException.class)
	public void testConstructorThrowable() throws HttpBodyElementMissingException {
		throw new HttpBodyElementMissingException(new Exception("This is a cause."));
	}
	
	@Test(expected = HttpBodyElementMissingException.class)
	public void testConstructorAll() throws HttpBodyElementMissingException {
		throw new HttpBodyElementMissingException("Message", new Exception("This is a cause."), false, false);
	}
}