
package org.cucina.engine.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cucina.engine.service.DefinitionServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DefinitionServiceDefinitionParserTest {
    private DefinitionServiceDefinitionParser parser = new DefinitionServiceDefinitionParser();

    /**
    * JAVADOC Method Level Comments
    */
    @Test
    public void testDoParseElementBeanDefinitionBuilder() {
        Element element = mock(Element.class);

        when(element.getAttribute("instanceFactory")).thenReturn("exex");
        when(element.getAttribute("workflowEnvironment")).thenReturn("hoho");
        when(element.getAttribute("workflowRepository")).thenReturn("woof");
        when(element.getAttribute("validator")).thenReturn("vava");

        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        when(builder.addConstructorArgReference("exex")).thenReturn(builder);
        when(builder.addConstructorArgReference("hoho")).thenReturn(builder);
        when(builder.addConstructorArgReference("woof")).thenReturn(builder);
        when(builder.addPropertyReference("validator", "vava")).thenReturn(builder);

        parser.doParse(element, builder);
        
        verify(builder).addConstructorArgReference("hoho");
        verify(builder).addConstructorArgReference("exex");
        verify(builder).addConstructorArgReference("woof");
        verify(builder).addPropertyReference("validator", "vava");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(DefinitionServiceImpl.class, parser.getBeanClass(null));
    }
}
