package org.cucina.search.marshall;

import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.InSearchCriterion;
import org.springframework.util.Assert;

import java.util.Map;


/**
 * Marshalls InSearchCriterion
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class InSearchCriterionMarshaller
		extends NotSearchCriterionMarshaller {
	/**
	 * Supports InSearchCriterion
	 *
	 * @param clazz Class<?>.
	 * @return if supports class or not.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(InSearchCriterion.class);
	}

	/**
	 * marshall criterion
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

		return ((InSearchCriterion) criterion).getValues();
	}
}
