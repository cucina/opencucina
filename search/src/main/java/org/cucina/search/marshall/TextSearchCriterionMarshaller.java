
package org.cucina.search.marshall;

import java.util.Map;

import org.cucina.search.query.SearchCriterion;
import org.cucina.search.query.criterion.TextSearchCriterion;
import org.springframework.util.Assert;


/**
 * Marshalls TextSearchCriterion
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TextSearchCriterionMarshaller
    extends NotSearchCriterionMarshaller {
    /**
     * Supports marshalling of objects of class TextSearchCriterion
     *
     * @param clazz Class<?>.
     *
     * @return true if is of type TextSearchCriterion.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(TextSearchCriterion.class);
    }

    /**
     * Marshall value of TextSearchCriterion
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

        return ((TextSearchCriterion) criterion).getValue();
    }
}
