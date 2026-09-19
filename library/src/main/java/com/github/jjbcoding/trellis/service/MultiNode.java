package com.github.jjbcoding.trellis.service;

import com.github.jjbcoding.trellis.exceptions.ResolutionTermination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A MultiNode extends Node. It persists all immediate children,
 * allowing them to retain state and be disposed of simultaneously.
 */
@SuppressWarnings("unused")
public abstract class MultiNode extends Node {
	// ----- CONTRACTUAL
	@Override
	Node nextNode(Class<? extends Node> nodeClass, InjectableBag bag) {
		Node node = persistentChildren.get(nodeClass);
		if (node == null)
			throw new ResolutionTermination("app:resolution:state: Couldn't find child Node on MultiNode " + nodeClass.getSimpleName());
		return node;
	}

	@Override
	void previousNode(Node node) {}

	@Override
	List<Node> livingNodesToList() {
		return new ArrayList<>(persistentChildren.values());
	}

	// ----- DYNAMIC
	// *** FIELDS
	HashMap<Class<?>, Node> persistentChildren;
	
	// *** CONSTRUCTORS
	/**
	 * Creates an instance of a MultiNode.
	 * @param _appService		The app service
	 * @param parent			The parent
	 * @param bag				The Injectable bag
	 */
	@SuppressWarnings("unused")
	MultiNode(AppService _appService, Node parent, InjectableBag bag) {
		super(_appService, parent, bag);

		NodeConfiguration configuration = _appService.objToConfig(this);
		for (NodeConfiguration childConfiguration : configuration.childrenConfigurations) {
			Class<? extends Node> childNode = childConfiguration.thisCls;
			persistentChildren.put(childNode, _appService.launchNode(this, childNode, bag));
		}
	}
}
