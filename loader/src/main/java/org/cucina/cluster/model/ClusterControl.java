package org.cucina.cluster.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.support.BooleanConverter;
import org.cucina.core.model.support.JsonMapConverter;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Entity
@Cacheable
public class ClusterControl
		extends PersistableEntity {
	private static final long serialVersionUID = 7033138796227872025L;
	private Date timestamp;
	private Map<Object, Object> properties;
	private String activeNodeId;
	private String event;
	private boolean complete;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String getActiveNodeId() {
		return activeNodeId;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param activeNodeId JAVADOC.
	 */
	public void setActiveNodeId(String activeNodeId) {
		this.activeNodeId = activeNodeId;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Convert(converter = BooleanConverter.class)
	@Column(columnDefinition = "CHAR(1) not null")
	public boolean isComplete() {
		return complete;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param complete JAVADOC.
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	/**
	 * There could be only one entry for each event, unless complete
	 *
	 * @return JAVADOC.
	 */
	@Column
	public String getEvent() {
		return event;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param event JAVADOC.
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Column(length = 1000)
	@Convert(converter = JsonMapConverter.class)
	public Map<Object, Object> getProperties() {
		return properties;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param properties JAVADOC.
	 */
	public void setProperties(Map<Object, Object> properties) {
		this.properties = properties;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param timestamp JAVADOC.
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("eventName", event)
				.append("activeNodeId", activeNodeId)
				.append("properties", properties)
				.append("complete", complete).toString();
	}
}
