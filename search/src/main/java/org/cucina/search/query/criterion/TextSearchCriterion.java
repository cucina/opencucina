package org.cucina.search.query.criterion;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;


/**
 * SearchCriterion implementation for textual values, restricts by using
 * 'like'
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class TextSearchCriterion
		extends AbstractSearchCriterion {
	private static final String WILDCARD = "%";
	private String value;
	private boolean exact;

	/**
	 * Creates a new TextSearchCriterion object.
	 *
	 * @param property   Property name to restrict on
	 * @param name       Criterion name
	 * @param tableAlias JAVADOC.
	 * @param text       JAVADOC.
	 */
	public TextSearchCriterion(String property, String name, String rootAlias, String value) {
		super(property, name, rootAlias);
		Assert.notNull(value, "value must be provided as a parameter");
		this.value = value;
	}

	/**
	 * Constructor is used by JSON for constructing new projections.
	 */
	@SuppressWarnings("unused")
	private TextSearchCriterion() {
		super();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param exact JAVADOC.
	 */
	public void setExact(boolean exact) {
		this.exact = exact;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String getRestriction() {
		return getSearchPropertyName() + (isBooleanNot() ? " not" : "") + " like ?";
	}

	/**
	 * Get value
	 *
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Return text value
	 *
	 * @return text.
	 */
	@Override
	public List<Object> getValues() {
		if (exact) {
			return Collections.singletonList((Object) value);
		}

		return Collections.singletonList((Object) (WILDCARD + value + WILDCARD));
	}
}
