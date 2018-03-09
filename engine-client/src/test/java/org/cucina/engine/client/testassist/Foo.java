package org.cucina.engine.client.testassist;

import java.util.Date;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class Foo {
	private Date date;
	private Long id;
	private String name;
	private int value;

	/**
	 * Creates a new Foo object.
	 */
	public Foo() {
		super();
	}

	/**
	 * Creates a new Foo object.
	 *
	 * @param id JAVADOC.
	 */
	public Foo(Long id) {
		super();
		setId(id);
	}

	/**
	 * @return
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return .
	 */
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
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String getName() {
		return name;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param name JAVADOC.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param value JAVADOC.
	 */
	public void setValue(int value) {
		this.value = value;
	}
}
