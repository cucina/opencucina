
package org.cucina.search.query.projection;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.search.query.AbstractSearchComponent;
import org.cucina.search.query.SearchCriterion;


/**
 * Provides generic functionality for projections
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class AbstractProjection
    extends AbstractSearchComponent
    implements Projection {
    private Collection<SearchCriterion> searchCriteria;

    /**
    * Creates a new AbstractProjection object.
    */
    public AbstractProjection() {
        super();
    }

    /**
     * Creates a new AbstractProjection object.
     *
     * @param name fully qualified field name, e.g. 'bazs.bars.name'.
     * @param alias projection alias.
     * @param rootAlias alias of root.
     */
    public AbstractProjection(String name, String alias, String rootAlias) {        
        super(name, alias, rootAlias);
    }

    /**
     * Is this aggregate function, by default not
     *
     * @return false.
     */
    @Override
    public boolean isAggregate() {
        return false;
    }

    /**
     * By default the projections should be in a group by
     */
    @Override
    public boolean isGroupable() {
        return true;
    }

    /**
     * Set parentAliases and update any related criterion if required
     *
     * @param parentAliases JAVADOC.
     */
    public void setParentAliases(Map<String, String> parentAliases) {
        super.setParentAliases(parentAliases);

        if (CollectionUtils.isNotEmpty(searchCriteria)) {
            for (SearchCriterion criterion : searchCriteria) {
                criterion.setParentAliases(parentAliases);
            }
        }
    }

    /**
    * JAVADOC Method Level Comments
    *
    * @param searchCriterion JAVADOC.
    */
    public void setSearchCriteria(Collection<SearchCriterion> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    /**
    * Default toString implementation
    *
    * @return This object as String.
    */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
