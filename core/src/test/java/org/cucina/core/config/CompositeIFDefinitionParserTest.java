
package org.cucina.core.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.cucina.core.CompositeInstanceFactory;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CompositeIFDefinitionParserTest {
    private CompositeIFDefinitionParser parser = new CompositeIFDefinitionParser();

    /**
    * JAVADOC Method Level Comments
    */
    @Test
    public void testDoParseElementBeanDefinitionBuilder() {
        Element element = mock(Element.class);
        NodeList nodelist = mock(NodeList.class);

        when(nodelist.getLength()).thenReturn(2);

        Element defel = mock(Element.class);

        when(defel.getNodeName()).thenReturn("instanceFactory");
        when(defel.getAttribute("package")).thenReturn("model");
        when(defel.getAttribute("package")).thenReturn(null);
        when(defel.getAttribute("ref")).thenReturn("simple");
        when(nodelist.getLength()).thenReturn(2);
        when(nodelist.getLength()).thenReturn(0);
        when(nodelist.item(0)).thenReturn(defel);
        when(nodelist.item(1)).thenReturn(defel);
        when(element.getChildNodes()).thenReturn(nodelist);

        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        when(builder.addPropertyValue(eq("instanceFactories"), any(ArrayList.class)))
            .thenReturn(builder);

        parser.doParse(element, builder);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(CompositeInstanceFactory.class, parser.getBeanClass(null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testResolveId() {
        ParserContext pc = new ParserContext(null, null, null);

        assertEquals("instanceFactory", parser.resolveId(null, null, pc));

        BeanDefinition bd = mock(BeanDefinition.class);

        pc = new ParserContext(null, null, bd);
        assertNull(parser.resolveId(null, null, pc));
    }
}
