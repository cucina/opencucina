package org.cucina.search;

import org.cucina.search.query.SearchBean;
import org.cucina.search.testassist.Bar;
import org.cucina.search.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.verify;


/**
 * Tests ProjectionsCriteriaModifier functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SimpleProjectionPopulatorTest {
	private static final Collection<String> PROJECTIONS_LIST = Arrays.asList("bla");
	private static final String BAR_ALIAS = "Bar";
	private static final String HISTORY_ALIAS = "history";
	private SearchBean bean;
	@Mock
	private SearchBeanFactory searchBeanFactory;
	private SimpleProjectionPopulator populator;

	/**
	 * No projectionslist provided so delegates to SearchBeanFactory. Test fixed 11/12/12 by AJ
	 */
	@Test
	public void noProjectionsList() {
		Map<String, Object> params = new HashMap<String, Object>();

		populator.populate(Bar.TYPE, bean, params);
		verify(searchBeanFactory).addProjections(Bar.TYPE, bean);
	}

	/**
	 * Sets up for test.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		populator = new SimpleProjectionPopulator(searchBeanFactory);

		LinkedHashMap<String, String> aliasByType = new LinkedHashMap<String, String>();

		aliasByType.put(Bar.TYPE, BAR_ALIAS);
		aliasByType.put(Foo.class.getSimpleName(), HISTORY_ALIAS);

		bean = new SearchBean();
		bean.setAliasByType(aliasByType);
	}

	/**
	 * Projections are created as expected.
	 */
	@Test
	public void testProjectionsList() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put(ProjectionPopulator.PROJECTIONS, PROJECTIONS_LIST);
		populator.populate(Bar.TYPE, bean, params);
		verify(searchBeanFactory).addProjections(Bar.TYPE, bean, PROJECTIONS_LIST);
	}
}
