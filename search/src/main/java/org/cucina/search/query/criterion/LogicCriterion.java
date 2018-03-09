package org.cucina.search.query.criterion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.cucina.search.query.SearchCriterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class LogicCriterion
		extends AbstractSearchCriterion {
	private static final Logger LOG = LoggerFactory.getLogger(LogicCriterion.class);
	private List<SearchCriterion> criteria;

	/**
	 * Creates a new LogicCriterion object.
	 *
	 * @param rootAlias JAVADOC.
	 * @param criteria  JAVADOC.
	 */
	public LogicCriterion(String rootAlias, List<SearchCriterion> criteria) {
		super(rootAlias);
		Assert.notNull(criteria, "criteria must be provided as a parameter");
		this.criteria = criteria;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public abstract String getLogicOperator();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public List<SearchCriterion> getCriteria() {
		return criteria;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	@JsonIgnore
	public String getRestriction() {
		StringBuffer buf = new StringBuffer();

		Iterator<SearchCriterion> iterator = criteria.iterator();

		if (iterator.hasNext()) {
			buf.append("(");
		}

		for (; iterator.hasNext(); ) {
			SearchCriterion crit = iterator.next();

			String restrictionValue = crit.getRestriction();

			if (LOG.isDebugEnabled()) {
				LOG.debug("Adding restriction [" + restrictionValue + "]");
			}

			buf.append(restrictionValue);

			if (iterator.hasNext()) {
				buf.append(" ");
				buf.append(getLogicOperator());
				buf.append(" ");
			}
		}

		if (buf.length() > 0) {
			buf.append(")");
		}

		return buf.toString();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	@JsonIgnore
	public List<Object> getValues() {
		List<Object> ret = new ArrayList<Object>();

		for (Iterator<SearchCriterion> iterator = criteria.iterator(); iterator.hasNext(); ) {
			SearchCriterion crit = iterator.next();

			ret.addAll(crit.getValues());
		}

		return ret;
	}
}
