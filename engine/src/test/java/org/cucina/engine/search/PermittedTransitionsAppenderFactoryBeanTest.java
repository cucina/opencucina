package org.cucina.engine.search;

import org.cucina.engine.service.TransitionsAccessor;
import org.cucina.search.ResultSetModifier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PermittedTransitionsAppenderFactoryBeanTest {
	@Mock
	private TransitionsAccessor transitionsAccessor;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void test()
			throws Exception {
		PermittedTransitionsAppenderFactoryBean bean = new PermittedTransitionsAppenderFactoryBean(false,
				transitionsAccessor);

		assertTrue(bean.isSingleton());
		assertTrue(bean.getObjectType().isAssignableFrom(ResultSetModifier.class));

		assertTrue(bean.getObject() instanceof NoObjectCheckPermittedTransitionsAppender);

		bean = new PermittedTransitionsAppenderFactoryBean(true, transitionsAccessor);
		assertTrue(bean.isSingleton());
		assertTrue(bean.getObjectType().isAssignableFrom(ResultSetModifier.class));

		assertTrue(bean.getObject() instanceof PermittedTransitionsAppender);
	}
}
