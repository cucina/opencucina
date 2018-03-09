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
public class NoObjectCheckPermittedTransitionsAppenderTest {
	private static final String TRANSITIONS = "aTransition";
	private NoObjectCheckPermittedTransitionsAppender pta;
	@Mock
	private TransitionsAccessor transitionsAccessor;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void onSetup() {
		MockitoAnnotations.initMocks(this);
		pta = new NoObjectCheckPermittedTransitionsAppender(transitionsAccessor);
	}

	/**
	 * Test that a property called transitionIds is added to the row.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testDoModify() {
		Map<String, Object> row1 = new HashMap<String, Object>();

		row1.put("name", "aName");
		row1.put(Token.WFD_ID_PROPERTY_NAME, "workflow");
		row1.put(Token.PLACE_ID_PROPERTY_NAME, "place");

		Map<String, Object> row2 = new HashMap<String, Object>();

		row2.put("name", "aName");
		row2.put(Token.WFD_ID_PROPERTY_NAME, "workflow");
		row2.put(Token.PLACE_ID_PROPERTY_NAME, "place");

		List<Map<String, Object>> rows = Arrays.asList(row1, row2);

		when(transitionsAccessor.listPermittedTransitionsNoObjectCheck(
				(String) row1.get(Token.WFD_ID_PROPERTY_NAME),
				(String) row1.get(Token.PLACE_ID_PROPERTY_NAME)))
				.thenReturn(Collections.singletonList(TRANSITIONS));

		pta.doModify(Foo.TYPE, rows);
		assertEquals(Arrays.asList(TRANSITIONS),
				row1.get(PermittedTransitionsAppender.PROPERTY_NAME));
		//listPermittedTransitions should only be called once because we should be
		//using the cache to improve performance.
		verify(transitionsAccessor)
				.listPermittedTransitionsNoObjectCheck((String) row1.get(Token.WFD_ID_PROPERTY_NAME),
						(String) row1.get(Token.PLACE_ID_PROPERTY_NAME));
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
