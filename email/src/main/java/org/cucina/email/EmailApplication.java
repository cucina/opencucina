package org.cucina.email;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@SpringBootApplication
@EnableJms
public class EmailApplication {
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
        SpringApplication.run(EmailApplication.class, args);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public DefaultJmsListenerContainerFactory myJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setDestinationResolver(destinationResolver());
        factory.setConcurrency("5");

        return factory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    private DestinationResolver destinationResolver() {
        return new DestinationResolver() {
                private DestinationResolver dynamicDestinationResolver = new DynamicDestinationResolver();

                @Override
                public Destination resolveDestinationName(Session session, String destinationName,
                    boolean pubSubDomain)
                    throws JMSException {
                    String dname = environment.getProperty("jms.destination." + destinationName,
                            destinationName);

                    return dynamicDestinationResolver.resolveDestinationName(session, dname,
                        pubSubDomain);
                }
            };
    }
}
