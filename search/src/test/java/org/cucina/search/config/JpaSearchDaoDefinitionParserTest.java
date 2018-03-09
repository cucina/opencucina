package org.cucina.search.config;

import org.cucina.search.jpa.JpaSearchDao;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class JpaSearchDaoDefinitionParserTest {
	private JpaSearchDaoDefinitionParser parser = new JpaSearchDaoDefinitionParser();

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testDoParseElementBeanDefinitionBuilder() {
		Element element = mock(Element.class);

		when(element.getAttribute("useCache")).thenReturn("true");

		BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

		when(builder.addPropertyReference("sessionFactory", "sf")).thenReturn(builder);
		when(builder.addPropertyValue("useCache", "true")).thenReturn(builder);
		parser.doParse(element, builder);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetBeanClassElement() {
		assertEquals(JpaSearchDao.class, parser.getBeanClass(null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testResolveId() {
		ParserContext parserContext = new ParserContext(null, null);
		Element element = mock(Element.class);

		assertEquals("searchDao", parser.resolveId(element, null, parserContext));
		when(element.getAttribute("id")).thenReturn("dog");
		assertEquals("dog", parser.resolveId(element, null, parserContext));
	}
}
