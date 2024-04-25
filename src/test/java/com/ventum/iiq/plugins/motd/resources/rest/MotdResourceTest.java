/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.resources.rest;

import com.ventum.iiq.plugins.motd.test.util.DummyContext;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import sailpoint.api.SailPointContext;

import javax.ws.rs.core.Response;
import java.util.*;

import static com.ventum.iiq.plugins.motd.database.DataReadWrite.NO_DATA_JSON;
import static com.ventum.iiq.plugins.motd.model.Constants.*;
import static com.ventum.iiq.plugins.motd.model.HtmlPayloadElement.*;
import static com.ventum.iiq.plugins.motd.test.util.DummyContextUtil.*;

public class MotdResourceTest {
	
	public static final String EXPECTED_PLUGIN_NAME = "motd";
	
	private MotdResource getDummyResource() {
		DummyContext dummyContext = getDummyContext();
		
		return new MotdResource() {
			@Override
			public SailPointContext getContext() {
				return dummyContext;
			}
		};
	}
	
	@Test
	public void getPluginName() {
		MotdResource dummy = getDummyResource();
		
		Assert.assertEquals(EXPECTED_PLUGIN_NAME, dummy.getPluginName());
	}
	
	@Test
	public void getMessagesNames() throws JSONException {
		MotdResource dummy = getDummyResource();
		
		JSONAssert.assertEquals(DUMMY_MESSAGE_NAMES_EXPECTED_JSON, (String) dummy.getMessagesNames().getEntity(), false);
	}
	
	@Test
	public void getMessagesNamesNoContext() throws JSONException {
		MotdResource dummy = new MotdResource() {
			@Override
			public SailPointContext getContext() {
				return null;
			}
		};
		
		Response response = dummy.getMessagesNames();
		
		Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void getMessage() throws JSONException {
		MotdResource dummy = getDummyResource();
		
		Response response = dummy.getMessage(DUMMY_MESSAGE_1);
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		JSONAssert.assertEquals(DUMMY_MESSAGE_1_DATA, (String) response.getEntity(), true);
	}
	
	@Test
	public void getMessageNoContext() throws JSONException {
		MotdResource dummy = new MotdResource() {
			@Override
			public SailPointContext getContext() {
				return null;
			}
		};
		
		Response response = dummy.getMessage(DUMMY_MESSAGE_1);
		
		Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}
	
	@Test
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addMessage() throws JSONException {
		MotdResource dummy = getDummyResource();
		
		Map dummyHtmlPayload = new HashMap();
		dummyHtmlPayload.put(NAME.key, DUMMY_MESSAGE_ADD_NAME);
		dummyHtmlPayload.put(BODY.key, DUMMY_MESSAGE_ADD_BODY);
		dummyHtmlPayload.put(FOOTER.key, DUMMY_MESSAGE_ADD_FOOTER);
		
		Response response = dummy.addMessage(dummyHtmlPayload);
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		JSONAssert.assertEquals(DUMMY_MESSAGE_ADD_EXPECTED_JSON, (String) dummy.getMessage(DUMMY_MESSAGE_ADD_NAME).getEntity(), true);
	}
	
	@Test
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addMessageNoName() {
		MotdResource dummy = getDummyResource();
		
		Map dummyHtmlPayload = new HashMap();
		dummyHtmlPayload.put(BODY.key, DUMMY_MESSAGE_ADD_BODY);
		dummyHtmlPayload.put(FOOTER.key, DUMMY_MESSAGE_ADD_FOOTER);
		
		Response response = dummy.addMessage(dummyHtmlPayload);
		
		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
	}
	
	@Test
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addMessageNameIsNotAString() {
		MotdResource dummy = getDummyResource();
		
		Map dummyHtmlPayload = new HashMap();
		dummyHtmlPayload.put(NAME.key, dummy);
		dummyHtmlPayload.put(BODY.key, DUMMY_MESSAGE_ADD_BODY);
		dummyHtmlPayload.put(FOOTER.key, DUMMY_MESSAGE_ADD_FOOTER);
		
		Response response = dummy.addMessage(dummyHtmlPayload);
		
		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
	}
	
	@Test
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addMessagePayloadIsASet() {
		MotdResource dummy = getDummyResource();
		
		Set dummyHtmlPayload = new HashSet();
		dummyHtmlPayload.add(NAME.key);
		dummyHtmlPayload.add(BODY.key);
		dummyHtmlPayload.add(FOOTER.key);
		
		Response response = dummy.addMessage(dummyHtmlPayload);
		
		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
	}
	
	@Test
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addMessagePayloadIsAList() {
		MotdResource dummy = getDummyResource();
		
		List dummyHtmlPayload = new ArrayList();
		dummyHtmlPayload.add(NAME.key);
		dummyHtmlPayload.add(BODY.key);
		dummyHtmlPayload.add(FOOTER.key);
		
		Response response = dummy.addMessage(dummyHtmlPayload);
		
		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void addMessagePayloadIsNull() {
		MotdResource dummy = getDummyResource();
		
		Response response = dummy.addMessage(null);
		
		Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
	}
	
	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addMessageWithEmojiInBody() {
		MotdResource dummy = getDummyResource();
		
		Map dummyHtmlPayload = new HashMap();
		dummyHtmlPayload.put(NAME.key, DUMMY_MESSAGE_ADD_NAME);
		dummyHtmlPayload.put(BODY.key, "🦀");
		dummyHtmlPayload.put(FOOTER.key, DUMMY_MESSAGE_ADD_FOOTER);
		
		Response response = dummy.addMessage(dummyHtmlPayload);
		
		Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}
	
	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addActiveMessageWithEmojiInName() {
		MotdResource dummy = getDummyResource();
		
		Map dummyHtmlPayload = new HashMap();
		dummyHtmlPayload.put(NAME.key, "🦀");
		dummyHtmlPayload.put(BODY.key, DUMMY_MESSAGE_ADD_BODY);
		dummyHtmlPayload.put(FOOTER.key, DUMMY_MESSAGE_ADD_FOOTER);
		
		Response response = dummy.addMessage(dummyHtmlPayload);
		
		Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}
	
	@Test
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addActiveMessage() throws JSONException {
		MotdResource dummy = getDummyResource();
		
		Map dummyHtmlPayload = new HashMap();
		dummyHtmlPayload.put(NAME.key, DUMMY_MESSAGE_ADD_NAME);
		dummyHtmlPayload.put(BODY.key, DUMMY_MESSAGE_ADD_BODY);
		dummyHtmlPayload.put(FOOTER.key, DUMMY_MESSAGE_ADD_FOOTER);
		dummyHtmlPayload.put(ACTIVE.key, true);
		
		Response response = dummy.addMessage(dummyHtmlPayload);
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		response = dummy.getActiveMessage();
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		JSONAssert.assertEquals(DUMMY_MESSAGE_ADD_EXPECTED_JSON, (String) response.getEntity(), true);
	}
	
	@Test
	public void getActiveMessage() {
		MotdResource dummy = getDummyResource();
		
		Response response = dummy.getActiveMessage();
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(DUMMY_ACTIVE_MESSAGE_DATA, response.getEntity());
	}
	
	@Test
	public void getActiveMessageNoContext() {
		MotdResource dummy = new MotdResource() {
			@Override
			public SailPointContext getContext() {
				return null;
			}
		};
		
		Response response = dummy.getActiveMessage();
		
		Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void setActiveMessage() {
		MotdResource dummy = getDummyResource();
		
		// Sanity check
		Response response = dummy.getActiveMessage();
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(DUMMY_ACTIVE_MESSAGE_DATA, response.getEntity());
		
		// Set message
		response = dummy.setActiveMessage(DUMMY_MESSAGE_1);
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		// Get message again
		response = dummy.getActiveMessage();
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(DUMMY_MESSAGE_1_DATA, response.getEntity());
	}
	
	@Test
	public void setActiveMessageWithEmoji() {
		MotdResource dummy = getDummyResource();
		
		// Sanity check
		Response response = dummy.getActiveMessage();
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(DUMMY_ACTIVE_MESSAGE_DATA, response.getEntity());
		
		// Set message
		response = dummy.setActiveMessage("🦀");
		Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void deleteMessage() {
		MotdResource dummy = getDummyResource();
		
		Response response = dummy.getMessage(DUMMY_MESSAGE_1);
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		JSONAssert.assertEquals(DUMMY_MESSAGE_1_DATA, (String) response.getEntity(), true);
		
		response = dummy.deleteMessage(DUMMY_MESSAGE_1);
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		
		response = dummy.getMessage(DUMMY_MESSAGE_1);
		JSONAssert.assertEquals(NO_DATA_JSON, (String) response.getEntity(), true);
	}
	
	@Test
	public void deleteMessageNoContext() {
		MotdResource dummy = new MotdResource() {
			@Override
			public SailPointContext getContext() {
				return null;
			}
		};
		
		Response response = dummy.deleteMessage(DUMMY_MESSAGE_1);
		
		Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void deleteMessageNoMessages() {
		Map<String, Object> initConfig = new HashMap<>();
		initConfig.put(KEY_ACTIVE_MESSAGE, DUMMY_ACTIVE_MESSAGE);
		initConfig.put(KEY_MESSAGES, null);
		
		DummyContext dummyContext = new DummyContext(initConfig);
		
		MotdResource dummy = new MotdResource() {
			@Override
			public SailPointContext getContext() {
				return dummyContext;
			}
		};
		
		Response response = dummy.deleteMessage(DUMMY_MESSAGE_1);
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void getTitle() {
		final String TEST_TITLE = "Test Title";
		final String EXPECTED_TEST_TITLE = String.format("{\"title\": \"%s\"}", TEST_TITLE);
		
		Map<String, Object> initConfig = new HashMap<>();
		initConfig.put(KEY_TITLE, TEST_TITLE);
		
		DummyContext dummyContext = new DummyContext(initConfig);
		
		MotdResource dummy = new MotdResource() {
			@Override
			public SailPointContext getContext() {
				return dummyContext;
			}
		};
		
		Response response = dummy.getTitle();
		
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		JSONAssert.assertEquals(EXPECTED_TEST_TITLE, (String) response.getEntity(), true);
	}
	
	@Test
	public void getTitleNoContext() {
		MotdResource dummy = new MotdResource() {
			@Override
			public SailPointContext getContext() {
				return null;
			}
		};
		
		Response response = dummy.getTitle();
		
		Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
	}
}