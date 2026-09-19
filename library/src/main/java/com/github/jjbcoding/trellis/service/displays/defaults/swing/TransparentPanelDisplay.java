package com.github.jjbcoding.trellis.service.displays.defaults.swing;

import com.github.jjbcoding.trellis.service.displays.IDisplay;
import com.github.jjbcoding.trellis.service.Node;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;

/**
 * This element is an extension of JPanel, implementing IDisplay. It
 * provides no user interface itself, only reflecting the child element,
 * which must be JPanel or a subtype thereof.
 */
@SuppressWarnings("unused")
public class TransparentPanelDisplay extends JPanel
	implements IDisplay {
	// Serial version ID
	@Serial
	private static final long serialVersionUID = 1L;
	// Fields
	Node _parent;
	// Components
	JPanel child;

	/**
	 * Constructs a TransparentPanelDisplay instance.
	 * @param _parent	The parent Node
	 */
	@SuppressWarnings("unused")
	public TransparentPanelDisplay(Node _parent) {
		// Super
		super(new BorderLayout());
		// Set parent
		this._parent = _parent;
		
		// Configure
		setOpaque(false);
	}

	// ----- INTERFACE IMPLEMENTATIONS
	// * [ IDisplay ]
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unused")
	@Override
	public Node getNode() {
		return _parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unused")
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
