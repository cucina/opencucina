package org.cucina.engine.event;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.ApplicationEvent;

import java.util.Collection;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PluralTransitionEvent
		extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	private Collection<Long> ids;
	private Map<String, Object> parameters;
	private String applicationType;
	private String workflowId;

	/**
	 * Creates a new TransitionEvent object.
	 *
	 * @param parameters      JAVADOC.
	 * @param transitionId    JAVADOC.
	 * @param workflowId      JAVADOC.
	 * @param applicationType JAVADOC.
	 * @param ids             JAVADOC.
	 */
	public PluralTransitionEvent(String transitionId, String workflowId, String applicationType,
								 Collection<Long> ids, Map<String, Object> parameters) {
		super(transitionId);
		this.parameters = parameters;
		this.workflowId = workflowId;
		this.applicationType = applicationType;
		this.ids = ids;
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
	public Collection<Long> getIds() {
		return ids;
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
	 * Equals impl making use of EqualsBuilder.
	 *
	 * @param obj Object.
	 * @return if equal or not.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof PluralTransitionEvent)) {
			return false;
		}

		PluralTransitionEvent rhs = (PluralTransitionEvent) obj;

		return new EqualsBuilder().append(this.applicationType, rhs.applicationType)
				.append(this.workflowId, rhs.workflowId)
				.append(this.parameters, rhs.parameters).append(this.ids, rhs.ids)
				.isEquals();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(applicationType).append(workflowId).append(parameters)
				.append(ids).toHashCode();
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
