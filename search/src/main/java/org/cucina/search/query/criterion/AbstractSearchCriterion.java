
package org.cucina.search.query.criterion;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.search.query.AbstractSearchComponent;
import org.cucina.search.query.SearchCriterion;


/**
 * Provides generic functionality for Criterion.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class AbstractSearchCriterion
    extends AbstractSearchComponent
    implements SearchCriterion {
    private boolean booleanNot = false;

    /**
     * Creates a new AbstractSearchCriterion object.
     */
    public AbstractSearchCriterion() {
        super();
    }

    /**
    * Creates a new AbstractCriterion object.
    *
    * @param rootAlias String.
    */
    public AbstractSearchCriterion(String rootAlias) {
        super("default", rootAlias);
    }

    /**
    * Creates a new AbstractCriterion object.
    *
    * @param name String.
    * @param rootAlias
    */
    public AbstractSearchCriterion(String name, String rootAlias) {
        super(name, rootAlias);
    }

    /**
     * Creates a new AbstractCriterion
     *
     * @param name property to search on
     * @param alias name of the criterion
     * @param rootAlias
     */
    public AbstractSearchCriterion(String name, String alias, String rootAlias) {
        super(name, alias, rootAlias);
    }

    /**
     * Mutator.
     *
     * @param not condition.
     */
    public void setBooleanNot(boolean booleanNot) {
        this.booleanNot = booleanNot;
    }

    /**
     * Accessor.
     *
     * @return is it boolean not.
     */
    public boolean isBooleanNot() {
        return booleanNot;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Convenience method which returns the operator.
     * @return equal operator, negated if isBooleanNot
     */
    protected String getEqualsOperator() {
        String operator = " = ";

        if (isBooleanNot()) {
            operator = " != ";
        }

        return operator;
    }

    /**
     * Convenience method which returns the operator.
     * @return in operator, negated if isBooleanNot
     */
    protected String getInOperator() {
        String operator = " in ";

        if (isBooleanNot()) {
            operator = " not in ";
        }

        return operator;
    }
}
