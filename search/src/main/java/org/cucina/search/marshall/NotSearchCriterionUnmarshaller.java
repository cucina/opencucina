
package org.cucina.search.marshall;

import java.util.Map;

import org.cucina.core.utils.NameUtils;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.AbstractSearchCriterion;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class NotSearchCriterionUnmarshaller
    implements SearchCriterionUnmarshaller {
    /**
     * Delegates to the implementations <code>{@link #doUnmarshall(String, String, String,
     * String, Map)}</code> and  adds booleanNot to the returned criterion if applicable.
     */
    @Override
    public final SearchCriterion unmarshall(String propertyName, String alias, String rootType,
        String rootAlias, Map<String, Object> criteria) {
        SearchCriterion criterion = doUnmarshall(propertyName, alias, rootType, rootAlias, criteria);

        if (criterion instanceof AbstractSearchCriterion) {
            Object marshalledNot = criteria.get(NameUtils.concat(alias,
                        SearchCriterionMarshaller.NOT_PROPERTY));
            boolean not = false;

            if (marshalledNot instanceof String) {
                not = Boolean.valueOf((String) marshalledNot);
            } else if (marshalledNot instanceof Boolean) {
                not = (Boolean) marshalledNot;
            }

            ((AbstractSearchCriterion) criterion).setBooleanNot(not);
        }

        return criterion;
    }

    /**
     * Main worker method to unmarshall to the required <code>SearchCriterion</code>
     *
     * @param propertyName
     * @param alias
     * @param rootType
     * @param rootAlias
     * @param criteria
     *
     * @return SearchCriterion null if no restrictions found.
     */
    protected abstract SearchCriterion doUnmarshall(String propertyName, String alias,
        String rootType, String rootAlias, Map<String, Object> criteria);
}
