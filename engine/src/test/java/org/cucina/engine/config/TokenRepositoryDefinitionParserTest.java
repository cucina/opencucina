package org.cucina.engine.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.engine.repository.jpa.TokenRepositoryImpl;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TokenRepositoryDefinitionParserTest {
    private TokenRepositoryDefinitionParser parser = new TokenRepositoryDefinitionParser();

    /**
    * JAVADOC Method Level Comments
    */
    @Test
    public void testDoParseElementBeanDefinitionBuilder() {
        Element element = mock(Element.class);

        when(element.getAttribute(SingletonBeanFactory.INSTANCE_FACTORY_ID)).thenReturn("hoho");

        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        when(builder.addConstructorArgReference("hoho")).thenReturn(builder);

        parser.doParse(element, builder);
        verify(builder).addConstructorArgReference("hoho");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(TokenRepositoryImpl.class, parser.getBeanClass(null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testResolveId() {
        assertEquals("tokenRepository", parser.getDefaultId());
    }
}
