
package org.cucina.loader.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.Assert;


/**
 * Publishes event provided in constructor upon execution.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PublishingAgent
    implements Agent, ApplicationEventPublisherAware {
    private static final Logger LOG = LoggerFactory.getLogger(PublishingAgent.class);
    private ApplicationEvent event;
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Creates a new PublishingExecutor object.
     *
     * @param event ApplicationEvent.
     */
    public PublishingAgent(ApplicationEvent event) {
        super();
        Assert.notNull(event, "Must provide with an event to publish");
        this.event = event;
    }

    /**
     * Set applicationContext
     *
     * @param applicationContext ApplicationContext.
     *
     * @throws BeansException.
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
        throws BeansException {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Publishes event provided as argument to constructor.
     */
    @Override
    public void execute() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Publishing Event [" + event + "]");
        }

        applicationEventPublisher.publishEvent(event);
    }
}
