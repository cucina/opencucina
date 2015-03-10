package org.cucina.i18n;

import org.apache.commons.lang3.ClassUtils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.cucina.core.InstanceFactory;
import org.cucina.core.PackageBasedInstanceFactory;
import org.cucina.core.service.ContextService;
import org.cucina.core.service.ThreadLocalContextService;

import org.cucina.i18n.model.Message;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@SpringBootApplication
public class I18nApplication {
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
     * @param args JAVADOC.
     */
    public static void main(String[] args) {
        SpringApplication.run(I18nApplication.class, args);
    }
}
