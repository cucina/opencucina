
package org.cucina.search.marshall;

import java.util.Map;

import org.cucina.search.query.SearchCriterion;


/**
 * Marshalls a SearchCriterion instance
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface SearchCriterionMarshaller {
    /** from. */
    String FROM_PROPERTY = "from";

    /** history */
    String HISTORY_ALIAS = "history";

    /** not. */
    String NOT_PROPERTY = "not";
    String RESTRICTION_PROPERTY = "restriction";

    /** to. */
    String TO_PROPERTY = "to";

    /**
     * Marshall this critieron into marshalledCriterion
     *
     * @param alias String.
     * @param criterion SearchCriterion.
     * @param marshalledCriterion Map containing marshalled values.
     */
    void marshall(String alias, String propertyName, SearchCriterion criterion,
        Map<String, Object> marshalledCriterion);

    /**
     * Whether Marshaller supports this clazz type
     *
     * @param clazz Class<?>.
     *
     * @return true or false if supported or otherwise.
     */
    boolean supports(Class<?> clazz);
}
