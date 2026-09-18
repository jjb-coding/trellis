package com.github.jjbcoding.trellis.example.displays;

import javax.swing.*;
import java.awt.*;
import com.github.jjbcoding.trellis.example.enums.injectables.Injectables;
import com.github.jjbcoding.trellis.example.injectables.controllers.ElementAccountBarController;
import java.io.Serial;

import com.github.jjbcoding.trellis.service.displays.IDisplay;
import com.github.jjbcoding.trellis.service.Node;

@SuppressWarnings("unused")
public class ElementAccountBarDisplay extends JPanel
	implements IDisplay {
	// ----- DYNAMIC
	// *** FIELDS
	// Serial version ID
	@Serial
	private static final long serialVersionUID = 1L;
	// Registrations
	Node _parent;

	// *** CONSTRUCTORS
	public ElementAccountBarDisplay(Node _parent) {
		// * Super
		super(new BorderLayout(0, 0));

		// * Registrations
		this._parent = _parent;
		ElementAccountBarController _controller = (ElementAccountBarController)inject(Injectables.ACCOUNTBAR_CONTROLLER);
		
		// * UI
		// Logout
		JButton logOutButton = new JButton();
		logOutButton.setOpaque(false);
		JButton backButton = new JButton();
		backButton.setOpaque(false);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		buttonPanel.add(logOutButton);
		buttonPanel.add(backButton);
		buttonPanel.setOpaque(false);
		
		// Text
		JLabel bannerText = new JLabel();
		JPanel bannerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bannerPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		bannerPanel.setOpaque(false);
		bannerPanel.add(bannerText);

		// Containerise & add
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(bannerPanel, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);
		
		// This
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		
		// * Attach
		_controller.attach(
				logOutButton,
				backButton,
				bannerText
				);
	}

	// ----- INTERFACE IMPLEMENTATIONS
	// * [ IDisplay ]
	@Override
	public Node getNode() {
		return _parent;
	}

	@Override
	public void swapChild(IDisplay child) {
	    // Clear
		for (Component component : getComponents())
	        if (BorderLayout.CENTER.equals(((BorderLayout)getLayout()).getConstraints(component)))
	            remove(component);
	    			
	    // Swap
 		add((JPanel)child, BorderLayout.CENTER);
	}
}
