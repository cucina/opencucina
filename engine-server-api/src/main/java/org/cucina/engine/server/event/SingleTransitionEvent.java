package org.cucina.engine.server.event;

import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SingleTransitionEvent
    extends ProcessEvent {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> extraParams;
    private Object attachment;
    private String approvedAs;
    private String assignedTo;
    private String comment;
    private String transitionId;

    /**
    * Creates a new SingleTransitionEvent object.
    *
    * @param source JAVADOC.
    */
    public SingleTransitionEvent(Object source, String applicationName) {
        super(source, applicationName);
    }

    /**
     * Creates a new SingleTransitionEvent object.
     */
    public SingleTransitionEvent() {
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param approvedAs JAVADOC.
     */
    public void setApprovedAs(String approvedAs) {
        this.approvedAs = approvedAs;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getApprovedAs() {
        return approvedAs;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param assignedTo JAVADOC.
     */
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getAssignedTo() {
        return assignedTo;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param attachment JAVADOC.
     */
    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Object getAttachment() {
        return attachment;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param comment JAVADOC.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getComment() {
        return comment;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param extraParams JAVADOC.
     */
    public void setExtraParams(Map<String, Object> extraParams) {
        this.extraParams = extraParams;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Map<String, Object> getExtraParams() {
        return extraParams;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param transitionId JAVADOC.
     */
    public void setTransitionId(String transitionId) {
        this.transitionId = transitionId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getTransitionId() {
        return transitionId;
    }
}
