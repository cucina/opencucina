package org.cucina.engine.server.event;

import java.util.Map;

public class StartWorkflowEvent
		extends ProcessEvent {
	private static final long serialVersionUID = -2588190857865779918L;
	private Map<String, Object> parameters;
	private String workflow;

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
	 * @return JAVADOC.
	 */
	public Map<String, Object> getParameters() {
		return parameters;
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
	 * @return .
	 */
	public String getWorkflow() {
		return workflow;
	}

	/**
	 * @param workflow .
	 */
	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}
}
