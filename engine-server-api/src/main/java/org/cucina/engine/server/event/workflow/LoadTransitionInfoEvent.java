
package org.cucina.engine.server.event.workflow;



/**
 * Event that requests transitional information for this workflow
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class LoadTransitionInfoEvent
    extends GetValueEvent {
    private static final long serialVersionUID = 5952453460362614699L;
    private String workflowId;

    /**
     * Creates a new ListTransitionInfoEvent object.
     *
     * @param source Object.
     * @param applicationName String.
     */
    public LoadTransitionInfoEvent(Object source, String applicationName) {
        super(source, applicationName);
    }

    /**
     * Creates a new LoadTransitionInfoEvent object.
     */
    public LoadTransitionInfoEvent() {
    }

    /**
     * Set workflowId
     *
     * @param workflowId String.
     */
    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    /**
     * Get workflowId
     *
     * @return workflowId String.
     */
    public String getWorkflowId() {
        return workflowId;
    }
}
