package org.cucina.security.testassist;

import org.cucina.security.model.Entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "foo")
@XmlRootElement(name = "foo")
public class Foo
		extends Entity {
	private Collection<Bar> bars;
	private Collection<Baz> bazs;
	private Date date;
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
		setId(BigInteger.valueOf(id));
	}

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
	public Collection<Baz> getBazs() {
		return bazs;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param bazs JAVADOC.
	 */
	public void setBazs(Collection<Baz> bazs) {
		this.bazs = bazs;
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
