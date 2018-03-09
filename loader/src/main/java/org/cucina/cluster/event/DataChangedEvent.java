package org.cucina.cluster.event;

import org.springframework.context.ApplicationEvent;

import java.util.Set;


/**
 * Event that indicates a change in persisted data, event will contain
 * a <code>Set</code> of the domain types that have changed in the payload.
 */
public class DataChangedEvent
		extends ApplicationEvent {
	/**
	 * DataChangedEvent.modifiedTypes.
	 */
	public static final String MODIFIED_TYPES = "DataChangedEvent.modifiedTypes";
	private static final long serialVersionUID = 6143074655366056607L;

	/**
	 * Create a new event with the list of changed domain types.
	 *
	 * @param domainTypes
	 */
	public DataChangedEvent(Set<String> domainTypes) {
		super(domainTypes);
	}
}
