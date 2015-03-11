package org.cucina.i18n;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;

import org.cucina.core.InstanceFactory;
import org.cucina.core.PackageBasedInstanceFactory;
import org.cucina.core.service.ContextService;
import org.cucina.core.service.ThreadLocalContextService;
import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.i18n.model.Message;
import org.cucina.i18n.service.MessageConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@SpringBootApplication
public class I18nApplication {
    private static final Logger LOG = LoggerFactory.getLogger(I18nApplication.class);

    /**
     * JAVADOC Method Level Comments
     *
     * @param args JAVADOC.
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(I18nApplication.class, args);

        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(context);

        ConversionService conversionService = (ConversionService) context.getBean("integrationConversionService");

        if (conversionService instanceof ConverterRegistry) {
            ((ConverterRegistry) conversionService).addConverter(new MessageConverter());
        }

        if (LOG.isTraceEnabled()) {
            String[] names = context.getBeanDefinitionNames();
            List<String> list = Arrays.asList(names);

            Collections.sort(list);

            for (String string : list) {
                LOG.trace(string);
            }
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public ContextService contextService() {
        return new ThreadLocalContextService();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public InstanceFactory instanceFactory() {
        return new PackageBasedInstanceFactory(ClassUtils.getPackageName(Message.class));
    }
}
