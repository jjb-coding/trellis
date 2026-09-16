package com.github.jjbcoding.trellis.service.displays;

import com.github.jjbcoding.trellis.exceptions.OperationException;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.Node;

/**
 * To be implemented by display elements, i.e. extensions of JFrame, JPanel.
 * This class is used to synchronise the underlying Node tree with the display
 * elements it corresponds to, and provide methods for IDisplay objects that
 * facilitate injection and access to the parent node.
 */
public interface IDisplay {
	// ----- ABSTRACT
	/**
	 * Retrieves the parent node.
	 * @return			The Node
	 */
	Node getNode();

	// ----- CONTRACTUAL
	// *** USER-OVERRIDDEN
	/**
	 * This method is used to synchronize reconfiguration of the
	 * underlying Node tree with the tree of {@link IDisplay} implementing objects.
	 * @param child		The child of the IDisplay element
	 */
	default void swapChild(IDisplay child) {}

	// ----- DEFAULT
	/**
	 * Sources an Injectable.
	 * @param injectableClass		The Injectable class
	 * @return						The Injectable
	 */
	default Object inject(Class<? extends Injectable> injectableClass) {
		return getNode().inject(injectableClass);
	}

	/**
	 * Sources an Injectable.
	 * @param injectableConstant	The Injectable constant
	 * @return						The Injectable
	 * @throws OperationException	if the constant is invalid
	 */
	default Object inject(Enum<?> injectableConstant) {
		return getNode().inject(injectableConstant);
	}

	/**
	 * Sources the AppService.
	 * @return						The AppService
	 */
	default AppService getAppService() {
		return getNode().getAppService();
	}

}
