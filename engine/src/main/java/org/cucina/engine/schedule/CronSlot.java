package org.cucina.engine.schedule;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CronSlot
		implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cronExpression;
	private Map<String, Object> parameters;
	private String name;
	private String transitionId;
	private String workflowId;

	/**
	 * Creates a new CronSlot object.
	 *
	 * @param name           String.
	 * @param workflowId     String.
	 * @param transitionId   String.
	 * @param cronExpression String.
	 */
	public CronSlot(String name, String workflowId, String transitionId,
					String cronExpression) {
		super();
		this.name = name;
		this.workflowId = workflowId;
		this.transitionId = transitionId;
		this.cronExpression = cronExpression;
		Assert.notNull(name, "name cannot be null");
		Assert.notNull(workflowId, "workflowId cannot be null");
		Assert.notNull(transitionId, "transitionId cannot be null");
		Assert.notNull(cronExpression, "cronExpression cannot be null");
	}

	/**
	 * Get cronExpression
	 *
	 * @return cronExpression CronExpression.
	 */
	public String getCronExpression() {
		return cronExpression;
	}

	/**
	 * Get name
	 *
	 * @return name String.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set parameters
	 *
	 * @return parameters Map<String, Object>.
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * Set parameters
	 *
	 * @param parameters Map<String, Object>.
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Get transitionId
	 *
	 * @return transitionId String.
	 */
	public String getTransitionId() {
		return transitionId;
	}

	/**
	 * Get workflowId
	 *
	 * @return workflowId String.
	 */
	public String getWorkflowId() {
		return workflowId;
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
