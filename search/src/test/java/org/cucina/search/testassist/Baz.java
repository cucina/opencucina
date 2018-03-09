package org.cucina.search.testassist;

import org.cucina.core.model.PersistableEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Entity(name = Baz.TYPE)
public class Baz
		extends PersistableEntity {
	/**
	 * This is a field JAVADOC
	 */
	public static final String TYPE = "Baz";
	private static final long serialVersionUID = -3645162443543733629L;
	private Collection<Bar> bars;
	private Foo foo;
	private String name;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "baz")
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
	@ManyToOne
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
