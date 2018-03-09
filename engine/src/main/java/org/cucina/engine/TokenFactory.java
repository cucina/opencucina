package org.cucina.engine;

import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.Token;

public interface TokenFactory {
	/**
	 * Creates a new instance of the {@link Token} class.
	 *
	 * @param domainObject an arbitrary domain object to be associated with the
	 *                     {@link Token} class.
	 * @return the newly created {@link Token} instance.
	 * @throws IllegalArgumentException if either of the supplied arguments is null.
	 */
	Token createToken(ProcessDefinition definition, Object domainObject);
}
