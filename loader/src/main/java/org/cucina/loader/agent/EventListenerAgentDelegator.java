package org.cucina.loader.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class EventListenerAgentDelegator
		implements SmartApplicationListener {
	private static final Logger LOG = LoggerFactory.getLogger(EventListenerAgentDelegator.class);
	private Agent executor;
	private Class<? extends ApplicationEvent> eventType;
	private Object source;
	private int order = 0;

	/**
	 * Creates a new EventListenerExecutorDelegator object.
	 *
	 * @param eventProcessor JAVADOC.
	 */
	public EventListenerAgentDelegator(Agent executor, Class<? extends ApplicationEvent> eventType,
									   Object source) {
		Assert.notNull(executor, "executor is null");
		this.executor = executor;
		Assert.notNull(eventType, "eventType is null");
		this.eventType = eventType;
		Assert.notNull(source, "source is null");
		this.source = source;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public int getOrder() {
		return order;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param order JAVADOC.
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param arg0 JAVADOC.
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received event:" + arg0);
		}

		if (source.equals(arg0.getSource())) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Calling execute on " + executor);
			}

			executor.execute();
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param arg0 JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> arg0) {
		return arg0.isAssignableFrom(this.eventType);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param arg0 JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean supportsSourceType(Class<?> arg0) {
		return true;
	}
}
