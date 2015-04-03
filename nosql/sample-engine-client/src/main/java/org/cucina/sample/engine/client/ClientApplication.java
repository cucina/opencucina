package org.cucina.sample.engine.client;

import java.math.BigInteger;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.messaging.MessageChannel;

import org.cucina.engine.client.MessagingProcessEngineFacade;
import org.cucina.engine.client.ProcessEngineFacade;
import org.cucina.engine.client.service.DomainFindingService;
import org.cucina.engine.client.service.EventHandler;
import org.cucina.engine.client.service.ProcessEventHandler;
import org.cucina.engine.server.event.CallbackEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 *
 * @author vlevine
 */
@SpringBootApplication
@EnableJms
@ImportResource(value = "classpath:/channelContext.xml")
public class ClientApplication {
    private static final Logger LOG = LoggerFactory.getLogger(ClientApplication.class);

    /**
     *
     *
     * @param applicationContext .
     *
     * @return .
     */
    @Bean
    public EventHandler<CallbackEvent> eventHandler(ApplicationContext applicationContext,
        final ItemRepository itemRepository) {
        BeanFactoryResolver beanResolver = new BeanFactoryResolver(applicationContext);
        DomainFindingService domainFindingService = new DomainFindingService() {
                @Override
                public Object find(String type, Object id) {
                    BigInteger bid = BigInteger.valueOf((Long) id);

                    return itemRepository.findOne(bid);
                }
            };

        return new ProcessEventHandler(beanResolver, domainFindingService);
    }

    /**
     *
     *
     * @param args
     *            .
     *
     * @throws Exception .
     */
    public static void main(String[] args)
        throws Exception {
        SpringApplication.run(ClientApplication.class, args);
    }

    /**
     *
     *
     * @param environment
     *            .
     *
     * @return .
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

    /**
     *
     * @return .
     */
    @Bean
    public ProcessEngineFacade processEngineFacade(
        @Qualifier("outputAsync")
    MessageChannel asyncChannel, @Qualifier("workflowRequest")
    MessageChannel workflowRequest, @Qualifier("workflowReply")
    MessageChannel workflowReply) {
        MessagingProcessEngineFacade facade = new MessagingProcessEngineFacade("client", "myQueue",
                asyncChannel);

        facade.setRequestChannel(workflowRequest);
        facade.setReplyChannel(workflowReply);

        return facade;
    }
}
