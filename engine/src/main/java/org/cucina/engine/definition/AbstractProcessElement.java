package org.cucina.engine.definition;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Common properties holder
 *
 * @author vlevine
 */
public abstract class AbstractProcessElement
		implements ProcessElement {
	/**
	 * Stores the {@link ProcessDefinition} for this <code>ProcessElement</code>
	 * .
	 */
	private ProcessDefinition processDefinition;

	/**
	 * Stores the description for this <code>ProcessElement</code>.
	 */
	private String description;

	/**
	 * Stores the ID for this <code>ProcessElement</code>.
	 */
	private String id;

	/**
	 * Gets the description for this <code>ProcessElement</code>.
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description for this <code>ProcessElement</code>.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the ID for this <code>ProcessElement</code>.
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * Sets the ID for this <code>ProcessElement</code>.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the {@link ProcessDefinition} that contains this
	 * <code>ProcessElement</code>.
	 */
	@Override
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	/**
	 * Sets the {@link ProcessDefinition} that contains this
	 * <code>ProcessElement</code>.
	 */
	@Override
	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	/**
	 * @return String
	 */
	public String toLongString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * @return JAVADOC.
	 */
	public String toString() {
		return new ToStringBuilder(this).append("Id", getId()).toString();
	}
}
