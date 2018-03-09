package org.cucina.loader.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.util.Assert;


/**
 * Processing asynchronous events. Usually paired with
 * {@link ProcessorEventMulticaster} to achieve controlled
 * multithreaded processing.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ProcessorEventListener
		implements SmartApplicationListener {
	private static final Logger LOG = LoggerFactory.getLogger(ProcessorEventListener.class);
	private Processor processor;

	/**
	 * Creates a new InputDataEventListener object.
	 *
	 * @param inputDataProcessorFactory JAVADOC.
	 */
	public ProcessorEventListener(Processor processor) {
		Assert.notNull(processor, "processor is null");
		Assert.isTrue(!(processor instanceof ProcessorEventMulticaster),
				"Processor cannot be ProcessorEventMulticaster to avoid endless cycling");
		this.processor = processor;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public int getOrder() {
		return 0;
	}

	/**
	 * Delegates call to the InputDataProcessFactory
	 *
	 * @param arg0 JAVADOC.
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received event:" + arg0);
		}

		try {
			processor.process(arg0.getSource());
		} catch (Exception ex) {
			LOG.warn("Oops", ex);
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
		return ProcessorEvent.class.isAssignableFrom(arg0);
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
