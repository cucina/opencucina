package org.cucina.eggtimer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.JndiDestinationResolver;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@EnableAutoConfiguration
@ComponentScan
@EnableJms
public class EggTimer {
    /**
     * JAVADOC Method Level Comments
     *
     * @param args JAVADOC.
     *
     * @throws Exception JAVADOC.
     */
    public static void main(String[] args)
        throws Exception {
        SpringApplication.run(EggTimer.class, args);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Bean
    public DestinationResolver destinationResolver() {
    	//TODO provide optional mapping in properties 
        JndiDestinationResolver destinationResolver = new JndiDestinationResolver();

        destinationResolver.setFallbackToDynamicDestination(true);

        return destinationResolver;
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
}
