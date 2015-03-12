
package org.cucina.engine.definition;


/**
 * The internal state of a {@link ProcessDefinition} is made up of individual
 * {@link WorkflowElement} that describe the states and transitions that
 * constitute the workflow.
 *
 * @author Rob Harrop
 */
public interface WorkflowElement {
    /**
     * Gets the description for this <code>NetworkElement</code>.
     */
    String getDescription();

    /**
     * Gets the identity of this <code>NetworkElement</code>.
     */
    String getId();

    /**
     * Sets the {@link ProcessDefinition} that this <code>NetworkElement</code>
     * is part of.
     */
    void setWorkflowDefinition(ProcessDefinition worklowDefinition);

    /**
     * Gets the {@link ProcessDefinition} that this <code>NetworkElement</code>
     * is part of.
     */
    ProcessDefinition getWorkflowDefinition();
}
