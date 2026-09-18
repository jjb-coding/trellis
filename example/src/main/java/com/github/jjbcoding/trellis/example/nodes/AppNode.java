package com.github.jjbcoding.trellis.example.nodes;

import com.github.jjbcoding.trellis.example.enums.states.States;
import com.github.jjbcoding.trellis.example.requests.BackRequest;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.Digest;
import com.github.jjbcoding.trellis.service.InjectableBag;
import com.github.jjbcoding.trellis.service.contributions.Contribution;
import com.github.jjbcoding.trellis.service.Node;
import com.github.jjbcoding.trellis.service.contributions.defaults.title.TitleContribution;
import com.github.jjbcoding.trellis.service.contributions.defaults.title.TitleDigestComponent;
import com.github.jjbcoding.trellis.service.requests.Request;
import com.github.jjbcoding.trellis.service.displays.defaults.swing.TransparentFrameDisplay;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EnumMap;

@SuppressWarnings("unused")
public class AppNode extends Node {
	// ----- STATIC
	static final EnumMap<States, States> backMap;
	static {
		backMap = new EnumMap<>(States.class);
		backMap.put(States.MAKE_BOOKING,	States.DASHBOARD);
		backMap.put(States.UPDATE_DETAILS,	States.DASHBOARD);
		backMap.put(States.VIEW_BOOKINGS,	States.DASHBOARD);
	}

	// ----- DYNAMIC
	// *** CONSTRUCTORS
	public AppNode(AppService _appService, Node parent, InjectableBag bag) {
		// * Super
		super(_appService, parent, bag);

		// * Display
		// Create
		TransparentFrameDisplay frame = new TransparentFrameDisplay(this);
		setDisplay(frame);

		// Build
		frame.setResizable(false);
		frame.setSize(600,760);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
				getAppService().close();
            }
        });
        
        // * Backspace
        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "BACK_EVENT");
        actionMap.put("BACK_EVENT", new AbstractAction() {
			@Override
            public void actionPerformed(ActionEvent e) {
				getAppService().request(new BackRequest());
            }
        });

		// * Finish
        frame.setVisible(true);
	}

	// ----- INTERFACE IMPLEMENTATIONS
	// * [ Node ]
	@Override
	protected Contribution getContribution() {
		return new TitleContribution("App");
	}

	@Override
	protected void catchDigest(Digest digest) {
		String title = ((TitleDigestComponent)digest.consume(TitleDigestComponent.class)).getTitle();

		JFrame frame = ((JFrame)getDisplay());
		frame.setTitle(title);
		frame.validate();
		frame.repaint();
	}

	@Override
	protected boolean catchRequest(Request request) {
		if (!(request instanceof BackRequest))
			return false;

		States constant = (States)getAppService().getState();
		setState(backMap.get(constant));
		return true;
	}
}
