package org.cucina.search;

import org.cucina.search.query.SearchBean;

import java.util.Map;


/**
 * Generates projections for
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface ProjectionPopulator {
	String PROJECTIONS = "projections";

	/**
	 * Populates SearchBean with projections for type.
	 *
	 * @param type   JAVADOC.
	 * @param bean   SearchBean.
	 * @param params Map<String, Object>
	 */
	void populate(String type, SearchBean bean, Map<String, Object> params,
				  Class<?>... projectionGroup);
}
