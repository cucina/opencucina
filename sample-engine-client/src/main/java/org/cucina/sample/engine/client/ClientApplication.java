package org.cucina.sample.engine.client;

import java.util.HashSet;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.integration.channel.FixedSubscriberChannel;
import org.springframework.integration.jms.DynamicJmsTemplate;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.util.Assert;

import org.cucina.conversation.ConversationConfiguration;
import org.cucina.conversation.EventHandler;
import org.cucina.conversation.events.ConversationEvent;
import org.cucina.core.spring.ContextPrinter;
import org.cucina.engine.client.MessagingProcessEngineFacade;
import org.cucina.engine.client.ProcessEngineFacade;
import org.cucina.engine.client.converters.DtoCheckConverter;
import org.cucina.engine.client.converters.DtoOperationConverter;
import org.cucina.engine.client.service.ApplicationRegistrator;
import org.cucina.engine.client.service.ProcessEventHandler;
import org.cucina.engine.server.jms.XmlEncoderDecoderConverter;
import org.cucina.sample.engine.client.app.ItemRepository;

/**
 *
 *
 * @author vlevine
 */
@SpringBootApplication
@Import(ConversationConfiguration.class)
@EnableJms
public class ClientApplication {
	private static final Logger LOG = LoggerFactory.getLogger(ClientApplication.class);

	public static void main(String[] args) throws Exception {
		ApplicationContext context = SpringApplication.run(ClientApplication.class, args);

		if (LOG.isTraceEnabled()) {
			ContextPrinter.traceBeanNames(context, LOG);
		}
	}

	@Bean
	public EventHandler<ConversationEvent> eventHandler(ApplicationContext applicationContext,
			final ItemRepository itemRepository) {
		return new ProcessEventHandler((String type, Object id) -> {
			Assert.notNull(id, "id is null");

			if (LOG.isDebugEnabled()) {
				LOG.debug("Loading Item with id " + id);
			}

			return itemRepository.findOne(new Long(id.toString()));
		}, conversionService(applicationContext));
	}

	@Bean
	public MessageChannel outputAsync(
			@Qualifier("jmsConnectionFactory") ConnectionFactory connectionFactory,
			DestinationResolver destinationResolver) {
		DynamicJmsTemplate jmsTemplate = new DynamicJmsTemplate();
		jmsTemplate.setConnectionFactory(connectionFactory);
		jmsTemplate.setDestinationResolver(destinationResolver);
		// TODO server should be detecting conversion type
		jmsTemplate.setMessageConverter(new XmlEncoderDecoderConverter());

		JmsSendingMessageHandler handler = new JmsSendingMessageHandler(jmsTemplate);
		handler.setDestinationName("server.queue");
		SubscribableChannel ch = new FixedSubscriberChannel(handler);

		return ch;
	}

	/**
	 * Uses properties to attempt to map a JMS destination name to an actual
	 * queue/topic name.
	 * 
	 * @param environment
	 * @return
	 */
	@Bean
	public DestinationResolver myDestinationResolver(final Environment environment) {
		return new DestinationResolver() {
			private DestinationResolver dynamicDestinationResolver = new DynamicDestinationResolver();

			@Override
			public Destination resolveDestinationName(Session session, String destinationName,
					boolean pubSubDomain) throws JMSException {
				String dname = environment.getProperty("jms.destination." + destinationName,
						destinationName);

				if (LOG.isDebugEnabled()) {
					LOG.debug("Resolved destination '" + destinationName + "' to '" + dname + "'");
				}

				return dynamicDestinationResolver.resolveDestinationName(session,
						resolveName(environment, destinationName), pubSubDomain);
			}
		};
	}

	@Bean
	public ProcessEngineFacade processEngineFacade(
			@Qualifier("conversation") MessageChannel orchestrateChannel,
			@Qualifier("conversationReply") MessageChannel workflowReply) {
		MessagingProcessEngineFacade facade = new MessagingProcessEngineFacade();

		facade.setRequestChannel(orchestrateChannel);
		facade.setReplyChannel(workflowReply);

		return facade;
	}

	private String resolveName(Environment environment, String destinationName) {
		String dname = environment.getProperty("jms.destination." + destinationName,
				destinationName);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Resolved destination '" + destinationName + "' to '" + dname + "'");
		}

		return dname;
	}

	// TODO move to the server api
	@Bean
	public ApplicationRegistrator applicationRegistrator(
			@Qualifier("outputAsync") MessageChannel asyncChannel) {
		return new ApplicationRegistrator("client", "myQueue", asyncChannel);
	}

	private ConversionService conversionService(ApplicationContext applicationContext) {
		// TODO should be also able to resolve a url to a bean/method
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
}
