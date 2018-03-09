package org.cucina.engine.config;

import org.cucina.engine.service.BulkWorkflowServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
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
public class BulkWorkflowServiceDefinitionParserTest {
	private BulkWorkflowServiceDefinitionParser parser = new BulkWorkflowServiceDefinitionParser();

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testDoParseElementBeanDefinitionBuilder() {
		Element element = mock(Element.class);

		when(element.getAttribute("workflowEnvironment")).thenReturn("hoho");
		when(element.getAttribute("tokenRepository")).thenReturn("exex");

		BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

		when(builder.addConstructorArgReference("exex")).thenReturn(builder);
		when(builder.addConstructorArgReference("hoho")).thenReturn(builder);

		parser.doParse(element, builder);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetBeanClassElement() {
		assertEquals(BulkWorkflowServiceImpl.class, parser.getBeanClass(null));
	}
}
