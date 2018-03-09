package org.cucina.engine.server.definition;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.definition.Operation;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class OperationDescriptor
		extends AbstractElementDescriptor
		implements Operation {
	/**
	 * @param executionContext .
	 */
	@Override
	public void execute(ExecutionContext executionContext) {
		throw new IllegalArgumentException("Not implemented in OperationDescriptor");
	}
}
