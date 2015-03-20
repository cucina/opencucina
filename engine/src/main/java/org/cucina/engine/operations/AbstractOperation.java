
package org.cucina.engine.operations;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.definition.AbstractProcessElement;
import org.cucina.engine.definition.Operation;


/**
 * this class merges Operation with AbstractWorkflowElement.
 *
 * @author $author$
 * @version $Revision$
 */
public abstract class AbstractOperation
    extends AbstractProcessElement
    implements Operation {
    /**
     * JAVADOC.
     *
     * @param executionContext
     *            JAVADOC.
     */
    public abstract void execute(ExecutionContext executionContext);
}
