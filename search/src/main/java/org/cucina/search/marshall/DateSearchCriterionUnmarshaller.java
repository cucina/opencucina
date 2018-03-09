package org.cucina.search.marshall;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.cucina.core.utils.NameUtils;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.DateRelativeSearchCriterion;
import org.cucina.search.query.criterion.DateSearchCriterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


/**
 * Unmarshalls DateSearchCriterion from criteria Map
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DateSearchCriterionUnmarshaller
		extends NotSearchCriterionUnmarshaller {
	private static final Logger LOG = LoggerFactory.getLogger(DateSearchCriterionUnmarshaller.class);

	/**
	 * Unmarshall DateSearchCriterion
	 *
	 * @param propertyName String.
	 * @param alias        String.
	 * @param rootType     String.
	 * @param rootAlias    String.
	 * @param criteria     Map<String, Object>.
	 * @return SearchCriterion populated with values from criteria Map.
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected SearchCriterion doUnmarshall(String propertyName, String alias, String rootType,
										   String rootAlias, Map<String, Object> marshalledCriterion) {
		Object from = marshalledCriterion.get(NameUtils.concat(alias,
				SearchCriterionMarshaller.FROM_PROPERTY));
		Object to = marshalledCriterion.get(NameUtils.concat(alias,
				SearchCriterionMarshaller.TO_PROPERTY));

		if ((from == null) && (to == null)) {
			Map<String, Object> criteria = (Map<String, Object>) marshalledCriterion.get(alias);

			if (MapUtils.isNotEmpty(criteria)) {
				from = criteria.get(SearchCriterionMarshaller.FROM_PROPERTY);
				to = criteria.get(SearchCriterionMarshaller.TO_PROPERTY);
			}
		}

		if ((from != null) || (to != null)) {
			from = convertDate(from);
			to = convertDate(to);

			if (from instanceof Date || to instanceof Date) {
				Assert.isTrue((from == null) || from instanceof Date,
						"Both from and to must be of same type, i.e. Date");
				Assert.isTrue((to == null) || to instanceof Date,
						"Both from and to must be of same type, i.e. Date");

				if (to != null) {
					Calendar cal = Calendar.getInstance();

					cal.setTime((Date) to);
					cal.set(Calendar.HOUR_OF_DAY, 23);
					cal.set(Calendar.MINUTE, 59);
					cal.set(Calendar.SECOND, 59);
					cal.set(Calendar.MILLISECOND, 999);
					to = cal.getTime();
				}

				return new DateSearchCriterion(propertyName, alias, rootAlias, (Date) from, (Date) to);
			} else if (from instanceof String || to instanceof String) {
				Assert.isTrue((from == null) || from instanceof String,
						"Both from and to must be of same type, i.e. relative Date");
				Assert.isTrue((to == null) || to instanceof String,
						"Both from and to must be of same type, i.e. relative Date");

				return new DateRelativeSearchCriterion(propertyName, alias, rootAlias, (String) from,
						(String) to);
			}
		}

		return null;
	}

	/**
	 * Parse the provided <code>String</code> into the required
	 * <code>Date</code>
	 *
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	private Object convertDate(Object value) {
		Object date = null;

		if (value instanceof Date) {
			date = value;
		} else if (value instanceof String && StringUtils.isNotBlank((String) value)) {
			String sValue = (String) value;

			if (StringUtils.isNumeric(sValue)) {
				try {
					date = new Date(Long.valueOf(sValue) * 1000);

					// date = dateFormat.parse(sValue);
				} catch (NumberFormatException e) {
					LOG.error(e.getLocalizedMessage());
				}
			} else {
				date = value;
			}
		}

		return date;
	}
}
