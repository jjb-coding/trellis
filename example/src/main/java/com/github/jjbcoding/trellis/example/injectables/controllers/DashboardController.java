package com.github.jjbcoding.trellis.example.injectables.controllers;


import com.github.jjbcoding.trellis.example.enums.injectables.Injectables;
import com.github.jjbcoding.trellis.example.enums.states.States;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.example.injectables.transfer.LogInData;
import com.github.jjbcoding.trellis.service.Node;

import javax.swing.*;

@SuppressWarnings("unused")
public class DashboardController extends Injectable {
	// ----- DYNAMIC
	// *** FIELDS
	// Injections
	LogInData _logInData;

	// *** CONSTRUCTORS
	/**
	 * Constructs a controller.
	 * @param appService		The appService
	 * @param parent			The parent of the controller
	 */
	public DashboardController(AppService appService, Node parent) {
		super(appService, parent);
	}

	// *** METHODS
	// ** PUBLIC
	/**
	 * Post-constructor initialisation. For injection.
	 */
	public void initialise() {
    	_logInData = (LogInData)inject(Injectables.LOGIN_DATA);
	}
	
	public void attach(
			JLabel welcomeLabel,
    		JButton newBookingButton,
    		JButton patientDetailsButton,
    		JButton viewBookingsButton,
    		JButton logoutButton
    		) {
		welcomeLabel.setText("Welcome, " + _logInData.getSecurityEmail());

        newBookingButton.addActionListener(e -> setState(States.MAKE_BOOKING));
        patientDetailsButton.addActionListener(e -> setState(States.UPDATE_DETAILS));
        viewBookingsButton.addActionListener(e -> setState(States.VIEW_BOOKINGS));
        logoutButton.addActionListener(e -> setState(States.LOG_IN));
        
	}
}
