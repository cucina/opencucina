package org.cucina.engine.definition.config.xml;

import org.cucina.engine.testadapters.ProcessEnvironmentFactory;
import org.cucina.engine.testassist.Foo;
import org.junit.Test;
import org.springframework.expression.BeanResolver;
import org.xml.sax.Attributes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
public class ResolverObjectCreationFactoryTest {
	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testCreate()
			throws Exception {
		BeanResolver res1 = mock(BeanResolver.class);

		Foo f1 = new Foo(1L);
		Foo f2 = new Foo(2L);

		when(res1.resolve(null, "bean:/aa/bb")).thenReturn(f1);
		when(res1.resolve(null, "class:/cc/dd")).thenReturn(f2);

		ProcessEnvironmentFactory.buildEnvironment(res1, null, null, null, null);

		ResolverObjectCreationFactory factory = new ResolverObjectCreationFactory();
		Attributes attributes = mock(Attributes.class);

		when(attributes.getValue("path")).thenReturn("bean:/aa/bb");
		when(attributes.getValue("id")).thenReturn("1");

		Foo foo = (Foo) factory.createObject(attributes);

		assertEquals(f1, foo);
		when(attributes.getValue("path")).thenReturn("class:/cc/dd");
		when(attributes.getValue("id")).thenReturn("2");
		foo = (Foo) factory.createObject(attributes);
		assertEquals(f2, foo);
	}
}
