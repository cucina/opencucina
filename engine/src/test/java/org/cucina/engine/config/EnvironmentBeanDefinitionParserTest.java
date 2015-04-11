
package org.cucina.engine.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.cucina.engine.DefaultProcessEnvironment;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class EnvironmentBeanDefinitionParserTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDoParse() {
        EnvironmentBeanDefinitionParser parser = new EnvironmentBeanDefinitionParser();
        Element element = mock(Element.class);

        when(element.getAttribute("tokenFactory")).thenReturn("tokenFactory");

        NodeList nodelist = mock(NodeList.class);

        when(nodelist.getLength()).thenReturn(2);

        Element defel = mock(Element.class);

        when(defel.getNodeName()).thenReturn("definition");
        when(defel.getLocalName()).thenReturn(null);
        when(defel.getNodeValue()).thenReturn("simple.xml");
        when(nodelist.item(0)).thenReturn(defel);

        Element lisel = mock(Element.class);

        when(lisel.getNodeName()).thenReturn("listenerRef");
        when(lisel.getLocalName()).thenReturn(null);
        when(lisel.getNodeValue()).thenReturn("hohoListener");
        when(nodelist.item(1)).thenReturn(lisel);
        when(element.getChildNodes()).thenReturn(nodelist);

        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        when(builder.addPropertyReference("tokenFactory", "tokenFactory")).thenReturn(builder);
        when(builder.addPropertyValue("definitionResources", Collections.singletonList("simple.xml")))
            .thenReturn(builder);

        List<BeanReference> lisnames = new ManagedList<BeanReference>();

        lisnames.add(new RuntimeBeanReference("hohoListener"));
        when(builder.addPropertyValue("workflowListeners", lisnames)).thenReturn(builder);
        parser.doParse(element, builder);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClass() {
        EnvironmentBeanDefinitionParser parser = new EnvironmentBeanDefinitionParser();
        Element element = mock(Element.class);

        assertEquals(DefaultProcessEnvironment.class, parser.getBeanClass(element));
    }
}
