package org.cucina.search;

import org.cucina.search.query.*;
import org.cucina.search.query.modifier.CriteriaModifier;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchServiceImplTest {
	/**
	 * Test that any modifiers are run against the clone so as not to polute the
	 * original <code>SearchBean</code>
	 */
	@Test
	public void allSearch() {
		SearchBean bean = mock(SearchBean.class);

		SearchBean clone = new SearchBean();

		when(bean.clone()).thenReturn(clone);

		Map<String, Object> params = new HashMap<String, Object>();
		SearchQuery query = new SearchQuery("dffdas", new ArrayList<Object>());
		List<Object> results = new ArrayList<Object>();
		CriteriaModifier criteriaModifier = mock(CriteriaModifier.class);

		when(criteriaModifier.modify(clone, params)).thenReturn(clone);

		SearchQueryGenerator generator = mock(SearchQueryGenerator.class);

		when(generator.generateQuery(clone)).thenReturn(query);

		SearchQueryGeneratorFactory searchQueryGeneratorFactory = mock(SearchQueryGeneratorFactory.class);

		when(searchQueryGeneratorFactory.getSearchQueryGenerator(clone)).thenReturn(generator);

		SearchDao dao = mock(SearchDao.class);

		when(dao.find(query)).thenReturn(results);

		SearchServiceImpl searchService = new SearchServiceImpl(criteriaModifier, dao,
				searchQueryGeneratorFactory);
		SearchResults searchResults = searchService.search(bean, params);

		assertNotNull("Should have returned searchResults", searchResults);
		assertEquals("Should have same result", results, searchResults.search());
	}

	/**
	 * Test that any changes to the bean are done on the clone so as not to polute the original
	 * <code>SearchBean</code>
	 */
	@Test
	public void cutDownSearch() {
		SearchBean bean = mock(SearchBean.class);
		SearchBean clone = new SearchBean();

		when(bean.clone()).thenReturn(clone);

		Map<String, Object> params = new HashMap<String, Object>();
		SearchQuery query = new SearchQuery("dffdas", new ArrayList<Object>());
		List<Object> results = new ArrayList<Object>();
		CriteriaModifier criteriaModifier = mock(CriteriaModifier.class);

		when(criteriaModifier.modify(clone, params)).thenReturn(clone);

		SearchQueryGenerator generator = mock(SearchQueryGenerator.class);

		when(generator.generateQuery(clone)).thenReturn(query);

		SearchQueryGeneratorFactory searchQueryGeneratorFactory = mock(SearchQueryGeneratorFactory.class);

		when(searchQueryGeneratorFactory.getSearchQueryGenerator(clone)).thenReturn(generator);

		SearchDao dao = mock(SearchDao.class);

		when(dao.find(query, 1, 10)).thenReturn(results);

		SearchServiceImpl searchService = new SearchServiceImpl(criteriaModifier, dao,
				searchQueryGeneratorFactory);
		SearchResults searchResults = searchService.search(bean, params);

		assertNotNull("Should have returned searchResults", searchResults);
		assertEquals("Should have same result", results, searchResults.search(1, 10));
	}
}
