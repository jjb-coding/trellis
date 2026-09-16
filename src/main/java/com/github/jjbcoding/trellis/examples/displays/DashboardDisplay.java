package com.github.jjbcoding.trellis.examples.displays;

import com.github.jjbcoding.trellis.service.displays.IDisplay;
import com.github.jjbcoding.trellis.examples.injectables.controllers.DashboardController;
import com.github.jjbcoding.trellis.examples.enums.injectables.Injectables;
import com.github.jjbcoding.trellis.service.Node;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

public class DashboardDisplay extends JPanel
	implements IDisplay {
    // ----- DYNAMIC
    // *** FIELDS
	// Serial version ID
	@Serial
    private static final long serialVersionUID = 1L;
	// Registrations
    Node _parent;
    DashboardController _dashboardController;

    // *** CONSTRUCTOR
    public DashboardDisplay(Node _parent) {
    	// * Super
    	super(new BorderLayout());

    	// * Registrations
    	this._parent = _parent;
    	// Injections
    	_dashboardController = (DashboardController)inject(Injectables.DASHBOARD_CONTROLLER);

    	// * UI
        // Apply look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // This
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        // Welcome Panel / Create welcome panel at the top
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        JLabel welcomeLabel = new JLabel("Welcome!");
        welcomePanel.add(welcomeLabel);
        welcomePanel.setSize(186, 30);

        // Welcome Panel / Containerise & add
        JPanel welcomeContainerPanel = new JPanel();
        welcomeContainerPanel.add(welcomePanel);
        welcomeContainerPanel.setBackground(Color.BLUE);
        welcomeContainerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        add(welcomeContainerPanel, BorderLayout.NORTH);

        // Tabbed Pane / Create tabbed pane for main content
        JPanel iconsPanel = new JPanel();
		iconsPanel.setBackground(Color.BLUE);
        iconsPanel.setLayout(new GridLayout(2, 2, 80, 80));
        iconsPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Tabbed Pane / Create buttons
        JButton viewBookingsButton = createDashboardButton("View Bookings");
        JButton newBookingButton = createDashboardButton("New Booking");
        JButton patientDetailsButton = createDashboardButton("Details");
        JButton messagesButton = createDashboardButton("Messages");

        // Tabbed Pane / Add buttons to the button panel
        iconsPanel.add(viewBookingsButton);
        iconsPanel.add(newBookingButton);
        iconsPanel.add(patientDetailsButton);
        iconsPanel.add(messagesButton);

        // Tabbed Pane / Containerise & add
		JPanel iconsContainerPanel = new JPanel();
		iconsContainerPanel.setLayout(new BoxLayout(iconsContainerPanel, BoxLayout.Y_AXIS));
		iconsPanel.setPreferredSize(new Dimension(580,580));
		iconsPanel.setMaximumSize(new Dimension(580,580));
		iconsContainerPanel.add(iconsPanel);
		iconsContainerPanel.add(Box.createVerticalGlue());
        
        // Tabbed Pane / Add the tabbed pane
        add(iconsContainerPanel, BorderLayout.CENTER);
        
        // * Attach
        _dashboardController.attach(
        		welcomeLabel,
        		newBookingButton,
        		patientDetailsButton,
        		viewBookingsButton,
        		messagesButton
        		);

        // * Finish
        revalidate();
        repaint();
    }
    
    private JButton createDashboardButton(String text) {
    	JButton button = new JButton(text);
    	button.setForeground(Color.GREEN);
    	button.setBackground(Color.LIGHT_GRAY);
    	button.setText("<html><center>" + text + "</center></html>");
    	button.setSize(80, 80);
    	return button;
    }

    // ----- INTERFACE IMPLEMENTATIONS
    // * [ IDisplay ]
	@Override
	public Node getNode() {
		return _parent;
	}
}