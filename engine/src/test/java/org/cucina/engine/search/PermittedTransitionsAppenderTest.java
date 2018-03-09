package org.cucina.engine.search;

import org.cucina.engine.definition.Token;
import org.cucina.engine.service.TransitionsAccessor;
import org.cucina.engine.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PermittedTransitionsAppenderTest {
	private static final String TRANSITIONS = "aTransition";
	private static final String TRANSITIONS2 = "anotherTransition";
	private PermittedTransitionsAppender pta;
	@Mock
	private TransitionsAccessor transitionsAccessor;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void onSetup() {
		MockitoAnnotations.initMocks(this);
		pta = new PermittedTransitionsAppender(transitionsAccessor);
	}

	/**
	 * Test that a property called transitionIds is added to the row.
	 */
	@Test
	public void testDoModify() {
		Map<String, Object> row1 = new HashMap<String, Object>();

		row1.put("name", "aName");
		row1.put(Token.WFD_ID_PROPERTY_NAME, "workflow");
		row1.put(Token.PLACE_ID_PROPERTY_NAME, "place");
		row1.put("legalEntity", 1L);

		Map<String, Object> row2 = new HashMap<String, Object>();

		row2.put("name", "aName");
		row2.put(Token.WFD_ID_PROPERTY_NAME, "workflow");
		row2.put(Token.PLACE_ID_PROPERTY_NAME, "place");
		row1.put("legalEntity", 2L);

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> rows = Arrays.asList(row1, row2);

		when(transitionsAccessor.listPermittedTransitions(
				(String) row1.get(Token.WFD_ID_PROPERTY_NAME),
				(String) row1.get(Token.PLACE_ID_PROPERTY_NAME), Foo.TYPE, row1))
				.thenReturn(Collections.singletonList(TRANSITIONS));

		when(transitionsAccessor.listPermittedTransitions(
				(String) row2.get(Token.WFD_ID_PROPERTY_NAME),
				(String) row2.get(Token.PLACE_ID_PROPERTY_NAME), Foo.TYPE, row2))
				.thenReturn(Collections.singletonList(TRANSITIONS2));

		pta.doModify(Foo.TYPE, rows);
		assertEquals(Arrays.asList(TRANSITIONS),
				row1.get(PermittedTransitionsAppender.PROPERTY_NAME));
		assertEquals(Arrays.asList(TRANSITIONS2),
				row2.get(PermittedTransitionsAppender.PROPERTY_NAME));
		//listPermittedTransitions should only be called once because we should be
		//using the cache to improve performance.
		verify(transitionsAccessor)
				.listPermittedTransitions((String) row1.get(Token.WFD_ID_PROPERTY_NAME),
						(String) row1.get(Token.PLACE_ID_PROPERTY_NAME), Foo.TYPE, row1);

		verify(transitionsAccessor)
				.listPermittedTransitions((String) row1.get(Token.WFD_ID_PROPERTY_NAME),
						(String) row1.get(Token.PLACE_ID_PROPERTY_NAME), Foo.TYPE, row2);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testListProperties() {
		assertEquals(Arrays.asList(PermittedTransitionsAppender.PROPERTY_NAME),
				pta.listProperties(Foo.TYPE));
	}
}
