package com.github.jjbcoding.trellis.examples.displays;


import com.github.jjbcoding.trellis.service.displays.IDisplay;
import com.github.jjbcoding.trellis.examples.injectables.controllers.LogInController;
import com.github.jjbcoding.trellis.service.Node;
import com.github.jjbcoding.trellis.examples.enums.injectables.Injectables;

import javax.swing.*;
import java.awt.*;

/**
 * Login GUI
 */
public class LogInDisplay extends JPanel
	implements IDisplay {
    // The serial version ID.
	private static final long serialVersionUID = 1L;
	
	// Injections
	Node _parent;
	LogInController _controller;
	

    public LogInDisplay(Node _parent) {
    	// Call super
    	super(new BorderLayout());
    	// Set parent
    	this._parent = _parent;
    	// Get injectables
    	_controller = (LogInController)inject(Injectables.LOGIN_CONTROLLER);
    	        
    	// * COMPONENTS
    	// Create input fields and labels
    	JTextField emailField = new JTextField(20);
        JLabel emailError = new JLabel();
        JPasswordField passwordField = new JPasswordField(20);
        JLabel passwordError = new JLabel();
        
        // Create buttons with consistent size
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");
        Dimension buttonSize = new Dimension(100, 25);
        loginButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        
        // Status
        JLabel statusLabel = new JLabel();
        
        // * NORTH -> INSTRUCTION
        // Create instruction label
        JLabel instructionLabel = new JLabel();
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.BLUE);
        northPanel.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createEtchedBorder(),
        	    BorderFactory.createEmptyBorder(10, 10, 10, 10)
        	));
        JPanel instructionContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        instructionContainer.add(instructionLabel);
        instructionContainer.setOpaque(false);
        northPanel.add(instructionContainer, BorderLayout.CENTER);
        
        // * CENTRE -> FORM PANEL
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Email label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.7;
        formPanel.add(new JLabel(), gbc);
        
        // Email field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);
        
        // Email error
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        formPanel.add(emailError, gbc);
        
        // Password label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.7;
        formPanel.add(new JLabel(""), gbc);
        
        // Password field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        // Password error
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        formPanel.add(passwordError, gbc);
        
        // Container
        JPanel formPanelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formPanelContainer.setBackground(Color.GREEN);
        formPanelContainer.setPreferredSize(new Dimension(0, 250));
        formPanelContainer.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        formPanelContainer.add(formPanel);
        
        // * SOUTH -> BUTTONS & STATUS PANEL
        JPanel southPanel = new JPanel(new BorderLayout(0, 10));
        southPanel.setBackground(Color.WHITE);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        
        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.add(statusLabel);
        southPanel.add(statusPanel, BorderLayout.SOUTH);

        // * MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(160, 120, 160, 120));
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(formPanelContainer, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.setBackground(Color.WHITE);
        
        // * THIS
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        add(mainPanel, BorderLayout.CENTER);
        
        // * OPAQUE
		setOpaque(false);
		formPanel.setOpaque(false);
        
        // *** ATTACH
        _controller.attach(
                loginButton,
                cancelButton,
                statusLabel,
                emailField,
                emailError,
                passwordField,
                passwordError
        		);
    }

    // ----- INTERFACE IMPLEMENTATIONS
    // * [ IDisplay ]
	@Override
	public Node getNode() {
		return _parent;
	}
}