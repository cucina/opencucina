package org.cucina.search.query;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;


/**
 * Left Join, works for jpa (hibernate and openJpa).
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class LeftJoin
		implements Join {
	private String property;
	private boolean unique;

	/**
	 * Creates a new LeftJoin object.
	 *
	 * @param property String.
	 * @param unique   boolean.
	 */
	public LeftJoin(String property, boolean unique) {
		super();
		Assert.notNull(property, "property param cannot be null");
		this.property = property;
		this.unique = unique;
	}

	/**
	 * Get join "left join "
	 *
	 * @return String.
	 */
	@Override
	public String getJoin() {
		return "left join ";
	}

	/**
	 * Get property
	 *
	 * @return property String.
	 */
	@Override
	public String getProperty() {
		return property;
	}

	/**
	 * If this join should not be shared with others unless specifically set
	 *
	 * @return unique boolean.
	 */
	@Override
	public boolean isUnique() {
		return unique;
	}

	/**
	 * Implement toString
	 *
	 * @return string representation.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
