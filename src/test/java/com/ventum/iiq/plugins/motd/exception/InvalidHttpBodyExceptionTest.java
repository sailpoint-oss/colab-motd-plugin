/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.exception;

import org.junit.Test;


public class InvalidHttpBodyExceptionTest {
	@Test(expected = InvalidHttpBodyException.class)
	public void testConstructor() throws InvalidHttpBodyException {
		throw new InvalidHttpBodyException();
	}
	
	@Test(expected = InvalidHttpBodyException.class)
	public void testConstructorString() throws InvalidHttpBodyException {
		throw new InvalidHttpBodyException("Message");
	}
	
	@Test(expected = InvalidHttpBodyException.class)
	public void testConstructorStringThrowable() throws InvalidHttpBodyException {
		throw new InvalidHttpBodyException("Message", new Exception("This is a cause."));
	}
	
	@Test(expected = InvalidHttpBodyException.class)
	public void testConstructorThrowable() throws InvalidHttpBodyException {
		throw new InvalidHttpBodyException(new Exception("This is a cause."));
	}
	
	@Test(expected = InvalidHttpBodyException.class)
	public void testConstructorAll() throws InvalidHttpBodyException {
		throw new InvalidHttpBodyException("Message", new Exception("This is a cause."), false, false);
	}
}