package org.cucina.search.query;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.util.Assert;


/**
 * Generates the order by hql for the provided propertyName and direction.
 */
public class OrderBy {
	private String propertyName;
	private boolean asc;

	/**
	 * Creates a new OrderBy object.
	 *
	 * @param propertyName JAVADOC.
	 * @param asc          JAVADOC.
	 */
	public OrderBy(String propertyName, boolean asc) {
		Assert.hasText(propertyName, "propertyName is required!");
		this.propertyName = propertyName;
		this.asc = asc;
	}

	/**
	 * Generate the required hql to perform the order by.
	 */
	public String getRestriction() {
		StringBuilder hql = new StringBuilder(propertyName);

		if (isAsc()) {
			hql.append(" asc");
		} else {
			hql.append(" desc");
		}

		return hql.toString();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param obj JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof OrderBy)) {
			return false;
		}

		OrderBy orderBy = (OrderBy) obj;

		return new EqualsBuilder().append(this.propertyName, orderBy.getPropertyName()).isEquals();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(3, 23).append(this.propertyName).hashCode();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	protected boolean isAsc() {
		return asc;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	protected String getPropertyName() {
		return propertyName;
	}
}
