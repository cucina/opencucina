
package org.cucina.core.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.cucina.core.PackageBasedInstanceFactory;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PackageIFDefinitionParserTest {
    private PackageIFDefinitionParser parser = new PackageIFDefinitionParser();

    /**
    * JAVADOC Method Level Comments
    */
    @Test
    public void testDoParseElementBeanDefinitionBuilder() {
        Element element = mock(Element.class);

        when(element.getAttribute("package")).thenReturn("exex");

        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        when(builder.addConstructorArgValue("exex")).thenReturn(builder);
        parser.doParse(element, builder);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetBeanClassElement() {
        assertEquals(PackageBasedInstanceFactory.class, parser.getBeanClass(null));
    }
}
