package org.cucina.core.config;

import org.cucina.core.service.ThreadLocalContextService;
import org.junit.Test;
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
public class ContextServiceDefinitionParserTest {
	private ContextServiceDefinitionParser parser = new ContextServiceDefinitionParser();

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetBeanClassElement() {
		assertEquals(ThreadLocalContextService.class, parser.getBeanClass(null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testResolveIdElementAbstractBeanDefinitionParserContext() {
		ParserContext parserContext = new ParserContext(null, null);
		Element element = mock(Element.class);

		assertEquals("contextService", parser.resolveId(element, null, parserContext));
		when(element.getAttribute("id")).thenReturn("dog");
		assertEquals("dog", parser.resolveId(element, null, parserContext));
	}
}
