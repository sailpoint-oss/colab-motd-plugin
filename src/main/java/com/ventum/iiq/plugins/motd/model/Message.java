/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.model;

import com.ventum.iiq.plugins.motd.exception.HttpBodyElementMissingException;
import com.ventum.iiq.plugins.motd.exception.InvalidHttpBodyException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

import static com.ventum.iiq.plugins.motd.model.HtmlPayloadElement.*;

public class Message {
	//region Local Fields
	private final String  name;
	private final String  body;
	private final String  footer;
	private final boolean active;
	//endregion Local Fields
	
	
	//region Constructors
	@SuppressWarnings("rawtypes")
	public Message(final Map message) throws InvalidHttpBodyException, HttpBodyElementMissingException {
		try {
			this.name   = (String) message.get(NAME.key);
			this.body   = (String) message.get(BODY.key);
			this.footer = (String) message.get(FOOTER.key);
			
			this.active = isActive(message);
		} catch (ClassCastException e) {
			throw new InvalidHttpBodyException("Couldn't create the user with the supplied HashMap (generated from HTTP body)");
		}
		
		if (StringUtils.isEmpty(name)) {
			throw new HttpBodyElementMissingException(String.format("Message '%s' was empty!", NAME.key));
		}
		
		if (StringUtils.isEmpty(body)) {
			throw new HttpBodyElementMissingException(String.format("Message '%s' was empty!", BODY.key));
		}
		
		if (footer == null) {
			throw new HttpBodyElementMissingException(String.format("Message '%s' was null!", FOOTER.key));
		}
	}
	//endregion Constructors
	
	
	//region Public Methods
	public String getName() {
		return name;
	}
	
	public String getBody() {
		return body;
	}
	
	public String getFooter() {
		return footer;
	}
	
	public boolean isActive() {
		return active;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, body, footer, active);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Message message = (Message) o;
		return active == message.active && name.equals(message.name) && body.equals(message.body) && footer.equals(message.footer);
	}
	
	@Override
	public String toString() {
		return "Message{" +
				"name='" + name + '\'' +
				", body='" + body + '\'' +
				", footer='" + footer + '\'' +
				", active=" + active +
				'}';
	}
	//endregion Public Methods
	
	
	//region Private Methods
	@SuppressWarnings("rawtypes")
	private boolean isActive(final Map message) {
		Object messageActive = message.get(ACTIVE.key);
		
		if (messageActive != null) {
			if (messageActive instanceof Boolean) {
				return ((Boolean) messageActive);
			} else if (messageActive instanceof String) {
				return Boolean.parseBoolean((String) messageActive);
			}
		}
		
		return false;
	}
	//endregion Private Methods
}
