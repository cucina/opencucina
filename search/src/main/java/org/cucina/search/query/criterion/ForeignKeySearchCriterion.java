package org.cucina.search.query.criterion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import org.springframework.util.Assert;


/**
 * SearchCriterion implementation for foreign key values, restricts by using
 * the id of the foreign key.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ForeignKeySearchCriterion
    extends AbstractSearchCriterion {
    private Collection<Long> value;
    private LongTransformer transformer = new LongTransformer();

    /**
    * Creates a new TextSearchCriterion object.
    *
    * @param property The property name to restrict
    * @param name Optional name to store this criterion under
    * @param tableAlias JAVADOC.
    * @param text JAVADOC.
    */
    @SuppressWarnings("unchecked")
    public ForeignKeySearchCriterion(String property, String name, String rootAlias, Object value) {
        super(property, name, rootAlias);
        Assert.notNull(value, "value must be provided as a parameter");

        if (value instanceof Collection) {
            this.value = CollectionUtils.collect((Collection<?>) value, transformer,
                    new HashSet<Long>());
        } else {
            this.value = Collections.singleton(transformer.transform(value));
        }
    }

    /**
     * Constructor is used by JSON for constructing new projections.
     */
    @SuppressWarnings("unused")
    private ForeignKeySearchCriterion() {
        super();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String getRestriction() {
        StringBuilder restriction = new StringBuilder();

        for (int i = 0; i < value.size(); i++) {
            if (i > 0) {
                restriction.append(",");
            }

            restriction.append("?");
        }

        if (restriction.length() == 1) {
            restriction.insert(0, ".id" + getEqualsOperator());
        } else {
            restriction.insert(0, ".id" + getInOperator() + "(");
            restriction.append(")");
        }

        return getSearchPropertyName() + restriction.toString();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Collection<Long> getValue() {
        return value;
    }

    /**
    * Return text value
    *
    * @return text.
    */
    @Override
    public List<Object> getValues() {
        return new ArrayList<Object>(value);
    }

    private static class LongTransformer
        implements Transformer {
        @Override
        public Long transform(Object value) {
            if (null == value) {
                return null;
            }

            if (value instanceof String) {
                return Long.valueOf((String) value);
            } else if (value instanceof Number) {
                return ((Number) value).longValue();
            } else {
                throw new IllegalArgumentException("Invalid value of type " +
                    value.getClass().getName() + ", only String, Long, Collection are supported");
            }
        }
    }
}
