
package org.cucina.engine.server.event.workflow;

import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class StartWorkflowEvent
    extends ProcessEvent {
    private static final long serialVersionUID = -2588190857865779918L;
    private Map<String, Object> parameters;

    /**
     * Creates a new StartWorkflowEvent object.
     *
     * @param source JAVADOC.
     */
    public StartWorkflowEvent(Object source, String applicationName) {
        super(source, applicationName);
    }

    /**
     * Creates a new StartWorkflowEvent object. Need it for JSON serialization.
     */
    public StartWorkflowEvent() {
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
}
