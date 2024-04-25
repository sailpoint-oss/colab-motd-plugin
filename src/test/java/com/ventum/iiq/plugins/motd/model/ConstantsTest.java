/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.model;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstantsTest {
	
	@Test(expected = InvocationTargetException.class)
	public void testConstructor() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
		Constructor<Constants> constructor = (Constructor<Constants>) Constants.class.getDeclaredConstructor();
		
		constructor.setAccessible(true);
		
		constructor.newInstance();
	}
}