package org.cucina.search.marshall;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cucina.core.utils.NameUtils;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.InSearchCriterion;
import org.cucina.search.query.criterion.NumberSearchCriterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;


/**
 * abstract base class providing functinality for unmarshalling numeric values
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class NumberSearchCriterionUnmarshaller
		extends NotSearchCriterionUnmarshaller {
	private static final Logger LOG = LoggerFactory.getLogger(NumberSearchCriterionUnmarshaller.class);

	/**
	 * Ensure that the correct implementation is returned.
	 *
	 * @param number
	 * @return
	 */
	protected abstract Number parseNumber(Number number);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param number JAVADOC.
	 * @return JAVADOC.
	 */
	protected abstract Number parseNumber(String number);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param propertyName JAVADOC.
	 * @param alias        JAVADOC.
	 * @param rootAlias    JAVADOC.
	 * @param criteria     JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	protected SearchCriterion doUnmarshall(String propertyName, String alias, String rootType,
										   String rootAlias, Map<String, Object> criteria) {
		if (criteria.get(alias) instanceof Collection) {
			Collection<?> potentialValues = (Collection<?>) criteria.get(alias);
			Collection<Number> restrictValues = new HashSet<Number>();

			for (Object object : potentialValues) {
				Number parsed = parse(object);

				if (parsed == null) {
					LOG.warn("Skipping " + object +
							" because it could not be parsed into a number");

					continue;
				}

				restrictValues.add(parsed);
			}

			if (CollectionUtils.isNotEmpty(restrictValues)) {
				return new InSearchCriterion(propertyName, alias, rootAlias, restrictValues);
			}
		} else {
			Number from = parse(criteria.get(NameUtils.concat(alias,
					SearchCriterionMarshaller.FROM_PROPERTY)));
			Number to = parse(criteria.get(NameUtils.concat(alias,
					SearchCriterionMarshaller.TO_PROPERTY)));

			if ((from == null) && (to == null)) {
				from = to = parse(criteria.get(alias));
			}

			if ((from != null) || (to != null)) {
				return new NumberSearchCriterion(propertyName, alias, rootAlias, from, to);
			}
		}

		return null;
	}

	/**
	 * Parse the provided <code>String</code> into a <code>Number</code>
	 *
	 * @param value
	 * @return
	 */
	private Number parse(Object value) {
		Number number = null;

		if (value instanceof Number) {
			number = parseNumber((Number) value);
		} else if (value instanceof String) {
			String sValue = (String) value;

			if (StringUtils.isNotBlank(sValue)) {
				try {
					return parseNumber(sValue);
				} catch (NumberFormatException e) {
					LOG.error(e.getLocalizedMessage());
				}
			}
		}

		return number;
	}
}
