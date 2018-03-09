package org.cucina.search.query.projection;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MaxProjection
		extends AbstractProjection {
	/**
	 * Creates a new MaxProjection object.
	 *
	 * @param name      JAVADOC.
	 * @param alias     JAVADOC.
	 * @param rootAlias JAVADOC.
	 */
	public MaxProjection(String name, String alias, String rootAlias) {
		super(name, alias, rootAlias);
	}

	@SuppressWarnings("unused")
	private MaxProjection() {
		super();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public boolean isAggregate() {
		return true;
	}

	/**
	 * Aggregate should not be part of group by
	 */
	@Override
	public boolean isGroupable() {
		return false;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String getProjection() {
		return "max(" + getSearchPropertyName() + ") as " + getAlias();
	}
}
