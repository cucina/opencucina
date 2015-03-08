
package org.cucina.search.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.search.SearchType;
import org.cucina.search.query.projection.Projection;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * SearchBean which contains criteria, projections and details of the search,
 * i.e. searchType and alias
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchBean
    implements Cloneable {
    private Collection<SearchCriterion> criteria = new ArrayList<SearchCriterion>();
    private LinkedHashMap<String, String> aliasByType;
    private List<OrderBy> order;
    private List<Projection> projections = new ArrayList<Projection>();
    private SearchType searchType = SearchType.DEFAULT;

    /**
     * Set aliasByType, first mapping should be for primary search type
     *
     * @param aliasByType
     *            LinkedHashMap<String, String>.
     */
    public void setAliasByType(LinkedHashMap<String, String> aliasByType) {
        this.aliasByType = aliasByType;
    }

    /**
     * Get aliasByType
     *
     * @return aliasBySearchType LinkedHashMap<String, String>.
     */
    public LinkedHashMap<String, String> getAliasByType() {
        return aliasByType;
    }

    /**
     * Set criteria
     *
     * @param criteria
     *            Collection<SearchCriterion>.
     */
    public void setCriteria(Collection<SearchCriterion> criteria) {
        this.criteria = criteria;
    }

    /**
     * Get projections
     *
     * @return criteria Collection<SearchCriterion>.
     */
    public Collection<SearchCriterion> getCriteria() {
        return criteria;
    }

    /**
     * Gets criteria by the root alias
     *
     * @param alias
     *            String root alias of the search type
     *
     * @return Collection<Projection>
     */
    @JsonIgnore
    public Collection<SearchCriterion> getCriteria(String alias) {
        Assert.notNull(alias, "alias cannot be null");

        Collection<SearchCriterion> typeSpecificCrits = new ArrayList<SearchCriterion>();

        for (SearchCriterion criterion : criteria) {
            if (alias.equals(criterion.getRootAlias())) {
                typeSpecificCrits.add(criterion);
            }
        }

        return typeSpecificCrits;
    }

    /**
     * Set order
     *
     * @param order
     *            List<OrderBy>
     */
    public void setOrder(List<OrderBy> order) {
        this.order = order;
    }

    /**
     * Get order
     *
     * @return List<OrderBy>
     */
    public List<OrderBy> getOrder() {
        return order;
    }

    /**
     * Return projection with name {@code name}
     *
     * @param name
     *            String of projection to get
     *
     * @return Projection of name {@code name}
     */
    @JsonIgnore
    public Projection getProjection(String name) {
        if (CollectionUtils.isNotEmpty(projections)) {
            for (Projection projection : projections) {
                if (projection.getName().equals(name)) {
                    return projection;
                }
            }
        }

        return null;
    }

    /**
     * Set projections
     *
     * @param projections
     *            Collection<Projection>.
     */
    public void setProjections(List<Projection> projections) {
        this.projections = projections;
    }

    /**
     * Get projections
     *
     * @return Collection<Projection>.
     */
    public List<Projection> getProjections() {
        return projections;
    }

    /**
     * Gets projections by the root alias
     *
     * @param alias
     *            String root alias of the search type
     *
     * @return Collection<Projection>
     */
    @JsonIgnore
    public Collection<Projection> getProjections(String alias) {
        Assert.notNull(alias, "alias cannot be null");

        Collection<Projection> typeSpecificProjs = new ArrayList<Projection>();

        for (Projection projection : projections) {
            if (alias.equals(projection.getRootAlias())) {
                typeSpecificProjs.add(projection);
            }
        }

        return typeSpecificProjs;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param searchType
     *            JAVADOC.
     */
    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public SearchType getSearchType() {
        return searchType;
    }

    /**
     * Add a criterion
     *
     * @param criterion
     *            SearchCriterion.
     */
    public void addCriterion(SearchCriterion criterion) {
        criteria.add(criterion);
    }

    /**
     * Add an <code>OrderBy</code>
     *
     * @param orderBy
     */
    public void addOrder(OrderBy orderBy) {
        Assert.notNull(orderBy, "orderBy is null!");

        if (order == null) {
            order = new ArrayList<OrderBy>();
        }

        order.add(orderBy);
    }

    /**
     * Add a projection
     *
     * @param projection
     *            JAVADOC.
     */
    public void addProjection(Projection projection) {
        projections.add(projection);
    }

    /**
     * The number of aggregate projections
     *
     * @return number of aggregate projections
     */
    public int aggregateProjectionCount() {
        int count = 0;

        if (CollectionUtils.isNotEmpty(projections)) {
            for (Projection projection : projections) {
                if (projection.isAggregate()) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     *
     * @throws CloneNotSupportedException
     *             JAVADOC.
     */
    @Override
    public Object clone() {
        SearchBean searchBean = new SearchBean();

        // Create new lists of values so as to prevent any possible
        // contamination
        if (criteria != null) {
            searchBean.setCriteria(new ArrayList<SearchCriterion>(criteria));
        }

        // order
        if (order != null) {
            searchBean.setOrder(new ArrayList<OrderBy>(order));
        }

        // projections
        if (projections != null) {
            searchBean.setProjections(new ArrayList<Projection>(projections));
        }

        // aliasByType
        if (aliasByType != null) {
            searchBean.setAliasByType(new LinkedHashMap<String, String>(aliasByType));
        }

        // searchtype
        if (searchType != null) {
            searchBean.setSearchType(searchType);
        }

        return searchBean;
    }
}
