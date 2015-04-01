package org.cucina.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import org.cucina.security.api.remote.RemoteAccessFacade;
import org.cucina.security.bean.InstanceFactory;
import org.cucina.security.bean.SimpleInstanceFactory;
import org.cucina.security.converters.DtoPermissionConverter;
import org.cucina.security.converters.DtoUserConverter;
import org.cucina.security.converters.PermissionDtoConverter;
import org.cucina.security.converters.UserDtoConverter;
import org.cucina.security.repository.UserRepository;


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
        return new SimpleInstanceFactory();
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
    public ConversionService myConversionService(UserRepository userRepository) {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();

        bean.setConverters(getConverters(userRepository));
        bean.afterPropertiesSet();

        return bean.getObject();
    }

    @SuppressWarnings("rawtypes")
    private Set<Converter> getConverters(UserRepository userRepository) {
        Set<Converter> converters = new HashSet<Converter>();

        converters.add(new PermissionDtoConverter());
        converters.add(new DtoPermissionConverter());
        converters.add(new UserDtoConverter());
        converters.add(new DtoUserConverter(userRepository));

        return converters;
    }
}
