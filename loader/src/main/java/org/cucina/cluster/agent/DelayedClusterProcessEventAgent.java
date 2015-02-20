package org.cucina.cluster.agent;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import org.cucina.cluster.event.ClusterProcessEvent;

import org.cucina.loader.agent.Agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Causes a delay before publishing a {@code ClusterProcessEvent}.
 *
 * @author $Author: $
 * @version $Revision:
 *
 *          TODO refactor using SchedulingAgent
 */
public class DelayedClusterProcessEventAgent
    implements Agent, ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(DelayedClusterProcessEventAgent.class);
    private ApplicationContext applicationContext;
    private String eventName;
    private int delayMillis;

    /**
     * Creates a new DelayedClusterProcessEventExecutor object.
     *
     * @param delayMillis
     *            Integer.
     * @param eventName
     *            String.
     */
    public DelayedClusterProcessEventAgent(Integer delayMillis, String eventName) {
        super();
        Assert.notNull(delayMillis, "delayMillis constructor arg cannot be null");
        this.delayMillis = delayMillis;
        Assert.notNull(eventName, "eventName constructor arg cannot be null");
        this.eventName = eventName;
    }

    /**
     * Set applicationContext
     *
     * @param applicationContext
     *            ApplicationContext.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Delay before publishing.
     */
    @Override
    public void execute() {
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
            LOG.warn("unexpected interruption, will publish event nonetheless", e);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Publishing ClusterProcessEvent(" + eventName + ")");
        }

        applicationContext.publishEvent(new ClusterProcessEvent(eventName));
    }
}
