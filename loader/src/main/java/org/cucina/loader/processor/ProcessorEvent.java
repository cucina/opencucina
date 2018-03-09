package org.cucina.loader.processor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.context.ApplicationEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ProcessorEvent
		extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	private String executorName;

	/**
	 * Creates a new InputDataEvent object.
	 *
	 * @param source JAVADOC.
	 */
	public ProcessorEvent(Object source) {
		super(source);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String getExecutorName() {
		return executorName;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param executorName JAVADOC.
	 */
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param obj JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof ProcessorEvent)) {
			return false;
		}

		ProcessorEvent rhs = (ProcessorEvent) obj;

		return new EqualsBuilder().append(this.source, rhs.source).isEquals();
	}
}
