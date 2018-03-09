package org.cucina.engine.config;

import org.cucina.engine.operations.TransitionOperation;
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
public class WorkflowTransitionActionDefinitionParserTest {
	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testDoParseElementBeanDefinitionBuilder() {
		TransitionOperationDefinitionParser parser = new TransitionOperationDefinitionParser();
		Element element = mock(Element.class);

		when(element.getAttribute("expressionExecutor")).thenReturn("ee");
		when(element.getAttribute("tokenRepository")).thenReturn("td");
		when(element.getAttribute("workflowEnvironment")).thenReturn("we");

		BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

		when(builder.setScope("prototype")).thenReturn(builder);
		when(builder.addPropertyReference("expressionExecutor", "ee")).thenReturn(builder);
		when(builder.addConstructorArgReference("td")).thenReturn(builder);
		when(builder.addConstructorArgReference("we")).thenReturn(builder);
		parser.doParse(element, builder);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetBeanClassElement() {
		TransitionOperationDefinitionParser parser = new TransitionOperationDefinitionParser();

		assertEquals(TransitionOperation.class, parser.getBeanClass(null));
	}
}
