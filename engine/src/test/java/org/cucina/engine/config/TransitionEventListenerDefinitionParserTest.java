
package org.cucina.engine.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cucina.engine.event.TransitionEventListener;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * Test that TransitionEventListenerDefinitionParser functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TransitionEventListenerDefinitionParserTest {
    private TransitionEventListenerDefinitionParser parser = new TransitionEventListenerDefinitionParser();

    /**
     * Test correct elements parsed and added
     */
    @Test
    public void testDoParseElementBeanDefinitionBuilder() {
        Element element = mock(Element.class);

        when(element.getAttribute("workflowEnvironment")).thenReturn("env");
        when(element.getAttribute("tokenRepository")).thenReturn("td");
        when(element.getAttribute("domainRepository")).thenReturn("ps");

        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        when(builder.addConstructorArgReference("env")).thenReturn(builder);
        when(builder.addConstructorArgReference("td")).thenReturn(builder);
        when(builder.addConstructorArgReference("ps")).thenReturn(builder);

        parser.doParse(element, builder);
        verify(builder).addConstructorArgReference("env");
        verify(builder).addConstructorArgReference("td");
        verify(builder).addConstructorArgReference("ps");
    }

    /**
     * Correct Listener is returned
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(TransitionEventListener.class, parser.getBeanClass(null));
    }
}
