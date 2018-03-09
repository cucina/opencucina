package org.cucina.search.query.criterion;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * SearchCriterion implementation searching by multiple values, restricts by using an 'in'
 * statement to restrict by the provided values.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class InSearchCriterion
		extends AbstractSearchCriterion {
	Collection<?> value;

	/**
	 * Creates a new TextSearchCriterion object.
	 *
	 * @param property   Property name that this restriction is applied to.
	 * @param name       Optional Criterion name.
	 * @param tableAlias JAVADOC.
	 * @param text       JAVADOC.
	 */
	public InSearchCriterion(String property, String name, String rootAlias, Collection<?> value) {
		super(property, name, rootAlias);
		Assert.notNull(value, "value must be provided as a parameter");
		this.value = value;
	}

	/**
	 * Constructor is used by JSON for constructing new projections.
	 */
	@SuppressWarnings("unused")
	private InSearchCriterion() {
		super();
	}

	/**
	 * Builds the required restriction with optimization for single value by using '=' in
	 * place of 'in'.
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String getRestriction() {
		StringBuilder restriction = new StringBuilder();

		for (int i = 0; i < getValues().size(); i++) {
			if (i > 0) {
				restriction.append(",");
			}

			restriction.append("?");
		}

		if (restriction.length() == 1) {
			restriction.insert(0, getEqualsOperator());
		} else {
			restriction.insert(0, getInOperator() + "(");
			restriction.append(")");
		}

		return getSearchPropertyName() + restriction.toString();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public Collection<?> getValue() {
		return value;
	}

	/**
	 * Return text value
	 *
	 * @return text.
	 */
	@Override
	public List<Object> getValues() {
		return (value != null) ? new ArrayList<Object>(value) : Collections.emptyList();
	}
}
