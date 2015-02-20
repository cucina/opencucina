package org.cucina.email.config;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Checks that EmailDefinitionParser functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class EmailDefinitionParserTest {
    /**
     * Test
     */
    @Test
    public void testSunnyDay() {
        Element element = mock(Element.class);

        when(element.getAttribute("host")).thenReturn("myHost");
        when(element.getAttribute("port")).thenReturn("myPort");
        when(element.getAttribute("username")).thenReturn("myUsername");
        when(element.getAttribute("password")).thenReturn("myPassword");
        when(element.getAttribute("templatesPath")).thenReturn("myTemplatesPath");
        when(element.getAttribute("from")).thenReturn("myFrom");
        when(element.getAttribute("suffix")).thenReturn("mySuffix");

        NodeList nodeList = mock(NodeList.class);

        when(nodeList.getLength()).thenReturn(0);
        when(element.getChildNodes()).thenReturn(nodeList);
        when(element.getAttribute("chunkSize")).thenReturn("myChunkSize");

        BeanDefinitionRegistry registry = mock(BeanDefinitionRegistry.class);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);

        XmlReaderContext readerContext = new XmlReaderContext(null, null, null, null, reader, null);

        ParserContext parserContext = new ParserContext(readerContext, null);

        registry.registerBeanDefinition(eq("javaMailSender"), any(BeanDefinition.class));
        registry.registerBeanDefinition(eq("freeMarkerConfigurationFactoryBean"),
            any(BeanDefinition.class));
        registry.registerBeanDefinition(eq("mimeMessagePreparatorFactory"),
            any(BeanDefinition.class));
        registry.registerBeanDefinition(eq("mailMessageChunker"), any(BeanDefinition.class));
        registry.registerBeanDefinition(eq("constructEmailService"), any(BeanDefinition.class));

        BeanDefinitionBuilder builder = mock(BeanDefinitionBuilder.class);

        when(builder.addPropertyReference("emailConstructor", "constructEmailService"))
            .thenReturn(builder);
        when(builder.addPropertyReference("javaMailSender", "javaMailSender")).thenReturn(builder);

        EmailDefinitionParser parser = new EmailDefinitionParser();

        parser.doParse(element, parserContext, builder);
    }
}
