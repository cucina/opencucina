package org.cucina.i18n;

import org.apache.commons.lang3.ClassUtils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.DefaultConversionService;

import org.cucina.core.CompositeInstanceFactory;
import org.cucina.core.InstanceFactory;
import org.cucina.core.PackageBasedInstanceFactory;
import org.cucina.core.service.ContextService;
import org.cucina.core.service.ThreadLocalContextService;
import org.cucina.core.spring.ContextPrinter;
import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.i18n.api.MessageDto;
import org.cucina.i18n.converter.DtoToListNodeConverter;
import org.cucina.i18n.converter.ListNodeToDtoConverter;
import org.cucina.i18n.converter.MessageConverter;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.MessageRepository;
import org.cucina.i18n.service.I18nService;

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
     * @param args
     *            JAVADOC.
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(I18nApplication.class, args);

        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(context);

        if (LOG.isTraceEnabled()) {
            ContextPrinter.traceBeanNames(context, LOG);
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
        return new CompositeInstanceFactory(new PackageBasedInstanceFactory(
                ClassUtils.getPackageName(Message.class)),
            new PackageBasedInstanceFactory(ClassUtils.getPackageName(MessageDto.class)));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public ConversionService myConversionService(InstanceFactory instanceFactory,
        MessageRepository messageRepository, I18nService i18nService) {
        ConversionService conversionService = new DefaultConversionService();

        if (conversionService instanceof ConverterRegistry) {
            ((ConverterRegistry) conversionService).addConverter(new MessageConverter(
                    instanceFactory));
            ((ConverterRegistry) conversionService).addConverter(new ListNodeToDtoConverter(
                    instanceFactory, i18nService));
            ((ConverterRegistry) conversionService).addConverter(new DtoToListNodeConverter(
                    instanceFactory, messageRepository));
        }

        return conversionService;
    }
}
