
package org.cucina.engine.jmx;

import java.util.HashMap;
import java.util.Map;

import org.cucina.engine.event.PluralTransitionEvent;
import org.cucina.engine.model.HistoryRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.Assert;


/**
 * mbean enabling workflow transitions
 *
 * @author $Author: $
 * @version $Revision: $
 */
@ManagedResource
public class WorkflowMBean
    implements ApplicationEventPublisherAware {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowMBean.class);
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Set applicationContext
     *
     * @param applicationContext
     *            ApplicationContext.
     *
     * @throws BeansException.
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
        throws BeansException {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type
     *            String.
     * @param workflowId
     *            String.
     * @param transitionId
     *            String.
     * @param comment
     *            String.
     */
    @ManagedOperation(description = "Attempt to execute the named transition on all objects " +
    "of given type identified by it's workflow which could be transitioned, providing optional comment")
    public void transition(String type, String workflowId, String transitionId, String comment) {
        Assert.notNull(type, "type argument cannot be null");
        Assert.notNull(workflowId, "workflowId argument cannot be null");
        Assert.notNull(transitionId, "transitionId argument cannot be null");
        Assert.notNull(comment, "comment argument cannot be null");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Attempting to transition type [" + type + "], workflow [" + workflowId +
                "], transition [" + transitionId + "] and comment [" + comment + "]");
        }

        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put(HistoryRecord.COMMENTS_PROPERTY, comment);
        applicationEventPublisher.publishEvent(new PluralTransitionEvent(transitionId, workflowId,
                type, null, parameters));
    }
}
