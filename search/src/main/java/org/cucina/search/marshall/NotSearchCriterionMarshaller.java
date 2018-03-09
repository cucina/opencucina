package org.cucina.search.marshall;

import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.AbstractSearchCriterion;

import java.util.HashMap;
import java.util.Map;


/**
 * Implementation of <code>SearchCriterionMarshaller</code> which adds 'not'
 * search information to the marshalled criteria.
 */
public abstract class NotSearchCriterionMarshaller
		implements SearchCriterionMarshaller {
	/**
	 * Delegates to
	 * <code>{@link #doMarshall(String, String, SearchCriterion, Map)}</code>
	 * and adds the 'not' search information to the marshalled criteria for the
	 * property. If criteria exists for this <code>SearchCriterion</code> the
	 * <code>#marshalledCriterion</code> will contain a <code>Map</code> of
	 * criteria keyed by the <code>#alias</code>
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public final void marshall(String alias, String propertyName, SearchCriterion criterion,
							   Map<String, Object> marshalledCriterion) {
		Object marshalled = doMarshall(alias, propertyName, criterion, marshalledCriterion);

		if (marshalled != null) {
			Map<String, Object> marshalledMap = new HashMap<String, Object>();

			if (marshalled instanceof Map) {
				marshalledMap.putAll((Map<String, Object>) marshalled);
			} else {
				marshalledMap.put(SearchCriterionMarshaller.RESTRICTION_PROPERTY, marshalled);
			}

			if (criterion instanceof AbstractSearchCriterion &&
					((AbstractSearchCriterion) criterion).isBooleanNot()) {
				marshalledMap.put(SearchCriterionMarshaller.NOT_PROPERTY, true);
			}

			marshalledCriterion.put(alias, marshalledMap);
		}
	}

	/**
	 * Marshalls the provided </code>SearchCriterion</code> for the property.
	 * Implementations are expected to place marshalled values into the
	 * marshalledCriterion <code>Map</code> keyed by alias.
	 * <code>SearchCriterion</code> with multiple properties should produce a
	 * <code>Map</code> containing the properties.
	 *
	 * @param alias
	 * @param propertyName
	 * @param criterion
	 * @param marshalledCriterion
	 */
	protected abstract Object doMarshall(String alias, String propertyName,
										 SearchCriterion criterion, Map<String, Object> marshalledCriterion);
}
