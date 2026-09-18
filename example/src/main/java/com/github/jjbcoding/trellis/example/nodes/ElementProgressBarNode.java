package com.github.jjbcoding.trellis.example.nodes;

import com.github.jjbcoding.trellis.example.displays.ElementProgressBarDisplay;
import com.github.jjbcoding.trellis.example.injectables.transfer.ProgressBarPublisher;
import com.github.jjbcoding.trellis.example.injectables.controllers.ElementProgressBarController;
import com.github.jjbcoding.trellis.service.InjectableBag;
import com.github.jjbcoding.trellis.service.annotations.Parent;
import com.github.jjbcoding.trellis.service.annotations.Supplies;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.Node;


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
