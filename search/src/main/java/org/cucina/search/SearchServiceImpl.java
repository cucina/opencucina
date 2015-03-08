
package org.cucina.search;

import java.util.Map;

import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchQuery;
import org.cucina.search.query.SearchQueryGenerator;
import org.cucina.search.query.SearchQueryGeneratorFactory;
import org.cucina.search.query.SearchQuerySearchResults;
import org.cucina.search.query.SearchResults;
import org.cucina.search.query.modifier.CriteriaModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * SearchService implementation which modifies, produces and runs searches.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SearchServiceImpl
    implements SearchService {
    private static final Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class);
    private CriteriaModifier criteriaModifier;
    private SearchDao searchDao;
    private SearchQueryGeneratorFactory searchQueryGeneratorFactory;

    /**
     * Creates a new SearchServiceImpl object.
     *
     * @param criteriaModifier CriteriaModifier.
     * @param searchDao SearchDao.
     * @param searchQueryGenerator SearchQueryGenerator.
     */
    public SearchServiceImpl(CriteriaModifier criteriaModifier, SearchDao searchDao,
        SearchQueryGeneratorFactory searchQueryGeneratorFactory) {
        super();
        Assert.notNull(criteriaModifier, "criteriaModifier must be provided");
        Assert.notNull(searchDao, "searchDao must be provided");
        Assert.notNull(searchQueryGeneratorFactory, "searchQueryGeneratorFactory must be provided");
        this.criteriaModifier = criteriaModifier;
        this.searchDao = searchDao;
        this.searchQueryGeneratorFactory = searchQueryGeneratorFactory;
    }

    /**
     * Runs a straight search
     *
     * @param searchBean SearchBean.
     * @param params Map<String, Object>.
     *
     * @return List<T>.
     */
    @Override
    public SearchResults search(SearchBean searchBean, Map<String, Object> params) {
        Assert.notNull(searchBean, "must provide searchBean as arg");
        Assert.notNull(params, "must provide params as arg");

        SearchBean clone = (SearchBean) searchBean.clone();

        clone = criteriaModifier.modify(clone, params);

        SearchQuery query = getSearchQueryGenerator(clone).generateQuery(clone);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Search query:" + query);
        }

        return new SearchQuerySearchResults(query, searchDao);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param searchBean JAVADOC.
     *
     * @return JAVADOC.
     */
    protected SearchQueryGenerator getSearchQueryGenerator(SearchBean searchBean) {
        return searchQueryGeneratorFactory.getSearchQueryGenerator(searchBean);
    }
}
