/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.model;

import com.ventum.iiq.plugins.motd.exception.HttpBodyElementMissingException;
import com.ventum.iiq.plugins.motd.exception.InvalidHttpBodyException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ventum.iiq.plugins.motd.model.HtmlPayloadElement.*;


public class MessageTest {
	public static final String TEST_MESSAGE_NAME   = "Test Message";
	public static final String TEST_MESSAGE_BODY   = "Currently testing the message";
	public static final String TEST_MESSAGE_FOOTER = "Test Footer";
	
	
	private Message getDummyMessage() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Map<String, Object> res = new HashMap<>();
		
		res.put(NAME.key, TEST_MESSAGE_NAME);
		res.put(BODY.key, TEST_MESSAGE_BODY);
		res.put(FOOTER.key, TEST_MESSAGE_FOOTER);
		
		return new Message(res);
	}
	
	@Test
	public void constructorActiveIsObject() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(NAME.key, TEST_MESSAGE_NAME);
		dummyMap.put(BODY.key, TEST_MESSAGE_BODY);
		dummyMap.put(FOOTER.key, TEST_MESSAGE_FOOTER);
		dummyMap.put(ACTIVE.key, new Object());
		
		Assert.assertFalse((new Message(dummyMap)).isActive());
	}
	
	
	@Test(expected = HttpBodyElementMissingException.class)
	public void constructorNameMissingTest() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(BODY.key, TEST_MESSAGE_BODY);
		dummyMap.put(FOOTER.key, TEST_MESSAGE_FOOTER);
		
		new Message(dummyMap);
	}
	
	@Test(expected = HttpBodyElementMissingException.class)
	public void constructorBodyMissingTest() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(NAME.key, TEST_MESSAGE_NAME);
		dummyMap.put(FOOTER.key, TEST_MESSAGE_FOOTER);
		
		new Message(dummyMap);
	}
	
	@Test(expected = HttpBodyElementMissingException.class)
	public void constructorFooterMissingTest() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(NAME.key, TEST_MESSAGE_NAME);
		dummyMap.put(BODY.key, TEST_MESSAGE_BODY);
		
		new Message(dummyMap);
	}
	
	@Test(expected = InvalidHttpBodyException.class)
	public void constructorBodyNotAStringTest() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(NAME.key, TEST_MESSAGE_NAME);
		dummyMap.put(BODY.key, new ArrayList<>());
		dummyMap.put(FOOTER.key, TEST_MESSAGE_FOOTER);
		
		new Message(dummyMap);
	}
	
	@Test
	public void getName() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Message dummyMessage = getDummyMessage();
		
		Assert.assertEquals(TEST_MESSAGE_NAME, dummyMessage.getName());
	}
	
	@Test
	public void getBody() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Message dummyMessage = getDummyMessage();
		
		Assert.assertEquals(TEST_MESSAGE_BODY, dummyMessage.getBody());
	}
	
	@Test
	public void getFooter() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Message dummyMessage = getDummyMessage();
		
		Assert.assertEquals(TEST_MESSAGE_FOOTER, dummyMessage.getFooter());
	}
	
	@Test
	public void isActiveTrue() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(NAME.key, TEST_MESSAGE_NAME);
		dummyMap.put(BODY.key, TEST_MESSAGE_BODY);
		dummyMap.put(FOOTER.key, TEST_MESSAGE_FOOTER);
		dummyMap.put(ACTIVE.key, true);
		
		Message dummyMessage = new Message(dummyMap);
		
		Assert.assertTrue(dummyMessage.isActive());
	}
	
	@Test
	public void isActiveFalse() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(NAME.key, TEST_MESSAGE_NAME);
		dummyMap.put(BODY.key, TEST_MESSAGE_BODY);
		dummyMap.put(FOOTER.key, TEST_MESSAGE_FOOTER);
		dummyMap.put(ACTIVE.key, false);
		
		Message dummyMessage = new Message(dummyMap);
		
		Assert.assertFalse(dummyMessage.isActive());
	}
	
	@Test
	public void isActiveStringTrue() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(NAME.key, TEST_MESSAGE_NAME);
		dummyMap.put(BODY.key, TEST_MESSAGE_BODY);
		dummyMap.put(FOOTER.key, TEST_MESSAGE_FOOTER);
		dummyMap.put(ACTIVE.key, "true");
		
		Message dummyMessage = new Message(dummyMap);
		
		Assert.assertTrue(dummyMessage.isActive());
	}
	
	@Test
	public void isActiveStringFalse() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(NAME.key, TEST_MESSAGE_NAME);
		dummyMap.put(BODY.key, TEST_MESSAGE_BODY);
		dummyMap.put(FOOTER.key, TEST_MESSAGE_FOOTER);
		dummyMap.put(ACTIVE.key, "false");
		
		Message dummyMessage = new Message(dummyMap);
		
		Assert.assertFalse(dummyMessage.isActive());
	}
	
	@Test
	public void testEquals() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Message msg1 = getDummyMessage();
		Message msg2 = getDummyMessage();
		
		Assert.assertTrue(msg1.equals(msg2) && msg2.equals(msg1));
		Assert.assertEquals(msg1.hashCode(), msg2.hashCode());
	}
	
	@Test
	public void testEqualsSame() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Message msg1 = getDummyMessage();
		
		Assert.assertEquals(msg1, msg1);
	}
	
	@SuppressWarnings({"squid:S5785", "ConstantConditions", "SimplifiableAssertion"})
	@Test
	public void testEqualsNull() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Message msg1 = getDummyMessage();
		
		Assert.assertFalse(msg1.equals(null));
	}
	
	@Test
	@SuppressWarnings({"squid:S5785", "EqualsBetweenInconvertibleTypes", "SimplifiableAssertion"})
	public void testEqualsNotSameObject() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Message msg1 = getDummyMessage();
		
		Assert.assertFalse(msg1.equals("This is a string"));
	}
	
	@Test
	public void testNotEqualsActive() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Message msg1 = getDummyMessage();
		
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(NAME.key, TEST_MESSAGE_NAME);
		dummyMap.put(BODY.key, TEST_MESSAGE_BODY);
		dummyMap.put(FOOTER.key, TEST_MESSAGE_FOOTER);
		dummyMap.put(ACTIVE.key, true);
		
		Message msg2 = new Message(dummyMap);
		
		Assert.assertFalse(msg1.equals(msg2) || msg2.equals(msg1));
	}
	
	@Test
	public void testNotEqualsName() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Message msg1 = getDummyMessage();
		
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(NAME.key, "Some name");
		dummyMap.put(BODY.key, TEST_MESSAGE_BODY);
		dummyMap.put(FOOTER.key, TEST_MESSAGE_FOOTER);
		dummyMap.put(ACTIVE.key, false);
		
		Message msg2 = new Message(dummyMap);
		
		Assert.assertFalse(msg1.equals(msg2) || msg2.equals(msg1));
	}
	
	@Test
	public void testNotEqualsBody() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Message msg1 = getDummyMessage();
		
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(NAME.key, TEST_MESSAGE_NAME);
		dummyMap.put(BODY.key, "Some body");
		dummyMap.put(FOOTER.key, TEST_MESSAGE_FOOTER);
		dummyMap.put(ACTIVE.key, false);
		
		Message msg2 = new Message(dummyMap);
		
		Assert.assertFalse(msg1.equals(msg2) || msg2.equals(msg1));
	}
	
	@Test
	public void testNotEqualsFooter() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		Message msg1 = getDummyMessage();
		
		Map<String, Object> dummyMap = new HashMap<>();
		
		dummyMap.put(NAME.key, TEST_MESSAGE_NAME);
		dummyMap.put(BODY.key, TEST_MESSAGE_BODY);
		dummyMap.put(FOOTER.key, "Some footer");
		dummyMap.put(ACTIVE.key, false);
		
		Message msg2 = new Message(dummyMap);
		
		Assert.assertFalse(msg1.equals(msg2) || msg2.equals(msg1));
	}
	
	@Test
	public void testToString() throws HttpBodyElementMissingException, InvalidHttpBodyException {
		String expectedString = "Message{" +
				"name='" + TEST_MESSAGE_NAME + '\'' +
				", body='" + TEST_MESSAGE_BODY + '\'' +
				", footer='" + TEST_MESSAGE_FOOTER + '\'' +
				", active=" + false +
				'}';
		
		Assert.assertEquals(expectedString, getDummyMessage().toString());
	}
}