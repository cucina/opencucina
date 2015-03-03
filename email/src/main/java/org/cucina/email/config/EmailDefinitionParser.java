package org.cucina.email.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cucina.email.service.EmailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;


/**
 * Sets up EmailServiceImpl and associated classes
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class EmailDefinitionParser
    extends AbstractSingleBeanDefinitionParser {
    private static final Logger LOG = LoggerFactory.getLogger(EmailDefinitionParser.class);

    /**
     * JAVADOC Method Level Comments
     *
     * @param element JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return EmailServiceImpl.class;
    }

    /**
     * Converts the following regular spring config into one easy line of configuration
     *
     * <bean id="messagePreparatorFactory"
        *         class="org.cucina.meringue.email.MimeMessagePreparatorFactory">
        *         <property name="configuration">
        *                 <bean
        *                         class="org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean">
        *                         <property name="templateLoaderPath"
        *                                 value="/WEB-INF/classes/email/"></property>
        *                 </bean>
        *         </property>
        *         <property name="from" value="${mail.from}"/>
        *         <property name="suffix" value=".ftl"/>
        *         <property name="standardParams">
        *                 <map>
        *                         <entry key="url" value="${mail.url}"/>
        *                 </map>
        *         </property>
        * </bean>
        * <bean id="mailSender"
        *         class="org.springframework.mail.javamail.JavaMailSenderImpl">
        *         <property name="host" value="${mail.host}"/>
        *         <property name="port" value="${mail.port}"/>
        *         <property name="username" value="${mail.username}"/>
        *         <property name="password" value="${mail.password}"/>
        * </bean>
        *
        * <!-- sends emails -->
        * <bean id="emailService"
        *         class="org.cucina.meringue.email.EmailServiceImpl">
        *         <property name="emailConstructor" ref="emailConstructor"/>
        *         <property name="javaMailSender" ref="mailSender"/>
        * </bean>
        * <!-- constructs emails -->
        * <bean id="emailConstructor"
        *         class="org.cucina.meringue.email.ConstructEmailServiceImpl">
        *         <property name="mailMessageChunker" ref="mailMessageChunker"/>
        * </bean>
        * <!-- chunks emails into chunks of <chunkSize/> -->
        * <bean id="mailMessageChunker"
        *         class="org.cucina.meringue.email.MailMessageChunkerImpl">
        *         <property name="messagePreparatorFactory" ref="messagePreparatorFactory"/>
        *         <property name="chunkSize" value="${mail.chunkSize}"/>
        * </bean>
        *
     * @param element JAVADOC.
     * @param builder JAVADOC.
     */
    @Override
    protected void doParse(Element element, ParserContext parserContext,
        BeanDefinitionBuilder builder) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating BeanDefinitions for emailService");
        }

        //JavaMailSenderImpl
        String host = element.getAttribute("host");
        String port = element.getAttribute("port");
        String username = element.getAttribute("username");
        String password = element.getAttribute("password");

        BeanDefinitionBuilder mailSenderBuilder = BeanDefinitionBuilder.genericBeanDefinition(
                "org.springframework.mail.javamail.JavaMailSenderImpl");

        mailSenderBuilder.addPropertyValue("host", host);
        mailSenderBuilder.addPropertyValue("port", port);
        mailSenderBuilder.addPropertyValue("username", username);
        mailSenderBuilder.addPropertyValue("password", password);

        parserContext.getRegistry()
                     .registerBeanDefinition("javaMailSender", mailSenderBuilder.getBeanDefinition());

        //org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean
        String templatesPath = element.getAttribute("templatesPath");

        BeanDefinitionBuilder configurationFactoryBuilder = BeanDefinitionBuilder.genericBeanDefinition(
                "org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean");

        configurationFactoryBuilder.addPropertyValue("templateLoaderPath", templatesPath);

        parserContext.getRegistry()
                     .registerBeanDefinition("freeMarkerConfigurationFactoryBean",
            configurationFactoryBuilder.getBeanDefinition());

        //MimeMessagePreparatorFactory
        String from = element.getAttribute("from");
        String suffix = element.getAttribute("suffix");
        List<Element> resources = DomUtils.getChildElementsByTagName(element, "param");

        Map<String, String> params = new HashMap<String, String>();

        if (CollectionUtils.isNotEmpty(resources)) {
            for (Element relement : resources) {
                params.put(relement.getAttribute("name"), relement.getAttribute("value"));
            }
        }

        BeanDefinitionBuilder mimeMessageFactoryBuilder = BeanDefinitionBuilder.genericBeanDefinition(
                "org.cucina.meringue.email.MimeMessagePreparatorFactory");

        mimeMessageFactoryBuilder.addPropertyReference("configuration",
            "freeMarkerConfigurationFactoryBean");
        mimeMessageFactoryBuilder.addPropertyValue("from", from);
        mimeMessageFactoryBuilder.addPropertyValue("suffix", suffix);
        mimeMessageFactoryBuilder.addPropertyValue("standardParams", params);

        parserContext.getRegistry()
                     .registerBeanDefinition("mimeMessagePreparatorFactory",
            mimeMessageFactoryBuilder.getBeanDefinition());

        //MailMessageChunkerImpl
        String chunkSize = element.getAttribute("chunkSize");

        BeanDefinitionBuilder mailMessageChunkerBuilder = BeanDefinitionBuilder.genericBeanDefinition(
                "org.cucina.meringue.email.MailMessageChunkerImpl");

        mailMessageChunkerBuilder.addPropertyReference("messagePreparatorFactory",
            "mimeMessagePreparatorFactory");

        if (StringUtils.isNotEmpty(chunkSize)) {
            mailMessageChunkerBuilder.addPropertyValue("chunkSize", chunkSize);
        }

        parserContext.getRegistry()
                     .registerBeanDefinition("mailMessageChunker",
            mailMessageChunkerBuilder.getBeanDefinition());

        //ConstructEmailServiceImpl
        BeanDefinitionBuilder constructEmailServiceBuilder = BeanDefinitionBuilder.genericBeanDefinition(
                "org.cucina.meringue.email.ConstructEmailServiceImpl");

        constructEmailServiceBuilder.addPropertyReference("mailMessageChunker", "mailMessageChunker");

        parserContext.getRegistry()
                     .registerBeanDefinition("constructEmailService",
            constructEmailServiceBuilder.getBeanDefinition());

        //EmailServiceImpl add refs
        builder.addPropertyReference("emailConstructor", "constructEmailService");
        builder.addPropertyReference("javaMailSender", "javaMailSender");
    }
}
