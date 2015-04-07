package org.cucina.sample.engine.client;

import java.math.BigInteger;

import java.util.HashSet;
import java.util.Set;

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
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.Assert;

import org.cucina.engine.client.MessagingProcessEngineFacade;
import org.cucina.engine.client.ProcessEngineFacade;
import org.cucina.engine.client.converters.DtoCheckConverter;
import org.cucina.engine.client.converters.DtoOperationConverter;
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
     * @param applicationContext .
     *
     * @return .
     */
    @Bean
    public EventHandler<CallbackEvent> eventHandler(ApplicationContext applicationContext,
        final ItemRepository itemRepository) {
        DomainFindingService domainFindingService = new DomainFindingService() {
                @Override
                public Object find(String type, Object id) {
                    Assert.notNull(id, "id is null");

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Loading Item with id " + id);
                    }

                    return itemRepository.findOne(new BigInteger(id.toString()));
                }
            };

        return new ProcessEventHandler(domainFindingService, conversionService(applicationContext));
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

                    return dynamicDestinationResolver.resolveDestinationName(session,
                        resolveName(environment, destinationName), pubSubDomain);
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
    MessageChannel workflowReply, Environment environment) {
        MessagingProcessEngineFacade facade = new MessagingProcessEngineFacade("client",
                resolveName(environment, "engine.client.queue"), asyncChannel);

        facade.setRequestChannel(workflowRequest);
        facade.setReplyChannel(workflowReply);

        return facade;
    }

    
    private ConversionService conversionService(ApplicationContext applicationContext) {
        BeanFactoryResolver beanResolver = new BeanFactoryResolver(applicationContext) {
                @Override
                public Object resolve(EvaluationContext context, String beanName)
                    throws AccessException {
                    return super.resolve(context,
                        (beanName.startsWith("bean:") ? beanName.substring("bean:".length())
                                                      : beanName));
                }
            };

        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        Set<Converter<?, ?>> cons = new HashSet<Converter<?, ?>>();

        cons.add(new DtoCheckConverter(beanResolver));
        cons.add(new DtoOperationConverter(beanResolver));
        factoryBean.setConverters(cons);
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    private String resolveName(Environment environment, String destinationName) {
        String dname = environment.getProperty("jms.destination." + destinationName, destinationName);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Resolved destination '" + destinationName + "' to '" + dname + "'");
        }

        return dname;
    }
}
