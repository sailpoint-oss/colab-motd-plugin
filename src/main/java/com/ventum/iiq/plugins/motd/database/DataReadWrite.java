/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.database;

import com.google.gson.Gson;
import com.ventum.iiq.plugins.motd.model.Message;
import org.apache.log4j.Logger;
import sailpoint.api.SailPointContext;
import sailpoint.object.Custom;
import sailpoint.tools.GeneralException;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import static com.ventum.iiq.plugins.motd.model.Constants.*;
import static com.ventum.iiq.plugins.motd.model.HtmlPayloadElement.*;

@SuppressWarnings("unchecked")
public class DataReadWrite implements AutoCloseable {
	//region Static Fields
	public static final String NO_DATA_JSON;
	public static final Logger log = Logger.getLogger(DataReadWrite.class);
	
	static {
		Gson                gson   = new Gson();
		Map<String, String> result = new HashMap<>();
		
		result.put(NAME.key, "");
		result.put(BODY.key, "<div class=\"text-center\" style=\"margin-top: 63px;\">\n" + "\t<p class=\"h4 text-muted\">Currently no data</p>\n" + "</div>");
		result.put(FOOTER.key, "Currently no data");
		
		NO_DATA_JSON = gson.toJson(result);
	}
	//endregion Static Fields
	
	
	//region Local Fields
	private final SailPointContext context;
	private       Custom           configCustomObject = null;
	//endregion Local Fields
	
	
	//region Constructors
	public DataReadWrite(@NotNull SailPointContext context) throws GeneralException {
		this.context = context;
		load();
	}
	//endregion Constructors
	
	
	//region Public Methods
	public String getTitle() throws GeneralException {
		String title = getConfigObject().getString(KEY_TITLE);
		
		return String.format("{\"title\": \"%s\"}", title);
	}
	
	public String getActiveMessage() throws GeneralException {
		String active = getConfigObject().getString(KEY_ACTIVE_MESSAGE);
		
		return getMessage(active);
	}
	
	public void setActiveMessage(String name) throws GeneralException {
		Custom config = getConfigObject();
		
		log.debug("Setting active message");
		
		config.put(KEY_ACTIVE_MESSAGE, name);
		
		save();
	}
	
	public String getMessage(String name) throws GeneralException {
		if (name == null) {
			return NO_DATA_JSON;
		}
		
		Map<String, String> messages = (Map<String, String>) getConfigObject().get(KEY_MESSAGES);
		
		if (messages == null) {
			return NO_DATA_JSON;
		}
		
		String message = messages.get(name);
		if (message != null) {
			return message;
		} else {
			return NO_DATA_JSON;
		}
	}
	
	public void addMessage(Message message) throws GeneralException {
		Map<String, String> messages = (Map<String, String>) getConfigObject().get(KEY_MESSAGES);
		
		if (messages == null) {
			messages = new HashMap<>();
			getConfigObject().put(KEY_MESSAGES, messages);
		}
		
		messages.put(message.getName(), messageToJson(message));
		
		save();
	}
	
	public String getMessageNames() throws GeneralException {
		Map<String, String> messages = (Map<String, String>) getConfigObject().get(KEY_MESSAGES);
		
		if (messages == null) {
			return "[]";
		}
		
		StringBuilder result = new StringBuilder("[");
		
		for (String key : messages.keySet())
			result.append("\"").append(key).append("\",");
		
		result.replace(result.length() - 1, result.length(), "]");
		
		return result.toString();
	}
	
	public void deleteMessage(String name) throws GeneralException {
		Map<String, String> messages = (Map<String, String>) getConfigObject().get(KEY_MESSAGES);
		
		if (messages == null) {
			return;
		}
		
		messages.remove(name);
		
		save();
	}
	
	@Override
	public void close() {
		configCustomObject = null;
	}
	
	public Custom getConfigObject() throws GeneralException {
		if (configCustomObject != null) {
			return configCustomObject;
		}
		
		load();
		return configCustomObject;
	}
	//endregion Public Methods

	//region Private Methods
	private void load() throws GeneralException {
		configCustomObject = context.getObjectByName(Custom.class, CONFIG_NAME);
	}
	
	private void save() throws GeneralException {
		context.saveObject(configCustomObject);
		context.commitTransaction();
	}

	private String messageToJson(Message message) {
		Map<String, String> result = new HashMap<>();
		
		result.put(NAME.key, message.getName());
		result.put(BODY.key, message.getBody());
		result.put(FOOTER.key, message.getFooter());
		
		return new Gson().toJson(result);
	}
	//endregion Private Methods
}
