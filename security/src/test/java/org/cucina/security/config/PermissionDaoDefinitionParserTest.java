package org.cucina.security.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.cucina.security.repository.jpa.PermissionRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
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
public class PermissionDaoDefinitionParserTest {
    @Mock
    private AbstractBeanDefinition abd;
    @Mock
    private BeanDefinitionBuilder builder;
    @Mock
    private ConstructorArgumentValues cav;
    @Mock
    private Element element;
    private PermissionRepositoryDefinitionParser parser = new PermissionRepositoryDefinitionParser();

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(PermissionRepositoryImpl.class, parser.getBeanClass(null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testResolveId() {
        ParserContext parserContext = new ParserContext(null, null);
        Element element = mock(Element.class);

        assertEquals(PermissionRepositoryDefinitionParser.DEFAULT_ID,
            parser.resolveId(element, null, parserContext));
        when(element.getAttribute("id")).thenReturn("dog");
        assertEquals("dog", parser.resolveId(element, null, parserContext));
    }
}
