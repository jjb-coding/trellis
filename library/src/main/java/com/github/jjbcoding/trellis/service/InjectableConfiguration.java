package com.github.jjbcoding.trellis.service;

import java.lang.reflect.InvocationTargetException;

/**
 * Classifies the data obtained from scanning a class whose superclass is Injectable.
 */
class InjectableConfiguration extends Configuration<Injectable> {
	// ----- DYNAMIC
	// *** CONSTRUCTORS
	InjectableConfiguration(Class<? extends Injectable> injectableClass) {
		super(injectableClass, false);
	}

	// *** METHODS
	// ** PACKAGE-PRIVATE
	Object execute(AppService _appService, Base parent) throws InvocationTargetException, InstantiationException, IllegalAccessException {
		Object[] values = new Object[2];
		values[0] = _appService;
		values[1] = parent;

		return constructor.newInstance(values);
	}
}
