package org.cucina.security.testassist;

import org.cucina.security.model.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Document
public class Baz
		extends Entity {
	private Collection<Bar> bars;
	private Foo foo;
	@Id
	private Long id;
	private String name;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public Collection<Bar> getBars() {
		return bars;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param bars JAVADOC.
	 */
	public void setBars(Collection<Bar> bars) {
		this.bars = bars;
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
