package com.github.jjbcoding.trellis.service;

/**
 * Classifies the data obtained from scanning a class whose superclass is Injectable.
 */
public class InjectableConfiguration extends Configuration {
	// ----- DYNAMIC
	// *** CONSTRUCTORS
	public InjectableConfiguration(Class<?> cls) {
		super(cls, false);
	}

	// *** METHODS
	// ** PUBLIC
	/**
	 * Launches an Injectable.
	 * @param _appContainer
	 * @return
	 */
	public Object launch(AppService _appContainer, Base parent) {
		Object[] values = new Object[2];
		values[0] = _appContainer;
		values[1] = parent;

		Object ret;
		try {
			ret = constructor.newInstance(values);
		}
		catch (Exception e) {
			throw new RuntimeException("APP:runtime: Couldn't instantiate injectable " + thisCls.getSimpleName(), e.getCause());
		}
		return ret;
	}
}
