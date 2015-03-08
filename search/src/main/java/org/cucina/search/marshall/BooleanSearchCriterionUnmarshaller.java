
package org.cucina.search.marshall;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.BooleanSearchCriterion;


/**
 * Boolean unmarshaller
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class BooleanSearchCriterionUnmarshaller
    implements SearchCriterionUnmarshaller {
    /**
     * JAVADOC Method Level Comments
     *
     * @param propertyName JAVADOC.
     * @param alias JAVADOC.
     * @param rootType JAVADOC.
     * @param rootAlias JAVADOC.
     * @param criteria JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public SearchCriterion unmarshall(String propertyName, String alias, String rootType,
        String rootAlias, Map<String, Object> criteria) {
        Object restriction = criteria.get(alias);

        if (restriction != null && StringUtils.isNotEmpty(restriction.toString())) {
            return new BooleanSearchCriterion(propertyName, alias, rootAlias,
                Boolean.parseBoolean(restriction.toString()));
        }

        return null;
    }
}
