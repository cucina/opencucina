package org.cucina.sample.engine.client;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.messaging.MessageChannel;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import org.cucina.core.service.ContextService;
import org.cucina.core.service.ThreadLocalContextService;
import org.cucina.engine.client.MessagingProcessEngineFacade;
import org.cucina.engine.client.ProcessEngineFacade;
import org.cucina.engine.client.converters.DtoCheckConverter;
import org.cucina.engine.client.converters.DtoOperationConverter;
import org.cucina.engine.client.service.EventHandler;
import org.cucina.engine.client.service.ProcessEventHandler;
import org.cucina.engine.server.communication.ConversationContext;
import org.cucina.engine.server.event.CallbackEvent;
import org.cucina.sample.engine.client.app.ItemRepository;

/**
 *
 *
 * @author vlevine
 */
@Configuration
@ImportResource(value = "classpath:/channelContext.xml")
public class ConversationConfiguration {
	private static final Logger LOG = LoggerFactory
			.getLogger(ConversationConfiguration.class);

	/**
	 *
	 *
	 * @return .
	 */
	@Bean
	public Advisor advisor(final ContextService contextService) {
		return new AbstractPointcutAdvisor() {
			private static final long serialVersionUID = -192186951500259942L;

			@Override
			public Advice getAdvice() {
				return advice(contextService);
			}

			@Override
			public Pointcut getPointcut() {
				return new StaticMethodMatcherPointcut() {
					@Override
					public boolean matches(Method method, Class<?> clazz) {
						return method.getAnnotation(Conversation.class) != null;
					}
				};
			}
		};
	}

	/**
	 *
	 *
	 * @return .
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		return new DefaultAdvisorAutoProxyCreator();
	}

	/**
	 *
	 *
	 * @return .
	 */
	@Bean
	public ContextService contextService() {
		return new ThreadLocalContextService();
	}

	/**
	 *
	 *
	 * @param contextService .
	 *
	 * @return .
	 */
	@Bean
	public WebRequestInterceptor conversationInterceptor(
			final ContextService contextService) {
		return new WebRequestInterceptor() {
			@Override
			public void afterCompletion(WebRequest request, Exception ex)
					throws Exception {
				if (LOG.isDebugEnabled()) {
					LOG.debug("afterCompletion:" + request + " : " + ex);
				}
			}

			@Override
			public void postHandle(WebRequest request, ModelMap model)
					throws Exception {
				if (LOG.isDebugEnabled()) {
					LOG.debug("postHandle:" + request + " : " + model);
				}
			}

			@Override
			public void preHandle(WebRequest request) throws Exception {
				if (LOG.isDebugEnabled()) {
					LOG.debug("preHandle:" + request);
				}

				contextService.put(ConversationContext.CONVERSATION_ID,
						request.getHeader(ConversationContext.CONVERSATION_ID));
			}
		};
	}

	
	/**
	 *
	 *
	 * @param applicationContext .
	 *
	 * @return .
	 */
	@Bean
	public EventHandler<CallbackEvent> eventHandler(
			ApplicationContext applicationContext,
			final ItemRepository itemRepository) {
		return new ProcessEventHandler((String type, Object id) -> {
			Assert.notNull(id, "id is null");

			if (LOG.isDebugEnabled()) {
				LOG.debug("Loading Item with id " + id);
			}

			return itemRepository.findOne(new Long(id.toString()));
		}, conversionService(applicationContext));
	}

	/**
	 *
	 *
	 * @param environment .
	 *
	 * @return .
	 */
	@Bean
	public DestinationResolver myDestinationResolver(
			final Environment environment) {
		return new DestinationResolver() {
			private DestinationResolver dynamicDestinationResolver = new DynamicDestinationResolver();

			@Override
			public Destination resolveDestinationName(Session session,
					String destinationName, boolean pubSubDomain)
					throws JMSException {
				String dname = environment.getProperty("jms.destination."
						+ destinationName, destinationName);

				if (LOG.isDebugEnabled()) {
					LOG.debug("Resolved destination '" + destinationName
							+ "' to '" + dname + "'");
				}

				return dynamicDestinationResolver.resolveDestinationName(
						session, resolveName(environment, destinationName),
						pubSubDomain);
			}
		};
	}

	/**
	 *
	 *
	 * @param asyncChannel .
	 * @param workflowRequest .
	 * @param workflowReply .
	 * @param environment .
	 *
	 * @return .
	 */
	@Bean
	public ProcessEngineFacade processEngineFacade(
			@Qualifier("outputAsync") MessageChannel asyncChannel,
			@Qualifier("orchestrateChannel") MessageChannel orchestrateChannel,
			@Qualifier("workflowReply") MessageChannel workflowReply,
			Environment environment) {
		MessagingProcessEngineFacade facade = new MessagingProcessEngineFacade();

		facade.setRequestChannel(orchestrateChannel);
		facade.setReplyChannel(workflowReply);

		return facade;
	}

	/**
	 *
	 *
	 * @return .
	 */
	@Bean
	public WebMvcConfigurerAdapter webMvcConfigurerAdapter(
			final WebRequestInterceptor conversationInterceptor) {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addWebRequestInterceptor(conversationInterceptor);
			}
		};
	}

	private Advice advice(final ContextService contextService) {
		// TODO pull this out into a separate class, coordinating with
		// Orchestrator
		return new MethodInterceptor() {
			@Override
			public Object invoke(MethodInvocation mi) throws Throwable {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Before "
							+ contextService
									.get(ConversationContext.CONVERSATION_ID));
				}

				// TODO either start a conversation or bind to an existing one
				//
				//
				try {
					return mi.proceed();
				}
				catch (RuntimeException e) {
					// rolledback locally, make sure it is propagated
					// clean up the conversation , if it is not cleaned up
					//
					//
					throw e;
				}
				finally {
					if (LOG.isDebugEnabled()) {
						LOG.debug("After "
								+ contextService
										.get(ConversationContext.CONVERSATION_ID));
					}
				}
			}
		};
	}

	private ConversionService conversionService(
			ApplicationContext applicationContext) {
		// TODO should be also able to resolve a url to a bean/method
		BeanFactoryResolver beanResolver = new BeanFactoryResolver(
				applicationContext) {
			@Override
			public Object resolve(EvaluationContext context, String beanName)
					throws AccessException {
				return super.resolve(
						context,
						(beanName.startsWith("bean:") ? beanName
								.substring("bean:".length()) : beanName));
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
		String dname = environment.getProperty("jms.destination."
				+ destinationName, destinationName);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Resolved destination '" + destinationName + "' to '"
					+ dname + "'");
		}

		return dname;
	}

}
