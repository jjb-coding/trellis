package com.github.jjbcoding.trellis.examples.injectables.constructed;

import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.Node;

public class LogInData extends Injectable {
	// ----- DYNAMIC
	// *** FIELDS
	String securityEmail, accountUUID, sessionToken;
	
	// *** CONSTRUCTORS
	public LogInData(AppService _appService, Node parent) {
		super(_appService, parent);
	}
	
	// *** METHODS
	// ** PUBLIC
	public void configure(String securityEmail, String accountUUID, String sessionToken) {
		this.securityEmail = securityEmail;
		this.accountUUID = accountUUID;
		this.sessionToken = sessionToken;
	}

	// Getters
	public String getSecurityEmail() {
		return securityEmail;
	}
}
