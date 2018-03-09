package org.cucina.search.query.modifier;

import org.cucina.search.query.SearchBean;

import java.util.Map;


/**
 * Abstract implementation for chained modifiers. Allows you to create a modifier chain.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class AbstractCriteriaModifier
		implements CriteriaModifier {
	private CriteriaModifier chainedModifier;

	/**
	 * Set chainedModifier
	 *
	 * @param chainedModifier CriteriaModifier.
	 */
	public void setChainedModifier(CriteriaModifier chainedModifier) {
		this.chainedModifier = chainedModifier;
	}

	/**
	 * If there's a chained modifier then modify it first
	 *
	 * @param searchBean JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public SearchBean modify(SearchBean searchBean, Map<String, Object> params) {
		if (chainedModifier != null) {
			chainedModifier.modify(searchBean, params);
		}

		return doModify(searchBean, params);
	}

	/**
	 * All subclasses need to provide implementation to modify searchBean.
	 *
	 * @param searchBean SearchBean.
	 * @return SearchBean.
	 */
	protected abstract SearchBean doModify(SearchBean searchBean, Map<String, Object> params);
}
