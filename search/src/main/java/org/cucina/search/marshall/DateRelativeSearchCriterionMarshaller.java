package org.cucina.search.marshall;

import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.DateRelativeSearchCriterion;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;


/**
 * Marshalls DateRelativeSearchCriterion
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DateRelativeSearchCriterionMarshaller
		extends NotSearchCriterionMarshaller {
	/**
	 * Supports DateRelativeSearchCriterion
	 *
	 * @param clazz DateRelativeSearchCriterion.
	 * @return boolean according to support.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(DateRelativeSearchCriterion.class);
	}

	/**
	 * Marshalls the DateRelativeSearchCriterion into from and to properties
	 *
	 * @param alias               String.
	 * @param criterion           SearchCriterion.
	 * @param marshalledCriterion Map<String, Object>.
	 */
	@Override
	protected Map<String, String> doMarshall(String alias, String propertyName,
											 SearchCriterion criterion, Map<String, Object> marshalledCriterion) {
		Assert.notNull(alias, "must supply an alias");
		Assert.notNull(criterion, "must supply an criterion");
		Assert.notNull(marshalledCriterion, "must supply marshalledCriterion");

		String from = ((DateRelativeSearchCriterion) criterion).getFrom();
		Map<String, String> criteria = new HashMap<String, String>();

		if (from != null) {
			criteria.put(SearchCriterionMarshaller.FROM_PROPERTY, from);
		}

		String to = ((DateRelativeSearchCriterion) criterion).getTo();

		if (to != null) {
			criteria.put(SearchCriterionMarshaller.TO_PROPERTY, to);
		}

		if (!criteria.isEmpty()) {
			return criteria;
		}

		return null;
	}
}
