package org.cucina.security.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Transient;

import java.math.BigInteger;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dimension extends Entity {
	private BigInteger domainObjectId;
	private Object domainObject;
	private String domainObjectType;
	private String propertyName;

	/**
	 * The setting of domainObject is for convenience only, and should be ignored by underlying
	 * ORM.
	 *
	 * @return domainObject PersistableObject.
	 */
	@Transient
	public Object getDomainObject() {
		return domainObject;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param domainObject JAVADOC.
	 */
	public void setDomainObject(Object domainObject) {
		this.domainObject = domainObject;
	}

	/**
	 * JAVADOC.
	 *
	 * @param expiryDate JAVADOC.
	 */
	public BigInteger getDomainObjectId() {
		return this.domainObjectId;
	}

	/**
	 * JAVADOC.
	 *
	 * @param expiryDate JAVADOC.
	 */
	public void setDomainObjectId(BigInteger domainObjectId) {
		this.domainObjectId = domainObjectId;
	}

	/**
	 * JAVADOC.
	 *
	 * @return JAVADOC.
	 */
	public String getDomainObjectType() {
		return domainObjectType;
	}

	/**
	 * JAVADOC.
	 *
	 * @param domainObjectType JAVADOC.
	 */
	public void setDomainObjectType(String domainObjectType) {
		this.domainObjectType = domainObjectType;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param propertyName JAVADOC.
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}
