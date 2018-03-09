package org.cucina.search.query.criterion;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
public class DateSearchCriterion
		extends AbstractDateSearchCriterion {
	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
	private Date from;
	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
	private Date to;

	/**
	 * Creates a new NumberSearchCriterion object.
	 *
	 * @param property  The property name to apply the restriction.
	 * @param name      Optional criterion name, when not provided the property will be used.
	 * @param rootAlias JAVADOC.
	 * @param from      JAVADOC.
	 * @param to        JAVADOC.
	 */
	public DateSearchCriterion(String property, String name, String rootAlias, Date from, Date to) {
		super(property, name, rootAlias);
		Assert.isTrue((from != null) || (to != null), "either from or to have to be provided");

		if ((from != null) && (to != null)) {
			Assert.isTrue(from.compareTo(to) <= 0,
					"Cannot create a criterion where from is greater than to");
		}

		this.from = from;
		this.to = to;
	}

	/**
	 * Constructor is used by JSON for constructing new projections.
	 */
	@SuppressWarnings("unused")
	private DateSearchCriterion() {
		super();
	}

	/**
	 * Return from
	 *
	 * @return from Date
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * Return to
	 *
	 * @return to Number
	 */
	public Date getTo() {
		return to;
	}

	/**
	 * Get value, contains from and to, depending upon which are set.
	 *
	 * @return JAVADOC.
	 */
	@Override
	public List<Object> getValues() {
		LinkedHashSet<Object> values = new LinkedHashSet<Object>();

		if (from != null) {
			values.add(from);
		}

		if (to != null) {
			values.add(to);
		}

		return new ArrayList<Object>(values);
	}
}
