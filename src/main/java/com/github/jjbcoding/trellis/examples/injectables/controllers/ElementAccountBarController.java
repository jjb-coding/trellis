package com.github.jjbcoding.trellis.examples.injectables.controllers;

import com.github.jjbcoding.trellis.examples.enums.injectables.Injectables;
import com.github.jjbcoding.trellis.examples.enums.states.States;
import com.github.jjbcoding.trellis.examples.injectables.constructed.LogInData;
import com.github.jjbcoding.trellis.examples.requests.BackRequest;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.Node;

import javax.swing.*;

public class ElementAccountBarController extends Injectable {
	// ----- DYNAMIC
	// *** FIELDS
	// Injections
	LogInData logInData;

	// *** CONSTRUCTORS
	/**
	 * Constructs a controller.
	 * @param _appService		The appContainer.
	 * @param parent			The parent of the controller.
	 */
	public ElementAccountBarController(AppService _appService, Node parent) {
		super(_appService, parent);
	}

	// *** METHODS
	// ** PUBLIC
	public void initialise() {
		logInData = (LogInData)inject(Injectables.LOGIN_DATA);
	}
	
	public void attach(
			JButton logOutButton,
			JButton backButton,
			JLabel bannerText
	) {
		// Set email address
		bannerText.setText(logInData.getSecurityEmail());
		
		// Listeners
		logOutButton.addActionListener(e -> setState(States.LOG_IN));
		backButton.addActionListener(e -> getAppService().request(new BackRequest()));
	}
}
