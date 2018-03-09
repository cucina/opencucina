package org.cucina.search.marshall;

import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.BooleanSearchCriterion;
import org.springframework.util.Assert;

import java.util.Map;


/**
 * Marshalls BooleanSearchCriterion
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class BooleanSearchCriterionMarshaller
		implements SearchCriterionMarshaller {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param alias               JAVADOC.
	 * @param propertyName        JAVADOC.
	 * @param criterion           JAVADOC.
	 * @param marshalledCriterion JAVADOC.
	 */
	@Override
	public void marshall(String alias, String propertyName, SearchCriterion criterion,
						 Map<String, Object> marshalledCriterion) {
		Assert.notNull(alias, "must supply an alias");
		Assert.notNull(criterion, "must supply an criterion");
		Assert.notNull(marshalledCriterion, "must supply marshalledCriterion");
		marshalledCriterion.put(alias, ((BooleanSearchCriterion) criterion).getValue());
	}

	/**
	 * Supports marshalling of objects of class BooleanSearchCriterion
	 *
	 * @param clazz Class<?>.
	 * @return true if is of type BooleanSearchCriterion.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(BooleanSearchCriterion.class);
	}
}
