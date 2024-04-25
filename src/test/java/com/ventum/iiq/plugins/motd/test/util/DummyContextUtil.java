/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.test.util;


import java.util.HashMap;
import java.util.Map;

import static com.ventum.iiq.plugins.motd.model.Constants.KEY_ACTIVE_MESSAGE;
import static com.ventum.iiq.plugins.motd.model.Constants.KEY_MESSAGES;

public class DummyContextUtil {
	public static final String DUMMY_ACTIVE_MESSAGE      = "Dummy Active";
	public static final String DUMMY_ACTIVE_MESSAGE_DATA = "DUMMY_ACTIVE_MESSAGE_DATA";
	
	public static final String DUMMY_MESSAGE_1 = "Dummy 1";
	public static final String DUMMY_MESSAGE_2 = "Dummy 2";
	public static final String DUMMY_MESSAGE_3 = "Dummy 3";
	
	public static final String DUMMY_MESSAGE_1_DATA = "DUMMY_MESSAGE_1_DATA";
	public static final String DUMMY_MESSAGE_2_DATA = "DUMMY_MESSAGE_2_DATA";
	public static final String DUMMY_MESSAGE_3_DATA = "DUMMY_MESSAGE_3_DATA";
	
	public static final String DUMMY_MESSAGE_ADD_NAME   = "Dummy Add";
	public static final String DUMMY_MESSAGE_ADD_BODY   = "Dummy Add Body";
	public static final String DUMMY_MESSAGE_ADD_FOOTER = "Dummy Add Footer";
	public static final String DUMMY_MESSAGE_ADD_EXPECTED_JSON = "{\"name\":\"Dummy Add\",\"footer\":\"Dummy Add Footer\",\"body\":\"Dummy Add Body\"}";
	
	public static final String DUMMY_MESSAGE_NAMES_EXPECTED_JSON = String.format("[\"%s\", \"%s\", \"%s\", \"%s\"]", DUMMY_ACTIVE_MESSAGE, DUMMY_MESSAGE_1, DUMMY_MESSAGE_2, DUMMY_MESSAGE_3);
	
	private DummyContextUtil() {}
	
	public static DummyContext getDummyContext() {
		Map<String, Object> initConfig = new HashMap<>();
		initConfig.put(KEY_ACTIVE_MESSAGE, DUMMY_ACTIVE_MESSAGE);
		
		Map<String, String> dummyMessages = new HashMap<>();
		dummyMessages.put(DUMMY_ACTIVE_MESSAGE, DUMMY_ACTIVE_MESSAGE_DATA);
		dummyMessages.put(DUMMY_MESSAGE_1, DUMMY_MESSAGE_1_DATA);
		dummyMessages.put(DUMMY_MESSAGE_2, DUMMY_MESSAGE_2_DATA);
		dummyMessages.put(DUMMY_MESSAGE_3, DUMMY_MESSAGE_3_DATA);
		
		initConfig.put(KEY_MESSAGES, dummyMessages);
		
		return new DummyContext(initConfig);
	}
}
