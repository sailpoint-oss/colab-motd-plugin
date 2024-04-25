/*
 * Copyright (c) 2021 Ventum Consulting GmbH
 */

package com.ventum.iiq.plugins.motd.model;

public enum HtmlPayloadElement {
	NAME("name"), BODY("body"), FOOTER("footer"), ACTIVE("active");
	
	//region Local Fields
	public final String key;
	//endregion Local Fields

	
	//region Constructors
	HtmlPayloadElement(String key) {
		this.key = key;
	}
	//endregion Constructors
}
