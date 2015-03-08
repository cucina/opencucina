
package org.cucina.search.query.criterion;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * Number search criterion, handles between A and B, direct match, >= from, <= to,
 * depending upon whether from and to are set and whether they are the same value
 * (i.e. direct match)
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class NumberSearchCriterion
    extends AbstractSearchCriterion {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    Number from;
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    Number to;

    /**
     * Creates a new NumberSearchCriterion object.
     *
     * @param property Property to restrict by
     * @param name Optional Critierion name
     * @param rootAlias JAVADOC.
     * @param from JAVADOC.
     * @param to JAVADOC.
     */
    public NumberSearchCriterion(String property, String name, String rootAlias, Number from, Number to) {
        super(property, name, rootAlias);
        Assert.isTrue((from != null) || (to != null), "either from or to have to be provided");

        if ((from != null) && (to != null)) {
            Assert.isTrue(from.longValue() <= to.longValue(),
                "Cannot create a criterion where from is greater than to");
        }

        this.from = from;
        this.to = to;
    }

    /**
     * Constructor is used by JSON for constructing new projections.
     */
    @SuppressWarnings("unused")
    private NumberSearchCriterion() {
        super();
    }

    /**
     * Return from
     *
     * @return from Number
     */
    public Number getFrom() {
        return from;
    }

    /**
        * Get restriction depending upon which values are set
        *
        * @return restriction String.
        */
    @Override
    public String getRestriction() {
        if ((from != null) && (to != null)) {
            if (from.equals(to)) {
                return getSearchPropertyName() + getEqualsOperator() + "? ";
            }

            return getSearchPropertyName() + (isBooleanNot() ? " not" : "") + " between ? and ? ";
        } else if (from != null) {
            return getSearchPropertyName() + (isBooleanNot() ? " < ? " : " >= ? ");
        }

        return getSearchPropertyName() + (isBooleanNot() ? " > ? " : " <= ? ");
    }

    /**
     * Return to
     *
     * @return to Number
     */
    public Number getTo() {
        return to;
    }

    /**
     * Get value, contains from and to, depending upon which are set.
     *
     * @return JAVADOC.
     */
    @Override
    public List<Object> getValues() {
        LinkedHashSet<Object> values = new LinkedHashSet<Object>();

        if (from != null) {
            values.add(from);
        }

        if (to != null) {
            values.add(to);
        }

        return new ArrayList<Object>(values);
    }
}
