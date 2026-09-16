package com.github.jjbcoding.trellis.examples.injectables.constructed;


import com.github.jjbcoding.trellis.examples.injectables.controllers.ElementProgressBarController;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.Node;
import com.github.jjbcoding.trellis.examples.enums.injectables.Injectables;

import java.util.Stack;

@SuppressWarnings("unused")
public class ProgressBarPublisher extends Injectable {
	// ----- DYNAMIC
	// *** FIELDS
	ElementProgressBarController progressBar;
	Stack<Float> stack;

	// *** CONSTRUCTORS
	public ProgressBarPublisher(AppService _appService, Node parent) {
		super(_appService, parent);
		stack = new Stack<>();
	}
	
	@Override
	public void initialise() {
		progressBar = (ElementProgressBarController)inject(Injectables.PROGRESSBAR_CONTROLLER);
	}

	// *** METHODS
	// ** PUBLIC
	/**
	 * Sends a progress change, within the current interval.
	 * @param progress	In the interval of 0.0 to 1.0.
	 */
	public void send(float progress) {
		sendAndGet(progress);
	}
	
	private float sendAndGet(float progress) {
		// Clamp the value
		if (progress > 1.0f)
			progress = 1.0f;
		if (progress < 0.0f)
			progress = 0.0f;
		
		// Get the current interval being used
		float start;
		if (stack.size() == 0)
			// Default (0.0,1.0)
			start = 0.0f;
		else
			start = stack.get(stack.size() - 1);
			
		// Map
		progress = start + ((1.0f - start) * progress);
		
		// Send to the progress bar controller
		progressBar.setProgress(progress);
		
		// Return transformed value
		return progress;
	}
	
	/**
	 * Updates the progress bar and maps future
	 * sends into the remaining region.
	 * @param start		A value. Will be clamped to the interval [0, 1]
	 */
	public void push(float start) {
		stack.push(sendAndGet(start));
	}
	
	/**
	 * Unrolls a single layer of mapping.
	 */
	public void pop() {
		stack.pop();
	}
	
	/**
	 * Resets the progress bar and stack.
	 */
	public void reset() {
		stack.empty();
		progressBar.setProgress(0);
	}
}
