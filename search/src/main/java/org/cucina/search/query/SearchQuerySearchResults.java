
package org.cucina.search.query;

import java.util.List;
import java.util.Map;

import org.cucina.search.SearchDao;
import org.springframework.util.Assert;


/**
 * SearchResults which takes a SpriteQuery and uses SearchDao
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SearchQuerySearchResults
    implements SearchResults {
    private SearchDao searchDao;
    private SearchQuery query;

    /**
     * Creates a new SpriteQuerySearchResults object.
     *
     * @param query JAVADOC.
     * @param searchDao JAVADOC.
     */
    public SearchQuerySearchResults(SearchQuery query, SearchDao searchDao) {
        super();
        Assert.notNull(query, "query cannot be null");
        Assert.notNull(searchDao, "searchDao cannot be null");
        this.query = query;
        this.searchDao = searchDao;
    }

    /**
     * search all
     *
     * @return List<T> results.
     */
    @Override
    public <T> List<T> search() {
        return searchDao.find(query);
    }

    /**
     * Search with start position and result size
     *
     * @param start int start results.
     * @param max int number of results.
     *
     * @return List<T> results.
     */
    @Override
    public <T> List<T> search(int start, int max) {
        return searchDao.find(query, start, max);
    }

    /**
     * Search with start position and result size that returns map
     *
     * @param start int start results.
     * @param max int number of results.
     *
     * @return List<Map<String,Object>> results.
     */
    @Override
    public <T, V> List<Map<T, V>> searchMap(int start, int max) {
        return searchDao.findMap(query, start, max);
    }
}
