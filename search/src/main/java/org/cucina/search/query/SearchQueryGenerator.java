
package org.cucina.search.query;


/**
 * Generates SpriteQuery which can be provided to the
 * SearchDao to run against persistence layer.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface SearchQueryGenerator {
    /**
     * Generate query.
     *
     * @param searchBean SearchBean.
     *
     * @return SpriteQuery.
     */
    SearchQuery generateQuery(SearchBean searchBean);
}
