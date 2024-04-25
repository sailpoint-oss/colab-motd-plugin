/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.test.util;

import com.vdurmont.emoji.EmojiManager;
import sailpoint.api.ObjectAlreadyLockedException;
import sailpoint.api.SailPointContext;
import sailpoint.connector.ExpiredPasswordException;
import sailpoint.object.*;
import sailpoint.tools.EmailException;
import sailpoint.tools.GeneralException;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.ventum.iiq.plugins.motd.model.Constants.CONFIG_NAME;
import static com.ventum.iiq.plugins.motd.model.Constants.KEY_MESSAGES;


@SuppressWarnings({"RedundantThrows", "DuplicateThrows"})
public class DummyContext implements SailPointContext {
	private Custom dummyConfig;
	private Custom uncommittedDummyConfig = null;
	
	public DummyContext(Map<String, Object> initConfig) {
		dummyConfig = new Custom();
		
		dummyConfig.setAttributes(new Attributes<>(initConfig));
	}
	
	@Override
	public SailPointContext getContext() {
		return this;
	}
	
	@Override
	public void prepare() {
	
	}
	
	@Override
	public Connection getJdbcConnection() throws GeneralException {
		return null;
	}
	
	@Override
	public Connection getConnection() throws GeneralException {
		return null;
	}
	
	@Override
	public boolean isClosed() {
		return false;
	}
	
	@Override
	public void setUserName(String s) {
	
	}
	
	@Override
	public String getUserName() {
		return null;
	}
	
	@Override
	public void impersonate(Identity identity) {
	
	}
	
	@Override
	public void setScopeResults(boolean b) {
	
	}
	
	@Override
	public boolean getScopeResults() {
		return false;
	}
	
	@Override
	public Configuration getConfiguration() throws GeneralException {
		return null;
	}
	
	@Override
	public String encrypt(String s) throws GeneralException {
		return null;
	}
	
	@Override
	public String encrypt(String s, boolean b) throws GeneralException {
		return null;
	}
	
	@Override
	public String decrypt(String s) throws GeneralException {
		return null;
	}
	
	@Override
	public void sendEmailNotification(EmailTemplate emailTemplate, EmailOptions emailOptions) throws GeneralException, EmailException {
	
	}
	
	@Override
	public Identity authenticate(String s, String s1) throws GeneralException, ExpiredPasswordException {
		return null;
	}
	
	@Override
	public Identity authenticate(String s, Map<String, Object> map) throws GeneralException, ExpiredPasswordException {
		return null;
	}
	
	@Override
	public void setProperty(String s, Object o) {
	
	}
	
	@Override
	public Object getProperty(String s) {
		return null;
	}
	
	@Override
	@SuppressWarnings("MethodDoesntCallSuperMethod")
	public Object clone() throws CloneNotSupportedException {
		return null;
	}
	
	@Override
	public void startTransaction() throws GeneralException {
	
	}
	
	@Override
	public void commitTransaction() throws GeneralException {
		if (uncommittedDummyConfig != null) {
			dummyConfig = uncommittedDummyConfig;
			uncommittedDummyConfig = null;
		}
	}
	
	@Override
	public void rollbackTransaction() throws GeneralException {
	
	}
	
	@Override
	public void close() throws GeneralException {
	
	}
	
	@Override
	public <T extends SailPointObject> T getObjectById(Class<T> aClass, String s) throws GeneralException {
		return null;
	}
	
	@SuppressWarnings("rawtypes unchecked")
	@Override
	public <T extends SailPointObject> T getObjectByName(Class<T> aClass, String s) throws GeneralException {
		if (aClass.equals(Custom.class) && s.equals(CONFIG_NAME)) {
			Custom copy = new Custom();
			
			
			Attributes<String, Object> attributes = dummyConfig.getAttributes();
			Attributes<String, Object> attributesClone = new Attributes<>();
			
			for (Map.Entry<String, Object> entry : attributes.entrySet()) {
				if (entry.getValue() instanceof HashMap) {
					attributesClone.put(entry.getKey(), new HashMap((Map) entry.getValue()));
				} else {
					attributesClone.put(entry.getKey(), entry.getValue());
				}
			}
			copy.setAttributes(attributesClone);
			
			return ((T) copy);
		} else {
			throw new GeneralException("This is a dummy context that supports only ONE custom object: '" + CONFIG_NAME + "'!");
		}
	}
	
	@Override
	public <T extends SailPointObject> T getObject(Class<T> aClass, String s) throws GeneralException {
		return null;
	}
	
	@Override
	public <T extends SailPointObject> T lockObjectById(Class<T> aClass, String s, Map<String, Object> map) throws ObjectAlreadyLockedException, GeneralException {
		return null;
	}
	
	@Override
	public <T extends SailPointObject> T lockObjectByName(Class<T> aClass, String s, Map<String, Object> map) throws ObjectAlreadyLockedException, GeneralException {
		return null;
	}
	
	@Override
	public <T extends SailPointObject> void unlockObject(T t) throws GeneralException {
	
	}
	
	@Override
	public <T extends SailPointObject> T getUniqueObject(T t) throws GeneralException {
		return null;
	}
	
	@Override
	public <T extends SailPointObject> T getUniqueObject(Class<T> aClass, Filter filter) throws GeneralException {
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void saveObject(SailPointObject sailPointObject) throws GeneralException {
		if (sailPointObject instanceof Custom) {
			Custom custom = (Custom) sailPointObject;
			
			// Check for emojis - these cannot be saved through the real context
			for (Map.Entry<String, Object> entry : custom.getAttributes().getMap().entrySet()) {
				if (EmojiManager.containsEmoji(entry.getKey()))
					throw new GeneralException("DUMMY org.hibernate.exception.GenericJDBCException: could not execute statement");
				
				
				Object value = entry.getValue();
				if (value instanceof String && EmojiManager.containsEmoji((String) value))
					throw new GeneralException("DUMMY org.hibernate.exception.GenericJDBCException: could not execute statement");
			}
			
			Map<String, String> messages = (Map<String, String>) custom.get(KEY_MESSAGES);
			
			for (Map.Entry<String, String> entry : messages.entrySet()) {
				if (EmojiManager.containsEmoji(entry.getKey()) || EmojiManager.containsEmoji(entry.getValue()))
					throw new GeneralException("DUMMY org.hibernate.exception.GenericJDBCException: could not execute statement");
			}
			
			uncommittedDummyConfig = custom;
		} else {
			throw new GeneralException("This is a dummy context that supports only ONE custom object: '" + CONFIG_NAME + "'!");
		}
	}
	
	@Override
	public void importObject(SailPointObject sailPointObject) throws GeneralException {
	
	}
	
	@Override
	public void removeObject(SailPointObject sailPointObject) throws GeneralException {
	
	}
	
	@Override
	public <T extends SailPointObject> void removeObjects(Class<T> aClass, QueryOptions queryOptions) throws GeneralException {
	
	}
	
	@Override
	public <T extends SailPointObject> List<T> getObjects(Class<T> aClass) throws GeneralException {
		return null;
	}
	
	@Override
	public <T extends SailPointObject> List<T> getObjects(Class<T> aClass, QueryOptions queryOptions) throws GeneralException {
		return null;
	}
	
	@Override
	public int countObjects(Class aClass, QueryOptions queryOptions) throws GeneralException {
		return 0;
	}
	
	@Override
	public <T extends SailPointObject> Iterator<T> search(Class<T> aClass, QueryOptions queryOptions) throws GeneralException {
		return null;
	}
	
	@Override
	public <T extends SailPointObject> Iterator<Object[]> search(Class<T> aClass, QueryOptions queryOptions, String s) throws GeneralException {
		return null;
	}
	
	@Override
	public <T extends SailPointObject> Iterator<Object[]> search(Class<T> aClass, QueryOptions queryOptions, List<String> list) throws GeneralException {
		return null;
	}
	
	@Override
	public int update(String s, Map<String, Object> map) throws GeneralException {
		return 0;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public Iterator search(String s, Map<String, Object> map, QueryOptions queryOptions) throws GeneralException {
		return null;
	}
	
	@Override
	public void attach(SailPointObject sailPointObject) throws GeneralException {
	
	}
	
	@Override
	public void decache(SailPointObject sailPointObject) throws GeneralException {
	
	}
	
	@Override
	public void decache() throws GeneralException {
	
	}
	
	@Override
	public void setPersistenceOptions(PersistenceOptions persistenceOptions) {
	
	}
	
	@Override
	public PersistenceOptions getPersistenceOptions() {
		return null;
	}
	
	@Override
	public void enableStatistics(boolean b) {
	
	}
	
	@Override
	public void printStatistics() {
	
	}
	
	@Override
	public void reconnect() throws GeneralException {
	
	}
	
	@Override
	public void clearHighLevelCache() throws GeneralException {
	
	}
	
	@Override
	public <T extends SailPointObject> T lockObject(Class<T> aClass, LockParameters lockParameters) throws GeneralException {
		return null;
	}
	
	@Override
	public Object runRule(Rule rule, Map<String, Object> map) throws GeneralException {
		return null;
	}
	
	@Override
	public Object runRule(Rule rule, Map<String, Object> map, List<Rule> list) throws GeneralException {
		return null;
	}
	
	@Override
	public Object runScript(Script script, Map<String, Object> map) throws GeneralException {
		return null;
	}
	
	@Override
	public Object runScript(Script script, Map<String, Object> map, List<Rule> list) throws GeneralException {
		return null;
	}
	
	@Override
	public Object getReferencedObject(String s, String s1, String s2) throws GeneralException {
		return null;
	}
}
