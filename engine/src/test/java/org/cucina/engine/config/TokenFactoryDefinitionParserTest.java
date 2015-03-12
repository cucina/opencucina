package org.cucina.engine.config;

import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.engine.TokenFactoryImpl;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TokenFactoryDefinitionParserTest {
    private TokenFactoryDefinitionParser parser = new TokenFactoryDefinitionParser();

    /**
    * JAVADOC Method Level Comments
    */
    @Test
    public void testDoParseElementBeanDefinitionBuilder() {
        Element element = mock(Element.class);

        when(element.getAttribute(SingletonBeanFactory.INSTANCE_FACTORY_ID)).thenReturn("exex");
        when(element.getAttribute("tokenRepository")).thenReturn("hoho");
        when(element.getAttribute("tokenClass")).thenReturn("pipi");

        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        when(builder.addConstructorArgReference("exex")).thenReturn(builder);
        when(builder.addConstructorArgReference("hoho")).thenReturn(builder);
        when(builder.addConstructorArgReference("pipi")).thenReturn(builder);

        AbstractBeanDefinition abd = mock(AbstractBeanDefinition.class);
        ConstructorArgumentValues cav = mock(ConstructorArgumentValues.class);

        cav.addGenericArgumentValue(any(ValueHolder.class));
        cav.addGenericArgumentValue(any(ValueHolder.class));
        cav.addGenericArgumentValue(any(ValueHolder.class));

        when(abd.getConstructorArgumentValues()).thenReturn(cav);
        when(builder.getRawBeanDefinition()).thenReturn(abd);

        parser.doParse(element, builder);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(TokenFactoryImpl.class, parser.getBeanClass(null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testResolveId() {
        assertEquals("tokenFactory", parser.getDefaultId());
    }
}
