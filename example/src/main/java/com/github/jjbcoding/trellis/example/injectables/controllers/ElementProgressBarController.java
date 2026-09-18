package com.github.jjbcoding.trellis.example.injectables.controllers;

import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.Node;

import javax.swing.*;

@SuppressWarnings("unused")
public class ElementProgressBarController extends Injectable {
	// ----- DYNAMIC
	// *** FIELDS
	// Injections
	JProgressBar progressBar;

	// *** CONTROLLERS
	public ElementProgressBarController(AppService _appService, Node parent) {
		super(_appService, parent);
	}

	// *** METHODS
	// ** PUBLIC
	public void attach(
		JProgressBar progressBar
	) {
		this.progressBar = progressBar;
		progressBar.revalidate();
		progressBar.repaint();
	}
	
	public void setProgress(float x) {
		progressBar.setValue((int)x * 100);
	}
}
