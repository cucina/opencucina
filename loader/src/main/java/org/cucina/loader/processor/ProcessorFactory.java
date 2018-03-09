package org.cucina.loader.processor;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Map;


/**
 * Map based implementation of a processor forwarding the processing to the one
 * corresponding to the type of the object submitted for processing. Looks up
 * for a processor by the class of the subject. Failing to find, it logs the
 * event.
 *
 * @param <T>
 * @author $Author: $
 * @version $Revision: $
 */
public class ProcessorFactory
		implements Processor {
	private static final Logger LOG = LoggerFactory.getLogger(ProcessorFactory.class);
	private Map<Class<?>, Processor> processors;
	private Processor fallback = new LoggingProcessor();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param processors JAVADOC.
	 */
	public void setProcessors(Map<Class<?>, Processor> processors) {
		this.processors = processors;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param clazz     JAVADOC.
	 * @param processor JAVADOC.
	 */
	public void addProcessor(Class<?> clazz, Processor processor) {
		processors.put(clazz, processor);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param subject JAVADOC.
	 */
	@Override
	public void process(Object subject) {
		Assert.notNull(subject, "Subject is null");

		Processor processor = processors.get(subject.getClass());

		if (processor != null) {
			processor.process(subject);

			return;
		}

		fallback.process(subject);
	}

	static class LoggingProcessor
			implements Processor {
		/**
		 * JAVADOC Method Level Comments
		 *
		 * @param object JAVADOC.
		 * @return JAVADOC.
		 */
		@Override
		public void process(Object object) {
			LOG.info("No matching processor found for: " +
					ToStringBuilder.reflectionToString(object));
		}
	}
}
