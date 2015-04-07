package org.cucina.i18n;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;

import org.cucina.core.InstanceFactory;
import org.cucina.core.PackageBasedInstanceFactory;
import org.cucina.core.service.ContextService;
import org.cucina.core.service.ThreadLocalContextService;
import org.cucina.core.spring.ContextPrinter;
import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.i18n.converter.DtoToListItemConverter;
import org.cucina.i18n.converter.ListItemToDtoConverter;
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
        return new PackageBasedInstanceFactory(ClassUtils.getPackageName(Message.class));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public ConversionService myConversionService(MessageRepository messageRepository,
        I18nService i18nService) {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();

        bean.setConverters(getConverters(messageRepository, i18nService));
        bean.afterPropertiesSet();

        return bean.getObject();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Set<?> getConverters(MessageRepository messageRepository, I18nService i18nService) {
        Set result = new HashSet();

        result.add(new MessageConverter());
        result.add(new ListItemToDtoConverter(i18nService));
        result.add(new DtoToListItemConverter(messageRepository));

        return result;
    }
}
