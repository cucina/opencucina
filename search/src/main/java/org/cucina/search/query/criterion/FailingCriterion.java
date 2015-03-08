
package org.cucina.search.query.criterion;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class FailingCriterion
    extends AbstractSearchCriterion {
    /**
     * Creates a new FailingCriterion object.
     *
     * @param rootAlias JAVADOC.
     */
    public FailingCriterion(String rootAlias) {
        super(rootAlias);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    @JsonIgnore
    public String getRestriction() {
        return "'x' = 'y'";
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    @JsonIgnore
    public List<Object> getValues() {
        return Collections.emptyList();
    }
}
