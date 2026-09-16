package com.github.jjbcoding.trellis.examples.nodes;

import com.github.jjbcoding.trellis.service.InjectableBag;
import com.github.jjbcoding.trellis.service.annotations.Parent;
import com.github.jjbcoding.trellis.service.annotations.Supplies;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.Node;
import com.github.jjbcoding.trellis.examples.injectables.controllers.ElementProgressBarController;
import com.github.jjbcoding.trellis.examples.displays.ElementProgressBarDisplay;
import com.github.jjbcoding.trellis.examples.injectables.constructed.ProgressBarPublisher;


@Parent(AppNode.class)
@Supplies({ProgressBarPublisher.class, ElementProgressBarController.class})
@SuppressWarnings("unused")
public class ElementProgressBarNode extends Node {
	// ----- DYNAMIC
	// *** CONSTRUCTORS
	public ElementProgressBarNode(AppService appService, Node parent, InjectableBag bag) {
		// * Super
		super(appService, parent, bag);

		// * Display
		// Create
		setDisplay(new ElementProgressBarDisplay(this));
	}

}
