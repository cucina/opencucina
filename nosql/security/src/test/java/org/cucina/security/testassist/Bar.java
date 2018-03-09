package org.cucina.security.testassist;

import org.cucina.security.model.Entity;

import java.math.BigInteger;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class Bar
		extends Entity {
	private Baz baz;
	private Foo foo;
	private String barDescription;
	private String name;

	/**
	 * Creates a new Bar object.
	 */
	public Bar() {
		super();
	}

	/**
	 * Creates a new Bar object.
	 *
	 * @param id JAVADOC.
	 */
	public Bar(Long id) {
		super();
		setId(BigInteger.valueOf(id));
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String getBarDescription() {
		return barDescription;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param barDescription JAVADOC.
	 */
	public void setBarDescription(String barDescription) {
		this.barDescription = barDescription;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public Baz getBaz() {
		return baz;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param baz JAVADOC.
	 */
	public void setBaz(Baz baz) {
		this.baz = baz;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public Foo getFoo() {
		return foo;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param foo JAVADOC.
	 */
	public void setFoo(Foo foo) {
		this.foo = foo;
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
}
