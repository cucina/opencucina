
package org.cucina.engine.definition;


/**
 * The internal state of a {@link ProcessDefinition} is made up of individual
 * {@link ProcessElement} that describe the states and transitions that
 * constitute the workflow.
 *
 * @author Rob Harrop
 */
public interface ProcessElement {
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
    void setProcessDefinition(ProcessDefinition worklowDefinition);

    /**
     * Gets the {@link ProcessDefinition} that this <code>NetworkElement</code>
     * is part of.
     */
    ProcessDefinition getProcessDefinition();
}
