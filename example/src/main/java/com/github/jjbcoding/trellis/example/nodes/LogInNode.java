package com.github.jjbcoding.trellis.example.nodes;

import com.github.jjbcoding.trellis.example.displays.LogInDisplay;
import com.github.jjbcoding.trellis.example.injectables.controllers.LogInController;
import com.github.jjbcoding.trellis.service.annotations.Parent;
import com.github.jjbcoding.trellis.service.annotations.Supplies;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.InjectableBag;
import com.github.jjbcoding.trellis.service.Node;
import com.github.jjbcoding.trellis.service.contributions.Contribution;
import com.github.jjbcoding.trellis.service.contributions.defaults.title.TitleContribution;

@Parent(ElementProgressBarNode.class)
@Supplies(LogInController.class)
@SuppressWarnings("unused")
public class LogInNode extends Node {
	public LogInNode(AppService _appService, Node parent, InjectableBag bag) {
		// * Super
		super(_appService, parent, bag);

		// * Display
		// Create
		setDisplay(new LogInDisplay(this));
	}

	// ----- INTERFACE IMPLEMENTATIONS
	// * [ Node ]
	@Override
	protected Contribution getContribution() {
		return new TitleContribution("Log In");
	}
}
