package org.cucina.security.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.math.BigInteger;


/**
 * @author vlevine
 */
public class Entity {
	@Id
	private BigInteger id;
	@Version
	private Integer version;

	/**
	 * Creates a new Entity object.
	 */
	public Entity() {
		super();
	}

	/**
	 * @return .
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 * @param id .
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}

	/**
	 * Get version number
	 *
	 * @return version.
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * Set version
	 *
	 * @param version int.
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
}
