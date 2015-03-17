
package org.cucina.engine.server.event.workflow;

import org.cucina.engine.server.event.GetValueEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ObtainHistoryEvent
    extends GetValueEvent {
    private static final long serialVersionUID = 719903452788460738L;
    private Long id;
    private String applicationType;

    /**
    * Creates a new StartWorkflowEvent object.
    *
    * @param source JAVADOC.
    */
    public ObtainHistoryEvent(Object source, String applicationName) {
        super(source, applicationName);
    }

    /**
     * Creates a new ObtainHistoryEvent object.
     */
    public ObtainHistoryEvent() {
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType JAVADOC.
     */
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getApplicationType() {
        return applicationType;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Long getId() {
        return id;
    }
}
