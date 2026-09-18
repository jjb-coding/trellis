package com.github.jjbcoding.trellis.example.nodes;

import com.github.jjbcoding.trellis.example.displays.ElementAccountBarDisplay;
import com.github.jjbcoding.trellis.example.injectables.transfer.LogInData;
import com.github.jjbcoding.trellis.example.injectables.controllers.ElementAccountBarController;
import com.github.jjbcoding.trellis.service.annotations.Expects;
import com.github.jjbcoding.trellis.service.annotations.Parent;
import com.github.jjbcoding.trellis.service.annotations.Supplies;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.InjectableBag;
import com.github.jjbcoding.trellis.service.Node;

@Parent(ElementProgressBarNode.class)
@Expects(LogInData.class)
@Supplies(ElementAccountBarController.class)
@SuppressWarnings("unused")
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
