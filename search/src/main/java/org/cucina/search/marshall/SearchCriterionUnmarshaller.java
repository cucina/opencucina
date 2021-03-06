package org.cucina.search.marshall;

import org.cucina.search.query.SearchCriterion;

import java.util.Map;


/**
 * Unmarshaller for SearchCriterion
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface SearchCriterionUnmarshaller {
	/**
	 * Unmarshall the SearchCriterion
	 *
	 * @param propertyName String.
	 * @param alias        String.
	 * @param rootType     String.
	 * @param rootAlias    String.
	 * @param criteria     Map<String, Object>.
	 * @return JAVADOC.
	 */
	SearchCriterion unmarshall(String propertyName, String alias, String rootType,
							   String rootAlias, Map<String, Object> criteria);
}
