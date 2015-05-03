package org.cucina.engine.server.event;

import java.io.Serializable;
import java.util.Collection;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ListProcessPropertiesEvent
    extends GetValueEvent {
    private static final long serialVersionUID = 2822971108610601836L;
    private Collection<Serializable> ids;
    private String applicationType;

    /**
    * Creates a new ListWorkflowPropertiesEvent object.
    *
    * @param source JAVADOC.
    */
    public ListProcessPropertiesEvent(Object source, String applicationName) {
        super(source, applicationName);
    }

    /**
     * Creates a new ListWorkflowPropertiesEvent object.
     */
    public ListProcessPropertiesEvent() {
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
    public void setIds(Collection<Serializable> ids) {
        this.ids = ids;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Collection<Serializable> getIds() {
        return ids;
    }
}
