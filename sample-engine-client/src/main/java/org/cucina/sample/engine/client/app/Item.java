package org.cucina.sample.engine.client.app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * @author vlevine
 */
@Entity
public class Item {
	private Long id;
	private String name;
	private String status;

	/**
	 * @return .
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	/**
	 * @param id .
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return .
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name .
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return .
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status .
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
