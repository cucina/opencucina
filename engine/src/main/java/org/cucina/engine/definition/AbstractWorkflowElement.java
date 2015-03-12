package org.cucina.engine.definition;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Common properties holder
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class AbstractWorkflowElement
    implements WorkflowElement {
    /**
    * Stores the description for this <code>NetworkElement</code>.
    */
    private String description;

    /**
     * Stores the ID for this <code>NetworkElement</code>.
     */
    private String id;

    /**
     * Stores the {@link ProcessDefinition} for this <code>NetworkElement</code>.
     */
    private ProcessDefinition workflowDefinition;

    /**
     * Sets the description for this <code>NetworkElement</code>.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the description for this <code>NetworkElement</code>.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the ID for this <code>NetworkElement</code>.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the ID for this <code>NetworkElement</code>.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the {@link ProcessDefinition} that contains this <code>NetworkElement</code>.
     */
    public void setWorkflowDefinition(ProcessDefinition workflowDefinition) {
        this.workflowDefinition = workflowDefinition;
    }

    /**
     * Gets the {@link ProcessDefinition} that contains this <code>NetworkElement</code>.
     */
    public ProcessDefinition getWorkflowDefinition() {
        return workflowDefinition;
    }

    /**
     * Returns String including all elements of the Workflow definition
     * @return String
     */
    public String toLongString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    public String toString() {
        return new ToStringBuilder(this).append("Id", getId()).toString();
    }
}