
package org.cucina.loader.processor;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;


/**
 * {@link Processor} implementation that publishes @see ProcessorEvent
 * to ApplicationContext. In turn this can be configured for multi-threading.
 * Naturally, there is an expectation that there is a listener for such event.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ProcessorEventMulticaster
    implements Processor, ApplicationContextAware {
    private ApplicationContext applicationContext;

    /**
     * Set {@link ApplicationContext}
     *
     * @param applicationContext
     *            {@link ApplicationContext}.
     *
     * @throws BeansException.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Public InputDataEvent to applicationContext
     *
     * @param subject
     *            Object.
     *
     * @return null.
     */
    @Override
    public Collection<Long> process(Object subject) {
        Assert.notNull(subject, "Cannot provide null subject");

        applicationContext.publishEvent(new ProcessorEvent(subject));

        return null;
    }
}
