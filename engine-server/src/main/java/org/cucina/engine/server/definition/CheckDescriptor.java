package org.cucina.engine.server.definition;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.definition.Check;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CheckDescriptor
    extends AbstractElementDescriptor implements Check {

	@Override
	public boolean test(ExecutionContext executionContext) {
		throw new IllegalArgumentException("Not implemented in CheckDescriptor");
	}
}
