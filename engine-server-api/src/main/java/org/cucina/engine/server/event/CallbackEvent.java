
package org.cucina.engine.server.event;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.engine.server.definition.WorkflowElementDescriptor;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CallbackEvent
    extends OriginatedEngineEvent {
    private static final long serialVersionUID = 1837060619272315839L;
    private Map<String, Object> parameters;
    private WorkflowElementDescriptor workflowElementDescriptor;

    /**
    * Creates a new CallbackEvent object.
    *
    * @param element JAVADOC.
    */
    public CallbackEvent(WorkflowElementDescriptor element, Map<String, Object> parameters,
        String applicationName) {
        super(element);
        setApplicationName(applicationName);
        this.workflowElementDescriptor = element;
        this.parameters = parameters;
    }

    /**
     * Creates a new CallbackEvent object. Needed by JSON serializer.
     */
    public CallbackEvent() {
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param parameters JAVADOC.
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflowElementDescriptor JAVADOC.
     */
    public void setWorkflowElementDescriptor(WorkflowElementDescriptor workflowElementDescriptor) {
        this.workflowElementDescriptor = workflowElementDescriptor;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public WorkflowElementDescriptor getWorkflowElementDescriptor() {
        return workflowElementDescriptor;
    }

    /**
         * Default toString implementation
         *
         * @return This object as String.
         */
    @Override
    public String toString() {
        ToStringBuilder tob = new ToStringBuilder(this);

        return tob.appendSuper(super.toString())
                  .append("workflowElementDescriptor", workflowElementDescriptor).toString();
    }
}
