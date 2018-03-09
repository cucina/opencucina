package org.cucina.search.query.modifier;

import org.cucina.search.query.SearchBean;

import java.util.Map;


/**
 * Provide ability to modify SearchBean
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface CriteriaModifier {
	/**
	 * modify Searchbean with context params
	 *
	 * @param searchBean SearchBean.
	 * @param params     Map<String, Object>.
	 * @return SearchBean.
	 */
	SearchBean modify(SearchBean searchBean, Map<String, Object> params);
}
