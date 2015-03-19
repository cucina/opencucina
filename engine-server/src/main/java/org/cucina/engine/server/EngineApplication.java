package org.cucina.engine.server;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.cucina.core.InstanceFactory;
import org.cucina.core.service.ContextService;
import org.cucina.core.service.ThreadLocalContextService;
import org.cucina.i18n.converter.MessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
@SpringBootApplication
@EnableJms
@ImportResource(value = "classpath:/channelContext.xml")
public class EngineApplication {
    private static final Logger LOG = LoggerFactory.getLogger(EngineApplication.class);
    @Autowired
    private Environment environment;

    /**
     * JAVADOC Method Level Comments
     *
     * @param args JAVADOC.
     *
     * @throws Exception JAVADOC.
     */
    public static void main(String[] args)
        throws Exception {
        SpringApplication.run(EngineApplication.class, args);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public ConversionService myConversionService(InstanceFactory instanceFactory) {
        ConversionService conversionService = new DefaultConversionService();

        if (conversionService instanceof ConverterRegistry) {
            ((ConverterRegistry) conversionService).addConverter(new MessageConverter(
                    instanceFactory));
        }

        return conversionService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public DestinationResolver myDestinationResolver() {
        return new DestinationResolver() {
                private DestinationResolver dynamicDestinationResolver = new DynamicDestinationResolver();

                @Override
                public Destination resolveDestinationName(Session session, String destinationName,
                    boolean pubSubDomain)
                    throws JMSException {
                    String dname = environment.getProperty("jms.destination." + destinationName,
                            destinationName);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Resolved destination '" + destinationName + "' to '" + dname +
                            "'");
                    }

                    return dynamicDestinationResolver.resolveDestinationName(session, dname,
                        pubSubDomain);
                }
            };
    }
    
    @Bean
    public ContextService tempContextService(){
    	return new ThreadLocalContextService();
    }
}
