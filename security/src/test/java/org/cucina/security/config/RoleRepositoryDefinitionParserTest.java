package org.cucina.security.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.cucina.security.repository.jpa.RoleRepositoryImpl;
import org.junit.Test;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class RoleRepositoryDefinitionParserTest {
    private RoleRepositoryDefinitionParser parser = new RoleRepositoryDefinitionParser();

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(RoleRepositoryImpl.class, parser.getBeanClass(null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testResolveId() {
        ParserContext parserContext = new ParserContext(null, null);
        Element element = mock(Element.class);

        assertEquals(RoleRepositoryDefinitionParser.DEFAULT_ID,
            parser.resolveId(element, null, parserContext));
        when(element.getAttribute("id")).thenReturn("dog");
        assertEquals("dog", parser.resolveId(element, null, parserContext));
    }
}
