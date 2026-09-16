package com.github.jjbcoding.trellis.examples.injectables.controllers;

import com.github.jjbcoding.trellis.examples.enums.states.States;
import com.github.jjbcoding.trellis.examples.injectables.constructed.ProgressBarPublisher;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.Node;
import com.github.jjbcoding.trellis.examples.enums.injectables.Injectables;

import javax.swing.*;

@SuppressWarnings("unused")
public class LogInController extends Injectable {
	// Injections
	ProgressBarPublisher pub;

    /**
     * Constructs a LogInController instance.
     * @param _appService 	The appService
     * @param parent        The parent
     */
    protected LogInController(AppService _appService, Node parent) {
        super(_appService, parent);
    }

    /**
	 * Post-constructor initialisation for injection.
	 */
	public void initialise() {
        // Injections
        pub = (ProgressBarPublisher)inject(Injectables.PROGRESSBAR_PUBLISHER);
	}
	
	public void attach(
			JButton loginButton,
			JButton cancelButton,
			JLabel statusLabel,
    		JTextField emailField,
            JLabel emailError,
    		JPasswordField passwordField,
            JLabel passwordError) {
		loginButton.addActionListener(e -> {});
		cancelButton.addActionListener(e -> setState(States.LOG_IN));
	}
}
