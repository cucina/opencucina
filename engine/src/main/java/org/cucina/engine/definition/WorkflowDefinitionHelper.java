
package org.cucina.engine.definition;


/**
 * Provides additional functionality to interrogate WorkflowDefinition
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface WorkflowDefinitionHelper {
    /**
     * JAVADOC Method Level Comments
     *
     * @param token JAVADOC.
     *
     * @return JAVADOC.
     */
    boolean isEnded(Token token);

    /**
     * Find the placeId corresponding to this <code>workflowId</code> and <code>transitionId</code>.
     *
     * @param workflowId String.
     * @param transitionId String.
     *
     * @return placeId String that has this transition.
     */
    String findPlaceId(String workflowId, String transitionId);
}
