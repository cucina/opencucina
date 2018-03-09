package org.cucina.search.query.criterion;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;


/**
 * SearchCriterion implementation for boolean values
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class BooleanSearchCriterion
		extends AbstractSearchCriterion {
	private boolean value;

	/**
	 * Creates a new TextSearchCriterion object.
	 *
	 * @param name       JAVADOC.
	 * @param alias      Optioinal Name to register this criterion under
	 * @param tableAlias JAVADOC.
	 * @param text       JAVADOC.
	 */
	public BooleanSearchCriterion(String name, String alias, String rootAlias, boolean value) {
		super(name, alias, rootAlias);
		Assert.notNull(value, "value must be provided as a parameter");
		this.value = value;
	}

	/**
	 * Constructor is used by JSON for constructing new projections.
	 */
	@SuppressWarnings("unused")
	private BooleanSearchCriterion() {
		super();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String getRestriction() {
		return getSearchPropertyName() + " = ?";
	}

	/**
	 * Get value
	 *
	 * @return value
	 */
	public boolean getValue() {
		return value;
	}

	/**
	 * Return text value
	 *
	 * @return text.
	 */
	@Override
	public List<Object> getValues() {
		return Collections.singletonList((Object) Boolean.valueOf(value));
	}
}
