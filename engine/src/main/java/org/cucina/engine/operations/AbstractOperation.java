
package org.cucina.engine.operations;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.definition.AbstractWorkflowElement;
import org.cucina.engine.definition.Operation;


/**
 * this class merges Operation with AbstractWorkflowElement.
 *
 * @author $author$
 * @version $Revision$
 */
public abstract class AbstractOperation
    extends AbstractWorkflowElement
    implements Operation {
    /**
     * JAVADOC.
     *
     * @param executionContext
     *            JAVADOC.
     */
    public abstract void execute(ExecutionContext executionContext);
}
