package org.cucina.engine.server;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.cucina.core.model.Attachment;
import org.cucina.engine.model.Workflow;
import org.cucina.engine.server.model.EntityDescriptor;
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
@EntityScan(basePackageClasses =  {
    EntityDescriptor.class, Workflow.class, Attachment.class}
)
public class EngineApplication {
    private static final Logger LOG = LoggerFactory.getLogger(EngineApplication.class);

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
    public DestinationResolver myDestinationResolver(final Environment environment) {
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
}
