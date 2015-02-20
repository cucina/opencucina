
package org.cucina.loader.agent;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.cucina.core.service.ContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AgentRunnerImpl
    implements AgentRunner {
    private static final Logger LOG = LoggerFactory.getLogger(AgentRunnerImpl.class);
    private ContextService contextService;
    private Map<String, Agent> executorRegistry;

    /**
     * Creates a new ExecutorRunnerImpl object.
     *
     * @param contextService JAVADOC.
     */
    public AgentRunnerImpl(ContextService contextService) {
        this.contextService = contextService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param executorRegistry
     *            JAVADOC.
     */
    public void setExecutorRegistry(Map<String, Agent> executorRegistry) {
        this.executorRegistry = executorRegistry;

        if (LOG.isDebugEnabled()) {
            LOG.debug("ExecutorRegistry keys=" + executorRegistry.keySet());
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName JAVADOC.
     * @param properties JAVADOC.
     */
    public void run(String eventName, Map<Object, Object> properties) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Starting execution for event '" + eventName + "' with properties " +
                properties + " [active]");
        }

        if (MapUtils.isNotEmpty(properties)) {
            contextService.putAll(properties);
        }

        try {
            Agent executor = getExecutorRegistry().get(eventName);

            if (executor != null) {
                executor.execute();
            } else {
                LOG.info("Could not find an executor for '" + eventName + "'");
            }
        } catch (RuntimeException e) {
            LOG.warn("Oops", e);
        } finally {
            // this one to stop potential memory leak
            // TODO make this functionality in a AOP after as it is useful in a
            // number of places
            contextService.clear();
        }
    }

    private Map<String, Agent> getExecutorRegistry() {
        return (executorRegistry == null) ? Collections.<String, Agent>emptyMap()
                                          : executorRegistry;
    }
}
