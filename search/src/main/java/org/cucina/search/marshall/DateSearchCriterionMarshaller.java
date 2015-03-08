
package org.cucina.search.marshall;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.DateSearchCriterion;
import org.springframework.util.Assert;


/**
 * Marshalls DateSearchCriterion
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DateSearchCriterionMarshaller
    extends NotSearchCriterionMarshaller {
    /**
     * Supports DateSearchCriterion
     *
     * @param clazz DateSearchCriterion.
     *
     * @return boolean according to support.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(DateSearchCriterion.class);
    }

    /**
     * Marshalls the DateSearchCriterion into from and to properties
     *
     * @param alias String.
     * @param criterion SearchCriterion.
     * @param marshalledCriterion Map<String, Object>.
     */
    @Override
    protected Object doMarshall(String alias, String propertyName, SearchCriterion criterion,
        Map<String, Object> marshalledCriterion) {
        Assert.notNull(alias, "must supply an alias");
        Assert.notNull(criterion, "must supply an criterion");
        Assert.notNull(marshalledCriterion, "must supply marshalledCriterion");

        Date from = ((DateSearchCriterion) criterion).getFrom();
        Map<String, Long> criteria = new HashMap<String, Long>();

        if (from != null) {
            criteria.put(SearchCriterionMarshaller.FROM_PROPERTY, from.getTime() / 1000);
        }

        Date to = ((DateSearchCriterion) criterion).getTo();

        if (to != null) {
            criteria.put(SearchCriterionMarshaller.TO_PROPERTY, to.getTime() / 1000);
        }

        if (!criteria.isEmpty()) {
            return criteria;
        }

        return null;
    }
}
