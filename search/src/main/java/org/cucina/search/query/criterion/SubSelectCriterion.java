package org.cucina.search.query.criterion;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;


/**
 * Simple equals subselect criterion which generates a query where the
 * <code>searchPropertyName</code> = <code>subSelect</code>.
 */
public class SubSelectCriterion
		extends AbstractSearchCriterion {
	private String subSelect;

	/**
	 * Creates a new SubSelectCriterion object.
	 *
	 * @param name      JAVADOC.
	 * @param rootAlias JAVADOC.
	 * @param subSelect JAVADOC.
	 */
	public SubSelectCriterion(String name, String rootAlias, String subSelect) {
		super(name, rootAlias);
		Assert.hasText(subSelect, "subSelect must be provided as a parameter");
		this.subSelect = subSelect;
	}

	@SuppressWarnings("unused")
	private SubSelectCriterion() {
		super();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String getRestriction() {
		return getSearchPropertyName() + getEqualsOperator() + "(" + subSelect + ")";
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String getSubSelect() {
		return subSelect;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public List<Object> getValues() {
		return Collections.emptyList();
	}
}
