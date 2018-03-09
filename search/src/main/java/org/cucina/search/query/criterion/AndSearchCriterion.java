package org.cucina.search.query.criterion;

import org.cucina.search.query.SearchCriterion;

import java.util.List;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class AndSearchCriterion
		extends LogicCriterion {
	/**
	 * Creates a new AndSearchCriterion object.
	 *
	 * @param rootAlias JAVADOC.
	 * @param criteria  JAVADOC.
	 */
	public AndSearchCriterion(String rootAlias, List<SearchCriterion> criteria) {
		super(rootAlias, criteria);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String getLogicOperator() {
		return "and";
	}
}
