package org.cucina.search.marshall;

import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.NumberSearchCriterion;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;


/**
 * Marshalls NumberSearchCriterion
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class NumberSearchCriterionMarshaller
		extends NotSearchCriterionMarshaller {
	/**
	 * Supports NumberSearchCriterion
	 *
	 * @param clazz NumberSearchCriterion.
	 * @return that is supports clazz.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return NumberSearchCriterion.class.isAssignableFrom(clazz);
	}

	/**
	 * Sets from and to properties in marshalledCriterion
	 *
	 * @param alias               String.
	 * @param criterion           SearchCriterion.
	 * @param marshalledCriterion Map<String, Object>.
	 */
	@Override
	protected Object doMarshall(String alias, String propertyName, SearchCriterion criterion,
								Map<String, Object> marshalledCriterion) {
		Assert.notNull(alias, "must supply an alias");
		Assert.notNull(criterion, "must supply an criterion");
		Assert.notNull(marshalledCriterion, "must supply marshalledCriterion");

		Number from = ((NumberSearchCriterion) criterion).getFrom();
		Map<String, Number> criteria = new HashMap<String, Number>();

		if (from != null) {
			criteria.put(SearchCriterionMarshaller.FROM_PROPERTY, from);
		}

		Number to = ((NumberSearchCriterion) criterion).getTo();

		if (to != null) {
			criteria.put(SearchCriterionMarshaller.TO_PROPERTY, to);
		}

		if (!criteria.isEmpty()) {
			return criteria;
		}

		return null;
	}
}
