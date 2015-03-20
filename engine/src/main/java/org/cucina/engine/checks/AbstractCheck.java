
package org.cucina.engine.checks;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.definition.AbstractProcessElement;
import org.cucina.engine.definition.Check;


/**
 * this class merges Check with AbstractWorkflowElement.
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class AbstractCheck
    extends AbstractProcessElement
    implements Check {
    /**
    * JAVADOC.
    *
    * @param executionContext JAVADOC.
    *
    * @return JAVADOC.
    */
    public abstract boolean test(ExecutionContext executionContext);
}
