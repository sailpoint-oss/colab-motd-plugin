/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.utils;

import javax.ws.rs.core.Response;

public class MotdUtil {
	//region Constructors
	private MotdUtil() {}
	//endregion Constructors
	
	
	//region Public Methods
	public static Response getOkResponse() {
		return Response.status(Response.Status.OK).build();
	}
	
	public static Response getOkResponse(String entity) {
		return Response.status(Response.Status.OK).entity(entity).build();
	}
	
	public static Response getClientErrorResponse(String message) {
		return getErrorResponse(Response.Status.BAD_REQUEST, message);
	}
	
	public static Response getServerErrorResponse(String message) {
		return getErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, message);
	}
	
	public static Response getErrorResponse(Response.Status status, String message) {
		return Response.status(status).entity(message).build();
	}
	//endregion Public Methods
}
