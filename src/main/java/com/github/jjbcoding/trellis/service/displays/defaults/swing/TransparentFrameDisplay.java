package com.github.jjbcoding.trellis.service.displays.defaults.swing;

import com.github.jjbcoding.trellis.service.displays.IDisplay;
import com.github.jjbcoding.trellis.exceptions.ImplementationException;
import com.github.jjbcoding.trellis.service.Node;

import javax.swing.*;
import java.io.Serial;

/**
 * An empty JFrame that takes a JPanel child element.
 */
public class TransparentFrameDisplay
	extends JFrame
	implements IDisplay {
	// ----- DYNAMIC
	// *** FIELDS
	// Serial version ID
	@Serial
	private static final long serialVersionUID = 1L;
	// Fields
	Node _parent;
	// Components
	JPanel child;

	// *** CONSTRUCTORS
	/**
	 * Constructs a TransparentFrameDisplay instance.
	 * @param _parent	The parent
	 */
	@SuppressWarnings("unused")
	public TransparentFrameDisplay(Node _parent) {
		this._parent = _parent;
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
	    getContentPane().removeAll();
	    
	    // Swap
		if (!(child instanceof JPanel jPanelChild))
			throw new ImplementationException("TransparentFrameDisplay: child was not instanceof JPanel");
	    this.child = jPanelChild;
	    this.getContentPane().add(this.child);
	}
}
