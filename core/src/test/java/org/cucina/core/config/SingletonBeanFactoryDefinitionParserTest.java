package org.cucina.core.config;

import org.cucina.core.spring.SingletonBeanFactory;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SingletonBeanFactoryDefinitionParserTest {
	private SingletonBeanFactoryDefinitionParser parser = new SingletonBeanFactoryDefinitionParser();

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testDoParseElementBeanDefinitionBuilder() {
		BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

		when(builder.setFactoryMethod("getInstance")).thenReturn(builder);
		parser.doParse(mock(Element.class), builder);
		verify(builder).setFactoryMethod("getInstance");
		;
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetBeanClassElement() {
		assertEquals(SingletonBeanFactory.class, parser.getBeanClass(null));
	}
}
