package org.cucina.core.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cucina.core.repository.jpa.ListNodeRepositoryImpl;
import org.junit.Test;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ListNodeRepositoryDefinitionParserTest {
    private ListNodeRepositoryDefinitionParser parser = new ListNodeRepositoryDefinitionParser();

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDoParseElementBeanDefinitionBuilder() {
        Element element = mock(Element.class);

        when(element.getAttribute("i18nService")).thenReturn("I18N");

        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        AbstractBeanDefinition abd = mock(AbstractBeanDefinition.class);
        ConstructorArgumentValues cav = mock(ConstructorArgumentValues.class);

        when(abd.getConstructorArgumentValues()).thenReturn(cav);
        when(builder.getRawBeanDefinition()).thenReturn(abd);
        parser.doParse(element, builder);
        verify(cav).addGenericArgumentValue(any(ValueHolder.class));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(ListNodeRepositoryImpl.class, parser.getBeanClass(null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testResolveId() {
        ParserContext parserContext = new ParserContext(null, null);
        Element element = mock(Element.class);

        assertEquals(ListNodeRepositoryDefinitionParser.DEFAULT_ID,
            parser.resolveId(element, null, parserContext));
        when(element.getAttribute("id")).thenReturn("dog");
        assertEquals("dog", parser.resolveId(element, null, parserContext));
    }
}
