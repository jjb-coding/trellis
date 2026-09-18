package com.github.jjbcoding.trellis.example.nodes;

import com.github.jjbcoding.trellis.example.displays.DashboardDisplay;
import com.github.jjbcoding.trellis.example.injectables.controllers.DashboardController;
import com.github.jjbcoding.trellis.service.annotations.Parent;
import com.github.jjbcoding.trellis.service.annotations.Supplies;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.InjectableBag;
import com.github.jjbcoding.trellis.service.Node;
import com.github.jjbcoding.trellis.service.contributions.Contribution;
import com.github.jjbcoding.trellis.service.contributions.defaults.title.TitleContribution;

@Parent(ElementAccountBarNode.class)
@Supplies(DashboardController.class)
@SuppressWarnings("unused")
public class DashboardNode extends Node {
	// ----- DYNAMIC
	// *** CONSTRUCTORS
	public DashboardNode(AppService _appService, Node parent, InjectableBag bag) {
		// * Super
		super(_appService, parent, bag);

		// * Display
		// Create
		setDisplay(new DashboardDisplay(this));
	}

	// ----- INTERFACE IMPLEMENTATIONS
	// * [ Node ]
	@Override
	protected Contribution getContribution() {
		return new TitleContribution("Dashboard");
	}
}
