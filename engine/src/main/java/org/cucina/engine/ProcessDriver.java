
package org.cucina.engine;

import java.util.List;

import org.cucina.engine.definition.Check;
import org.cucina.engine.definition.Operation;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ProcessDriver {
    /**
     * JAVADOC Method Level Comments
     *
     * @param actions JAVADOC.
     * @param executionContext JAVADOC.
     */
    void execute(List<Operation> actions, ExecutionContext executionContext);

    /**
     * JAVADOC Method Level Comments
     *
     * @param condition JAVADOC.
     * @param executionContext JAVADOC.
     *
     * @return JAVADOC.
     */
    boolean test(Check condition, ExecutionContext executionContext);
}
