
package org.cucina.engine.server.event.workflow;

import java.util.Collection;

import org.cucina.engine.server.event.GetValueEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ListWorkflowPropertiesEvent
    extends GetValueEvent {
    private static final long serialVersionUID = 2822971108610601836L;
    private Collection<Long> ids;
    private String applicationType;

    /**
    * Creates a new ListWorkflowPropertiesEvent object.
    *
    * @param source JAVADOC.
    */
    public ListWorkflowPropertiesEvent(Object source, String applicationName) {
        super(source, applicationName);
    }

    /**
     * Creates a new ListWorkflowPropertiesEvent object.
     */
    public ListWorkflowPropertiesEvent() {
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
     * @param ids JAVADOC.
     */
    public void setIds(Collection<Long> ids) {
        this.ids = ids;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Collection<Long> getIds() {
        return ids;
    }
}
