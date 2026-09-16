package com.github.jjbcoding.trellis.examples.nodes;

import com.github.jjbcoding.trellis.service.annotations.Expects;
import com.github.jjbcoding.trellis.service.annotations.Parent;
import com.github.jjbcoding.trellis.service.annotations.Supplies;
import com.github.jjbcoding.trellis.examples.displays.ElementAccountBarDisplay;
import com.github.jjbcoding.trellis.examples.injectables.constructed.LogInData;
import com.github.jjbcoding.trellis.examples.injectables.controllers.ElementAccountBarController;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.InjectableBag;
import com.github.jjbcoding.trellis.service.Node;

@Parent(ElementProgressBarNode.class)
@Expects(LogInData.class)
@Supplies(ElementAccountBarController.class)
public class ElementAccountBarNode extends Node {
	// ----- DYNAMIC
	// *** CONSTRUCTORS
	public ElementAccountBarNode(AppService _appService, Node parent, InjectableBag bag) {
		// * Super
		super(_appService, parent, bag);

		// * Display
		// Create
		setDisplay(new ElementAccountBarDisplay(this));
	}
}
