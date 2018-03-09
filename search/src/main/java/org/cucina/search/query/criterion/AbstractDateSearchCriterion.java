package org.cucina.search.query.criterion;

public abstract class AbstractDateSearchCriterion extends
		AbstractSearchCriterion {

	/**
	 * Constructor is used by JSON for constructing new projections.
	 */
	public AbstractDateSearchCriterion() {
		super();
	}

	/**
	 * Creates a new instance.
	 *
	 * @param name
	 * @param alias
	 * @param rootAlias
	 */
	public AbstractDateSearchCriterion(String name, String alias,
									   String rootAlias) {
		super(name, alias, rootAlias);
	}

	abstract Object getFrom();

	abstract Object getTo();

	/**
	 * Get restriction depending upon which values are set
	 *
	 * @return restriction String.
	 */
	@Override
	public String getRestriction() {
		if ((getFrom() != null) && (getTo() != null)) {
			if (getFrom().equals(getTo())) {
				return getSearchPropertyName() + getEqualsOperator() + "? ";
			}

			return getSearchPropertyName() + (isBooleanNot() ? " not" : "") + " between ? and ? ";
		} else if (getFrom() != null) {
			return getSearchPropertyName() + (isBooleanNot() ? " < ? " : " >= ? ");
		}

		return getSearchPropertyName() + (isBooleanNot() ? " > ? " : " <= ? ");
	}

}