/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.database;

import com.ventum.iiq.plugins.motd.exception.HttpBodyElementMissingException;
import com.ventum.iiq.plugins.motd.exception.InvalidHttpBodyException;
import com.ventum.iiq.plugins.motd.model.Message;
import com.ventum.iiq.plugins.motd.test.util.DummyContext;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import sailpoint.tools.GeneralException;

import java.util.HashMap;
import java.util.Map;

import static com.ventum.iiq.plugins.motd.database.DataReadWrite.NO_DATA_JSON;
import static com.ventum.iiq.plugins.motd.model.Constants.KEY_ACTIVE_MESSAGE;
import static com.ventum.iiq.plugins.motd.model.Constants.KEY_MESSAGES;
import static com.ventum.iiq.plugins.motd.model.HtmlPayloadElement.*;
import static com.ventum.iiq.plugins.motd.test.util.DummyContextUtil.*;


public class DataReadWriteTest {

	private DataReadWrite getDummyDataReadWrite() throws GeneralException {
		return new DataReadWrite(getDummyContext());
	}
	
	@Test
	public void constructorTest() throws GeneralException {
		DataReadWrite dataReadWrite = new DataReadWrite(getDummyContext());
		
		Assert.assertNotNull(dataReadWrite.getConfigObject());
	}
	
	@Test
	public void getActiveMessage() throws GeneralException {
		DataReadWrite dummy = getDummyDataReadWrite();
		
		Assert.assertEquals(DUMMY_ACTIVE_MESSAGE_DATA, dummy.getActiveMessage());
	}
	
	@Test
	public void setActiveMessage() throws GeneralException {
		DummyContext dummyContext = getDummyContext();
		
		try (DataReadWrite dummy = new DataReadWrite(dummyContext)) {
			// Sanity check
			Assert.assertEquals(DUMMY_ACTIVE_MESSAGE_DATA, dummy.getActiveMessage());
			
			dummy.setActiveMessage(DUMMY_MESSAGE_1);
		}
		
		try (DataReadWrite dummy = new DataReadWrite(dummyContext)) {
			Assert.assertEquals(DUMMY_MESSAGE_1_DATA, dummy.getActiveMessage());
		}
	}
	
	@Test
	public void getMessage() throws GeneralException {
		DataReadWrite dummy = getDummyDataReadWrite();
		
		Assert.assertEquals(DUMMY_MESSAGE_3_DATA, dummy.getMessage(DUMMY_MESSAGE_3));
	}
	
	@Test
	public void getMessageNameNull() throws GeneralException, JSONException {
		DataReadWrite dummy = getDummyDataReadWrite();
		
		JSONAssert.assertEquals(NO_DATA_JSON, dummy.getMessage(null), true);
	}
	
	@Test
	public void getMessageNotFound() throws GeneralException, JSONException {
		DataReadWrite dummy = getDummyDataReadWrite();
		
		JSONAssert.assertEquals(NO_DATA_JSON, dummy.getMessage("some random name that's not saved"), true);
	}
	
	@Test
	public void getMessageNoMessages() throws GeneralException, JSONException {
		Map<String, Object> initConfig = new HashMap<>();
		initConfig.put(KEY_ACTIVE_MESSAGE, DUMMY_ACTIVE_MESSAGE);
		initConfig.put(KEY_MESSAGES, null);
		
		DummyContext dummyContext = new DummyContext(initConfig);
		DataReadWrite dummy = new DataReadWrite(dummyContext);
		
		JSONAssert.assertEquals(NO_DATA_JSON, dummy.getMessage(DUMMY_MESSAGE_3), true);
	}
	
	@Test
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addMessage() throws GeneralException, InvalidHttpBodyException, HttpBodyElementMissingException, JSONException {
		DummyContext dummyContext = getDummyContext();
		
		try (DataReadWrite dummy = new DataReadWrite(dummyContext)) {
			Map dummyHtmlPayload = new HashMap();
			dummyHtmlPayload.put(NAME.key, DUMMY_MESSAGE_ADD_NAME);
			dummyHtmlPayload.put(BODY.key, DUMMY_MESSAGE_ADD_BODY);
			dummyHtmlPayload.put(FOOTER.key, DUMMY_MESSAGE_ADD_FOOTER);
			
			Message dummyMessage = new Message(dummyHtmlPayload);

			dummy.addMessage(dummyMessage);
		}
		
		try (DataReadWrite dummy = new DataReadWrite(dummyContext)) {
			// Compare the values as actual JSON Objects and not Strings since the order of keys is irrelevant.
			JSONAssert.assertEquals(DUMMY_MESSAGE_ADD_EXPECTED_JSON, dummy.getMessage(DUMMY_MESSAGE_ADD_NAME), true);
		}
	}
	
	@Test
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addFirstMessage() throws GeneralException, InvalidHttpBodyException, HttpBodyElementMissingException, JSONException {
		Map<String, Object> initConfig = new HashMap<>();
		initConfig.put(KEY_ACTIVE_MESSAGE, DUMMY_ACTIVE_MESSAGE);
		initConfig.put(KEY_MESSAGES, null);
		
		DummyContext dummyContext = new DummyContext(initConfig);
		
		try (DataReadWrite dummy = new DataReadWrite(dummyContext)) {
			Map dummyHtmlPayload = new HashMap();
			dummyHtmlPayload.put(NAME.key, DUMMY_MESSAGE_ADD_NAME);
			dummyHtmlPayload.put(BODY.key, DUMMY_MESSAGE_ADD_BODY);
			dummyHtmlPayload.put(FOOTER.key, DUMMY_MESSAGE_ADD_FOOTER);
			
			Message dummyMessage = new Message(dummyHtmlPayload);

			dummy.addMessage(dummyMessage);
		}
		
		try (DataReadWrite dummy = new DataReadWrite(dummyContext)) {
			// Compare the values as actual JSON Objects and not Strings since the order of keys is irrelevant.
			JSONAssert.assertEquals(DUMMY_MESSAGE_ADD_EXPECTED_JSON, dummy.getMessage(DUMMY_MESSAGE_ADD_NAME), true);
		}
	}
	
	@Test
	public void getMessageNames() throws GeneralException, JSONException {
		DataReadWrite dummy = getDummyDataReadWrite();
		
		JSONAssert.assertEquals(DUMMY_MESSAGE_NAMES_EXPECTED_JSON, dummy.getMessageNames(), false);
	}
	
	@Test
	public void getMessageNamesNoMessages() throws GeneralException, JSONException {
		Map<String, Object> initConfig = new HashMap<>();
		initConfig.put(KEY_ACTIVE_MESSAGE, DUMMY_ACTIVE_MESSAGE);
		initConfig.put(KEY_MESSAGES, null);
		
		DummyContext dummyContext = new DummyContext(initConfig);
		DataReadWrite dummy = new DataReadWrite(dummyContext);
		
		JSONAssert.assertEquals("[]", dummy.getMessageNames(), true);
	}
	
	@Test
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void testExplicitLoad() throws GeneralException, InvalidHttpBodyException, HttpBodyElementMissingException, JSONException {
		DummyContext dummyContext = getDummyContext();
		
		DataReadWrite dummy = new DataReadWrite(dummyContext);
		
		Map dummyHtmlPayload = new HashMap();
		dummyHtmlPayload.put(NAME.key, DUMMY_MESSAGE_ADD_NAME);
		dummyHtmlPayload.put(BODY.key, DUMMY_MESSAGE_ADD_BODY);
		dummyHtmlPayload.put(FOOTER.key, DUMMY_MESSAGE_ADD_FOOTER);
		
		Message dummyMessage = new Message(dummyHtmlPayload);
		
		dummy.addMessage(dummyMessage);
		dummy.close();
		
		JSONAssert.assertEquals(DUMMY_MESSAGE_ADD_EXPECTED_JSON, dummy.getMessage(DUMMY_MESSAGE_ADD_NAME), true);
	}
}