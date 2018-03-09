package org.cucina.search.query.criterion;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;


/**
 * Date search criterion, handles between A and B, direct match, >= from, <= to,
 * depending upon whether from and to are set and whether they are the same value
 * (i.e. direct match)
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DateRelativeSearchCriterion
		extends AbstractDateSearchCriterion {

	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
	private String from;
	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
	private String to;

	/**
	 * Creates a new NumberSearchCriterion object.
	 *
	 * @param property  Property name to search on.
	 * @param name      Name for storing the criterion
	 * @param rootAlias JAVADOC.
	 * @param from      JAVADOC.
	 * @param to        JAVADOC.
	 */
	public DateRelativeSearchCriterion(String property, String name, String rootAlias, String from, String to) {
		super(property, name, rootAlias);
		Assert.isTrue(StringUtils.isNotBlank(from) || StringUtils.isNotBlank(to), "either from or to have to be provided");
		this.from = from;
		this.to = to;
		parseValues();
	}

	/**
	 * Constructor is used by JSON for constructing new projections.
	 */
	@SuppressWarnings("unused")
	private DateRelativeSearchCriterion() {
		super();
	}

	/**
	 * Return from
	 *
	 * @return from Date
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Return to
	 *
	 * @return to Number
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Makes use of the <code>DateRelativeHelper</code> to parse the dates provided.
	 *
	 * @return JAVADOC.
	 */
	@Override
	public List<Object> getValues() {
		List<Object> values = new ArrayList<Object>();
		values.addAll(parseValues());
		return values;
	}

	/**
	 * Parses the Relative syntax into Date instances. Also ensures that from is before to.
	 *
	 * @return
	 */
	private List<Date> parseValues() {
		Date fromDate = null;
		Date toDate = null;
		DateRelativeHelper helper = DateRelativeHelper.getInstance();

		if (StringUtils.isNotBlank(getFrom())) {
			String fitFrom = getFrom().trim().toLowerCase();
			fromDate = helper.parseDate(fitFrom, true);
		}

		if (StringUtils.isNotBlank(getTo())) {
			String fitTo = getTo().trim().toLowerCase();

			toDate = helper.parseDate(fitTo, false);
		}

		LinkedHashSet<Date> values = new LinkedHashSet<Date>();

		if (fromDate != null) {
			values.add(fromDate);
		}

		if (toDate != null) {
			values.add(toDate);
		}

		if ((fromDate != null) && (toDate != null)) {
			Assert.isTrue(fromDate.compareTo(toDate) <= 0,
					"Cannot create a criterion where from is greater than to");
		}

		return new ArrayList<Date>(values);
	}
}
