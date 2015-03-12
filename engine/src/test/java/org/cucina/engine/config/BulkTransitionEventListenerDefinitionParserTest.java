
package org.cucina.engine.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.cucina.engine.event.PluralTransitionEventListener;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class BulkTransitionEventListenerDefinitionParserTest {
    private BulkTransitionEventListenerDefinitionParser parser = new BulkTransitionEventListenerDefinitionParser();

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDoParseElementBeanDefinitionBuilder() {
        Element element = mock(Element.class);

        when(element.getAttribute("bulkWorkflowService")).thenReturn("exex");

        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        when(builder.addConstructorArgReference("exex")).thenReturn(builder);

        parser.doParse(element, builder);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(PluralTransitionEventListener.class, parser.getBeanClass(null));
    }
}
