package org.cucina.engine.server.event.workflow;

import java.io.Serializable;

import java.util.Collection;

import org.cucina.engine.server.event.GetValueEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ListAllTransitionsEvent
    extends GetValueEvent {
    private static final long serialVersionUID = 2822971108610601836L;
    private Collection<Serializable> ids;
    private String applicationType;

    /**
    * Creates a new StartWorkflowEvent object.
    *
    * @param source JAVADOC.
    */
    public ListAllTransitionsEvent(Object source, String applicationName) {
        super(source, applicationName);
    }

    /**
     * Creates a new ListAllTransitionsEvent object.
     */
    public ListAllTransitionsEvent() {
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
