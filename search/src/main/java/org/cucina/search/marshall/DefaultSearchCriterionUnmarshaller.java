
package org.cucina.search.marshall;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.ForeignKeySearchCriterion;
import org.cucina.search.query.criterion.InSearchCriterion;
import org.cucina.search.query.criterion.TextSearchCriterion;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DefaultSearchCriterionUnmarshaller
    extends NotSearchCriterionUnmarshaller {
    private InstanceFactory instanceFactory;

    /**
         * Creates a new DefaultSearchCriterionMarshaller object.
         *
         * @param instanceFactory JAVADOC.
         */
    public DefaultSearchCriterionUnmarshaller(InstanceFactory instanceFactory) {
        super();
        Assert.notNull(instanceFactory, "instanceFactory cannot be null");
        this.instanceFactory = instanceFactory;
    }

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
    @SuppressWarnings("unchecked")
    @Override
    protected SearchCriterion doUnmarshall(String propertyName, String alias, String rootType,
        String rootAlias, Map<String, Object> criteria) {
        if (instanceFactory.isForeignKey(rootType, propertyName)) {
            Object restriction = criteria.get(alias);

            if (restriction != null) {
                if (!(restriction instanceof String && StringUtils.isBlank((String) restriction)) &&
                        !(restriction instanceof Collection &&
                        CollectionUtils.isEmpty((Collection<?>) restriction))) {
                    return new ForeignKeySearchCriterion(propertyName, alias, rootAlias, restriction);
                }
            }
        } else {
            Object restriction = criteria.get(alias);

            if (restriction instanceof Collection) {
                if (CollectionUtils.isNotEmpty((Collection<String>) restriction)) {
                    return new InSearchCriterion(propertyName, alias, rootAlias,
                        (Collection<String>) restriction);
                }
            } else if (StringUtils.isNotBlank((String) restriction)) {
                return new TextSearchCriterion(propertyName, alias, rootAlias, (String) restriction);
            }
        }

        return null;
    }
}
