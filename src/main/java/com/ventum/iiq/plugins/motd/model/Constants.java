/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.model;

public class Constants {
	private Constants() throws InstantiationException {
		throw new InstantiationException();
	}
	
	public static final String CONFIG_NAME = "MOTDConfigObject";
	
	public static final String KEY_ACTIVE_MESSAGE = "activeMessage";
	public static final String KEY_MESSAGES       = "messages";
	
	public static final String KEY_TITLE = "title";
}
