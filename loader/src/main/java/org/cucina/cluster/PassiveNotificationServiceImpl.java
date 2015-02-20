package org.cucina.cluster;

import java.util.Collections;
import java.util.Map;

import org.cucina.loader.agent.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PassiveNotificationServiceImpl
    implements PassiveNotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(PassiveNotificationServiceImpl.class);
    private Map<String, Agent> executorRegistry;

    /**
     * Creates a new PassiveNotificationServiceImpl object.
     *
     * @param executorRegistry
     *            JAVADOC.
     */
    public PassiveNotificationServiceImpl(Map<String, Agent> executorRegistry) {
        super();
        this.executorRegistry = executorRegistry;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName
     *            JAVADOC.
     */
    @Override
    public void notify(String eventName) {
        // find executor, and execute
        Agent executor = getExecutorRegistry().get(eventName);

        if (executor != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("In passive notification service. Found executor for '" + eventName +
                    "', executing");
            }

            executor.execute();
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("In passive notification service. Could not find an executor for '" +
                    eventName + "'");
            }
        }
    }

    private Map<String, Agent> getExecutorRegistry() {
        return (executorRegistry == null) ? Collections.<String, Agent>emptyMap() : executorRegistry;
    }
}
