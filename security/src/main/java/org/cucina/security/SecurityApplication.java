package org.cucina.security;

import org.apache.catalina.User;
import org.apache.commons.lang3.ClassUtils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.DefaultConversionService;

import org.cucina.core.InstanceFactory;
import org.cucina.core.PackageBasedInstanceFactory;

import org.cucina.security.api.remote.RemoteAccessFacade;
import org.cucina.security.service.PermissionDtoConverter;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
@SpringBootApplication(exclude = RemoteAccessFacade.class)
@ComponentScan(basePackages =  {
    "org.cucina.security", "org.cucina.core.spring"}
)
public class SecurityApplication {
    /**
     *
     * @return .
     */
    @Bean
    public InstanceFactory instanceFactory() {
        return new PackageBasedInstanceFactory(ClassUtils.getPackageName(User.class));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param args
     *            JAVADOC.
     *
     * @throws Exception
     *             JAVADOC.
     */
    public static void main(String[] args)
        throws Exception {
        SpringApplication.run(SecurityApplication.class, args);
    }

    /**
     *
     *
     * @return .
     */
    @Bean
    public ConversionService myConversionService() {
        ConversionService conversionService = new DefaultConversionService();

        if (conversionService instanceof ConverterRegistry) {
            ((ConverterRegistry) conversionService).addConverter(new PermissionDtoConverter());
        }

        return conversionService;
    }
}
