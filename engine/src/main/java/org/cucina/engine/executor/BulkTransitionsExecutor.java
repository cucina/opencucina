package org.cucina.engine.executor;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.engine.event.PluralTransitionEvent;
import org.cucina.loader.agent.Agent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Publishes the pre-configured events to the applicationContext.
 *
 */
public class BulkTransitionsExecutor
    implements Agent, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private Collection<PluralTransitionEvent> transitions;

    /**
     * Creates a new WorkflowTransitionExecutor object.
     *
     * @param transitions
     *            JAVADOC.
     * @param processor
     *            JAVADOC.
     */
    public BulkTransitionsExecutor(Collection<PluralTransitionEvent> transitions) {
        this.transitions = transitions;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationContext
     *            JAVADOC.
     *
     * @throws BeansException
     *             JAVADOC.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Processes 'scheduled' transitions.
     */
    @Override
    public void execute() {
        if (CollectionUtils.isNotEmpty(transitions)) {
            for (PluralTransitionEvent event : transitions) {
                applicationContext.publishEvent(event);
            }
        }
    }
}
