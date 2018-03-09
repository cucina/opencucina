package org.cucina.conversation;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.cucina.conversation.events.ConversationEvent;
import org.cucina.core.service.ContextService;
import org.cucina.core.service.ThreadLocalContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.lang.reflect.Method;


/**
 * @author vlevine
 */
@Configuration
@ImportResource(value = "classpath:/conversationContext.xml")
public class ConversationConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(ConversationConfiguration.class);

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

	@Bean
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		return new DefaultAdvisorAutoProxyCreator();
	}

	@Bean
	public ContextService contextService() {
		return new ThreadLocalContextService();
	}

	@Bean
	public WebRequestInterceptor conversationInterceptor(final ContextService contextService) {
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
			public void preHandle(WebRequest request)
					throws Exception {
				if (LOG.isDebugEnabled()) {
					LOG.debug("preHandle:" + request);
				}

				contextService.put(Operative.CONVERSATION_ID,
						request.getHeader(Operative.CONVERSATION_ID));
			}
		};
	}

	@Bean
	public OperativeFactory operativeFactory(
			@Qualifier("outputPollableChannel")
					PollableChannel outputPollableChannel, @Qualifier("callbackReply")
					MessageChannel callbackReply,
			@Qualifier("eventHandler")
					EventHandler<ConversationEvent> eventHandler) {
		return new OperativeFactoryImpl(activeOperativePoolFactory(outputPollableChannel,
				callbackReply, eventHandler), passiveOperativePoolFactory());
	}

	@Bean
	public OrchestratorHandler orchestratorHandler(
			@Qualifier("conversationReply")
					MessageChannel conversationReply, OperativeFactory operativeFactory) {
		return new OrchestratorHandler(conversationReply, operativeFactory);
	}

	@Bean
	public TransactionHandler transactionHandler(
			@Qualifier("outputAsync")
					MessageChannel asyncChannel) {
		return new TransactionHandlerImpl(asyncChannel);
	}

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

	private BasePooledObjectFactory<Operative> activeOperativePoolFactory(
			PollableChannel outputPollableChannel, MessageChannel callbackReply,
			EventHandler<ConversationEvent> eventHandler) {
		ObjectPool<PollableChannel> mcPool = new GenericObjectPool<PollableChannel>(messagePoolFactory());

		return new BasePooledObjectFactory<Operative>() {
			@Override
			public void activateObject(PooledObject<Operative> p)
					throws Exception {
				Operative operative = p.getObject();

				operative.setCallbackChannel(mcPool.borrowObject());
			}

			@Override
			public Operative create()
					throws Exception {
				ActiveOperative op = new ActiveOperative(outputPollableChannel);

				op.setCallbackReplyChannel(callbackReply);
				op.setEventHandler(eventHandler);

				return op;
			}

			@Override
			public void passivateObject(PooledObject<Operative> p)
					throws Exception {
				Operative op = p.getObject();

				mcPool.returnObject(op.getCallbackChannel());
			}

			@Override
			public PooledObject<Operative> wrap(Operative q) {
				return new DefaultPooledObject<Operative>(q);
			}
		};
	}

	private Advice advice(final ContextService contextService) {
		// TODO pull this out into a separate class, coordinating with
		// Orchestrator
		return new MethodInterceptor() {
			@Override
			public Object invoke(MethodInvocation mi)
					throws Throwable {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Before " + contextService.get(Operative.CONVERSATION_ID));
				}

				// TODO either start a conversation or bind to an existing one
				//
				//
				try {
					return mi.proceed();
				} catch (RuntimeException e) {
					// rolledback locally, make sure it is propagated
					// clean up the conversation , if it is not cleaned up
					//
					//
					throw e;
				} finally {
					if (LOG.isDebugEnabled()) {
						LOG.debug("After " + contextService.get(Operative.CONVERSATION_ID));
					}
				}
			}
		};
	}

	private BasePooledObjectFactory<PollableChannel> messagePoolFactory() {
		return new BasePooledObjectFactory<PollableChannel>() {
			@Override
			public PollableChannel create()
					throws Exception {
				return new QueueChannel(10);
			}

			@Override
			public PooledObject<PollableChannel> wrap(PollableChannel obj) {
				return new DefaultPooledObject<PollableChannel>(obj);
			}
		};
	}

	private BasePooledObjectFactory<Operative> passiveOperativePoolFactory() {
		ObjectPool<PollableChannel> mcPool = new GenericObjectPool<PollableChannel>(messagePoolFactory());

		return new BasePooledObjectFactory<Operative>() {
			@Override
			public void activateObject(PooledObject<Operative> p)
					throws Exception {
				Operative operative = p.getObject();

				operative.setCallbackChannel(mcPool.borrowObject());
			}

			@Override
			public Operative create()
					throws Exception {
				return new PassiveOperative();
			}

			@Override
			public void passivateObject(PooledObject<Operative> p)
					throws Exception {
				Operative op = p.getObject();

				mcPool.returnObject(op.getCallbackChannel());
			}

			@Override
			public PooledObject<Operative> wrap(Operative q) {
				return new DefaultPooledObject<Operative>(q);
			}
		};
	}
}
