package org.cucina.search;

import org.cucina.search.query.SearchBean;
import org.cucina.search.query.SearchResults;

import java.util.Map;


/**
 * Service which provides search functionality. Will modify and generate appropriate
 * db query as well as running it.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface SearchService {
	/**
	 * Search with no limits
	 *
	 * @param searchBean SearchBean.
	 * @param params     Map<String, Object>.
	 * @return SearchResults results.
	 */
	SearchResults search(SearchBean searchBean, Map<String, Object> params);
}
