package org.cucina.search.query;

import org.cucina.search.SearchDao;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


/**
 * Test that SpriteQuerySearchResults functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchQuerySearchResultsTest {
	/**
	 * Test finds all results.
	 */
	@Test
	public void findResults() {
		SearchQuery query = new SearchQuery("fdasfd", new ArrayList<Object>());
		List<Object> results = new ArrayList<Object>();

		SearchDao searchDao = mock(SearchDao.class);

		when(searchDao.find(query)).thenReturn(results);

		SearchResults searchResults = new SearchQuerySearchResults(query, searchDao);

		assertEquals("Incorrect results", results, searchResults.search());
		verify(searchDao).find(query);
	}

	/**
	 * Finds results with defined start and max size.
	 */
	@Test
	public void findSpecificResults() {
		SearchQuery query = new SearchQuery("fdasfd", new ArrayList<Object>());
		List<Object> results = new ArrayList<Object>();

		SearchDao searchDao = mock(SearchDao.class);

		when(searchDao.find(query, 1, 5)).thenReturn(results);

		SearchResults searchResults = new SearchQuerySearchResults(query, searchDao);

		assertEquals("Incorrect results", results, searchResults.search(1, 5));
		verify(searchDao).find(query, 1, 5);
	}

	/**
	 * Query parameter is required.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void queryRequired() {
		new SearchQuerySearchResults(null, mock(SearchDao.class));
	}

	/**
	 * SearchDao is required.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void searchDaoRequired() {
		SearchQuery query = new SearchQuery("fdasfd", new ArrayList<Object>());

		new SearchQuerySearchResults(query, null);
	}
}
