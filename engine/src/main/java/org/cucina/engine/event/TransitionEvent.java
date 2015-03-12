
package org.cucina.engine.event;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.ApplicationEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TransitionEvent
    extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Map<String, Object> parameters;
    private String applicationType;
    private String workflowId;

    /**
     * Creates a new TransitionEvent object.
     *
     * @param parameters JAVADOC.
     * @param transitionId JAVADOC.
     * @param workflowId JAVADOC.
     * @param applicationType JAVADOC.
     * @param ids Long.
     */
    public TransitionEvent(String transitionId, String workflowId, String applicationType, Long id,
        Map<String, Object> parameters) {
        super(transitionId);
        this.parameters = parameters;
        this.workflowId = workflowId;
        this.applicationType = applicationType;
        this.id = id;
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
     * @return JAVADOC.
     */
    public Long getId() {
        return id;
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
    * @return JAVADOC.
    */
    public String getWorkflowId() {
        return workflowId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param obj JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof TransitionEvent)) {
            return false;
        }

        TransitionEvent event = (TransitionEvent) obj;

        return new EqualsBuilder().append(this.id, event.getId())
                                  .append(this.applicationType, event.applicationType)
                                  .append(this.workflowId, event.workflowId).isEquals();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(applicationType).append(workflowId)
                                    .toHashCode();
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
