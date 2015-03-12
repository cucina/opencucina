package org.cucina.engine.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.cucina.engine.operations.SaveOperation;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * Check DeleteActionDefinitionParser functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SaveActionDefinitionParserTest {
    private SaveOperationDefinitionParser parser = new SaveOperationDefinitionParser();

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDoParseElementBeanDefinitionBuilder() {
        Element element = mock(Element.class);

        when(element.getAttribute("domainRepository")).thenReturn("tD");

        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        when(builder.setScope(BeanDefinition.SCOPE_PROTOTYPE)).thenReturn(builder);
        when(builder.addConstructorArgReference("tD")).thenReturn(builder);
        parser.doParse(element, builder);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(SaveOperation.class, parser.getBeanClass(null));
    }
}
