package org.cucina.engine.server.event;

import java.io.Serializable;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ObtainHistorySummaryEvent
		extends GetValueEvent {
	private static final long serialVersionUID = -7479820901125310024L;
	private Serializable id;
	private String applicationType;

	/**
	 * Creates a new StartWorkflowEvent object.
	 *
	 * @param source JAVADOC.
	 */
	public ObtainHistorySummaryEvent(Object source, String applicationName) {
		super(source, applicationName);
	}

	/**
	 * Creates a new ObtainHistorySummaryEvent object.
	 */
	public ObtainHistorySummaryEvent() {
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
	public Serializable getId() {
		return id;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param id JAVADOC.
	 */
	public void setId(Serializable id) {
		this.id = id;
	}
}
