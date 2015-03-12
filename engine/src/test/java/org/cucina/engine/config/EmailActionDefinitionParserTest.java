
package org.cucina.engine.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.cucina.engine.operations.EmailOperation;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class EmailActionDefinitionParserTest {
    private EmailOperationDefinitionParser parser = new EmailOperationDefinitionParser();

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDoParseElementBeanDefinitionBuilder() {
        Element element = mock(Element.class);

        when(element.getAttribute("expressionExecutor")).thenReturn("exex");
        when(element.getAttribute("userAccessor")).thenReturn("uax");

        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        when(builder.setScope(BeanDefinition.SCOPE_PROTOTYPE)).thenReturn(builder);
        when(builder.addPropertyReference("expressionExecutor", "exex")).thenReturn(builder);
        when(builder.addPropertyReference("userAccessor", "uax")).thenReturn(builder);

        parser.doParse(element, builder);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(EmailOperation.class, parser.getBeanClass(null));
    }
}
