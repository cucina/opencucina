package org.cucina.cluster.jms;

import org.apache.commons.lang3.StringUtils;
import org.cucina.cluster.event.ClusterControlEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ValidatingClusterEventDelegate
		implements ApplicationContextAware {
	private static final Logger LOG = LoggerFactory.getLogger(ValidatingClusterEventDelegate.class);
	private ApplicationContext applicationContext;
	private String applicationName;

	/**
	 * Creates a new ValidatingClusterEventDelegate object.
	 *
	 * @param applicationName JAVADOC.
	 */
	public ValidatingClusterEventDelegate(String applicationName) {
		Assert.notNull(applicationName);
		Assert.hasLength(applicationName);
		this.applicationName = applicationName;
	}

	/**
	 * Set applicationContext
	 *
	 * @param applicationContext ApplicationContext.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param event JAVADOC.
	 */
	public void handleMessage(ClusterControlEvent event) {
		if (StringUtils.isNotEmpty(event.getApplicationName()) &&
				!event.getApplicationName().equals(applicationName)) {
			LOG.warn("CHECK CONFIGURATION. " +
					"Received cluster message from a different application that has been configured with the same cluster.destination jms topic. " +
					"This is incorrect.  This message will be ignored. This application has application.name " +
					applicationName + ". Message [" + event + "]");

			return;
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("New event received. Publishing [" + event + "]");
		}

		applicationContext.publishEvent(event);
	}
}
