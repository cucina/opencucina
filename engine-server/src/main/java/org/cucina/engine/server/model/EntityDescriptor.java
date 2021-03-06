package org.cucina.engine.server.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.core.model.PersistableEntity;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;


/**
 * @author vlevine
 */
@Cacheable
@Entity
public class EntityDescriptor
		extends PersistableEntity {
	/**
	 * This is a field JAVADOC
	 */
	public static final String APPLICATION_TYPE_PROP = "applicationType";
	private static final long serialVersionUID = -2567806169877757973L;
	private String remoteId;
	private String applicationName;
	private String applicationType;

	/**
	 * Creates a new EntityDescriptor object.
	 */
	public EntityDescriptor() {
	}

	/**
	 * Creates a new EntityDescriptor object.
	 *
	 * @param applicationType JAVADOC.
	 * @param refid           JAVADOC.
	 */
	public EntityDescriptor(String applicationType, String id, String applicationName) {
		this.applicationType = applicationType;
		this.remoteId = id;
		this.applicationName = applicationName;
	}

	/**
	 * Creates a new EntityDescriptor object.
	 *
	 * @param entity JAVADOC.
	 */
	public EntityDescriptor(PersistableEntity entity) {
		this.applicationType = entity.getApplicationType();
		this.remoteId = entity.getId().toString();
	}

	/**
	 * Get applicationName
	 *
	 * @return applicationName String.
	 */
	@Basic(optional = false)
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * Set applicationName
	 *
	 * @param applicationName String.
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Basic(optional = false)
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
	public String getRemoteId() {
		return remoteId;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param localId JAVADOC.
	 */
	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
