package com.github.jjbcoding.trellis.examples.displays;

import com.github.jjbcoding.trellis.service.displays.IDisplay;
import com.github.jjbcoding.trellis.examples.injectables.controllers.ElementProgressBarController;
import com.github.jjbcoding.trellis.service.Node;
import com.github.jjbcoding.trellis.examples.enums.injectables.Injectables;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

@SuppressWarnings("unused")
public class ElementProgressBarDisplay extends JPanel
	implements IDisplay {
	// ----- DYNAMIC
	// *** FIELDS
	// Serial version ID
	@Serial
	private static final long serialVersionUID = 1L;
	// Fields
	Node _parent;

	// *** CONSTRUCTORS
	public ElementProgressBarDisplay(Node _parent) {
		// * Super
		super(new BorderLayout(0, 0));

		// * Registrations
		this._parent = _parent;
		ElementProgressBarController _controller = (ElementProgressBarController)inject(Injectables.PROGRESSBAR_CONTROLLER);
		
		// * UI
		// Logout
		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(false);
		progressBar.setForeground(Color.BLUE);
		progressBar.setBorderPainted(false);
		progressBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		progressBar.setPreferredSize(new Dimension(0, 20));

		// Containerise & add
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(progressBar, BorderLayout.CENTER);
		add(topPanel, BorderLayout.NORTH);

		// This
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		
		// * Attach
		_controller.attach(
				progressBar
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
	    
	    // Add
 		add((JPanel)child, BorderLayout.CENTER);
	};
}
