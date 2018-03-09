package org.cucina.engine.server.event;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ListActionableTransitionsEvent
		extends GetValueEvent {
	private static final long serialVersionUID = -2588190857865779918L;
	private String workflowId;

	/**
	 * Creates a new StartWorkflowEvent object.
	 *
	 * @param source JAVADOC.
	 */
	public ListActionableTransitionsEvent(Object source, String applicationName) {
		super(source, applicationName);
		this.workflowId = (String) source;
	}

	/**
	 * Creates a new ListActionableTransitionsEvent object.
	 */
	public ListActionableTransitionsEvent() {
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
	 * @param workflowId JAVADOC.
	 */
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
}
