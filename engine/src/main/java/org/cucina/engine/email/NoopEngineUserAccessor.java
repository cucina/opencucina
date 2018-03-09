package org.cucina.engine.email;


/**
 * Default no op implementation of {@link UserAccessorBean}.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class NoopEngineUserAccessor
		implements UserAccessorBean {
	/**
	 * Returns null {@link engineUser}.
	 *
	 * @return null.
	 */
	@Override
	public Object getCurrentUser() {
		return null;
	}
}
