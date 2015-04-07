package org.cucina.engine.server.event.workflow;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.cucina.engine.server.event.OriginatedEngineEvent;


/**
 * To identify non-infrastructural events
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class WorkflowEvent
    extends OriginatedEngineEvent {
    private static final long serialVersionUID = 675264777733240826L;
    private Object id;
    private String type;

    /**
    * Creates a new BusinessEvent object.
    *
    * @param source JAVADOC.
    */
    public WorkflowEvent(Object source, String applicationName) {
        super(source);
        setApplicationName(applicationName);
    }

    /**
     * Creates a new WorkflowEvent object. Needs it for JSON.
     */
    public WorkflowEvent() {
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     */
    public void setId(Object id) {
        this.id = id;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Object getId() {
        return id;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getType() {
        return type;
    }

    /**
         * Default toString implementation
         *
         * @return This object as String.
         */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
