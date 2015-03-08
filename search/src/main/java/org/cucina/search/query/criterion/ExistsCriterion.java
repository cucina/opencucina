
package org.cucina.search.query.criterion;

import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;


/**
 * Simple exists criterion which generates a query where exists (<code>exists</code>).
 *
 * @author thornton
 *
 */
public class ExistsCriterion
    extends AbstractSearchCriterion {
    private String exists;

    /**
     * Creates a new ExistsCriterion object.
     *
     * @param rootAlias JAVADOC.
     * @param exists JAVADOC.
     */
    public ExistsCriterion(String rootAlias, String exists) {
        super(rootAlias);
        Assert.hasText(exists, "exists must be provided as a parameter");
        this.exists = exists;
    }

    @SuppressWarnings("unused")
    private ExistsCriterion() {
        super();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getExists() {
        return exists;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String getRestriction() {
        return (isBooleanNot() ? " not" : "") + " exists (" + exists + ")";
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public List<Object> getValues() {
        return Collections.emptyList();
    }
}
