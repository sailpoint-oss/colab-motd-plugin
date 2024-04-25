/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.resources.rest;

import com.ventum.iiq.plugins.motd.database.DataReadWrite;
import com.ventum.iiq.plugins.motd.exception.HttpBodyElementMissingException;
import com.ventum.iiq.plugins.motd.exception.InvalidHttpBodyException;
import com.ventum.iiq.plugins.motd.model.Message;
import org.apache.log4j.Logger;
import sailpoint.rest.plugin.AllowAll;
import sailpoint.rest.plugin.BasePluginResource;
import sailpoint.rest.plugin.RequiredRight;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static com.ventum.iiq.plugins.motd.utils.MotdUtil.*;


@SuppressWarnings("rawtypes")
@Path("motd")
@Produces({"application/json"})
@AllowAll
public class MotdResource extends BasePluginResource {
	//region Static Fields
	public static final Logger log = Logger.getLogger(MotdResource.class);
	//endregion Static Fields
	
	
	//region Public Methods
	@Override
	public String getPluginName() {
		return "motd";
	}
	
	@GET
	@Path("reference-plugin-service/getTitle/")
	public Response getTitle() {
		
		try (DataReadWrite io = new DataReadWrite(getContext())) {
			return getOkResponse(io.getTitle());
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return getServerErrorResponse("Unknown error getting plugin title, please check logs for more details");
	}
	
	@GET
	@Path("reference-plugin-service/getActiveMessage/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getActiveMessage() {
		
		try (DataReadWrite io = new DataReadWrite(getContext())) {
			log.debug("Getting active");
			
			return getOkResponse(io.getActiveMessage());
		} catch (Exception e) {
			log.error(e, e);
			return getServerErrorResponse("Unknown error getting active message, please check logs for more details");
		}
	}
	
	@GET
	@RequiredRight("ViewMessageOfTheDayRight")
	@Path("reference-plugin-service/getMessageNames/")
	public Response getMessagesNames() {
		
		try (DataReadWrite io = new DataReadWrite(getContext())) {
			return getOkResponse(io.getMessageNames());
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return getServerErrorResponse("Unknown error getting message names, please check logs for more details");
	}
	
	@PUT
	@RequiredRight("ViewMessageOfTheDayRight")
	@Path("reference-plugin-service/addMessage/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addMessage(Object body) {
		
		if (body == null) {
			return getClientErrorResponse("Body was not provided! null!");
		}
		
		if (!(body instanceof Map)) {
			if (body instanceof List) {
				return getClientErrorResponse("JSON Array is not supported! Use an Object!");
			} else {
				return getClientErrorResponse("Payload needs to be a JSON Object!");
			}
		}
		
		Message message;
		
		try {
			message = new Message((Map) body);
		} catch (InvalidHttpBodyException | HttpBodyElementMissingException e) {
			log.error(e, e);
			return getClientErrorResponse(e.getMessage());
		}
		
		
		try (DataReadWrite io = new DataReadWrite(getContext())) {
			log.debug("Body type: " + body.getClass().getSimpleName());
			
			io.addMessage(message);
			
			if (message.isActive()) {
				log.debug("Flag 'active' == true -> Setting as active!");
				io.setActiveMessage(message.getName());
			}
			
			return getOkResponse();
			
		} catch (Exception e) {
			log.error(e, e);
			return getServerErrorResponse("Unknown error adding message, please check logs for more details");
		}
	}
	
	@PUT
	@RequiredRight("ViewMessageOfTheDayRight")
	@Path("reference-plugin-service/setActiveMessage/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setActiveMessage(@PathParam("name") String name) {
		
		try (DataReadWrite io = new DataReadWrite(getContext())) {
			log.debug("Setting active");
			
			io.setActiveMessage(name);
			return getOkResponse();
			
		} catch (Exception e) {
			log.error(e, e);
			return getServerErrorResponse("Unknown error setting active message, please check logs for more details");
		}
	}
	
	@GET
	@RequiredRight("ViewMessageOfTheDayRight")
	@Path("reference-plugin-service/getMessage/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getMessage(@PathParam("name") String name) {
		
		try (DataReadWrite io = new DataReadWrite(getContext())) {
			log.debug("Getting message");
			
			return getOkResponse(io.getMessage(name));
			
		} catch (Exception e) {
			log.error(e, e);
			return getServerErrorResponse("Unknown error getting message, please check logs for more details");
		}
	}
	
	@DELETE
	@RequiredRight("ViewMessageOfTheDayRight")
	@Path("reference-plugin-service/deleteMessage/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMessage(@PathParam("name") String name) {
		
		try (DataReadWrite io = new DataReadWrite(getContext())) {
			log.debug("Deleting message");
			
			io.deleteMessage(name);
			
			return getOkResponse();
			
		} catch (Exception e) {
			log.error(e, e);
			return getServerErrorResponse("Unknown error deleting message, please check logs for more details");
		}
	}
	//endregion Public Methods
}
