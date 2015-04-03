package org.cucina.engine.client.service;

import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.util.Assert;

import org.cucina.engine.client.Check;
import org.cucina.engine.client.Operation;
import org.cucina.engine.server.definition.CheckDescriptorDto;
import org.cucina.engine.server.definition.OperationDescriptorDto;
import org.cucina.engine.server.definition.WorkflowElementDescriptor;
import org.cucina.engine.server.event.ActionResultEvent;
import org.cucina.engine.server.event.BooleanEvent;
import org.cucina.engine.server.event.CallbackEvent;
import org.cucina.engine.server.event.EngineEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ProcessEventHandler
    implements EventHandler<CallbackEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessEventHandler.class);
    private BeanResolver beanResolver;
    private DomainFindingService domainFindingService;

    /**
     * Creates a new WorkflowEventHandler object.
     */
    public ProcessEventHandler(BeanResolver beanResolver, DomainFindingService domainFindingService) {
        Assert.notNull(beanResolver, "beanResolver is null");
        this.beanResolver = beanResolver;
        Assert.notNull(domainFindingService, "domainFindingService is null");
        this.domainFindingService = domainFindingService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param event
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public EngineEvent handleEvent(CallbackEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Handling event:" + event);
        }

        try {
            WorkflowElementDescriptor source = event.getWorkflowElementDescriptor();

            Assert.notNull(source, "no descriptor in event");

            Object pe = loadDomainObject(source.getDomainType(), source.getDomainId());

            if (LOG.isDebugEnabled()) {
                LOG.debug("Loaded object " + pe);
            }

            Object element = findBean(source);

            if (source instanceof CheckDescriptorDto) {
                boolean result = ((Check) element).test(pe, event.getParameters());

                return new BooleanEvent(event, result);
            } else if (source instanceof OperationDescriptorDto) {
                ((Operation) element).execute(pe, event.getParameters());

                return new ActionResultEvent(event.getParameters());
            }
        } catch (AccessException e) {
            LOG.error("Oops", e);
            throw new RuntimeException("Rollback", e);
        }

        // TODO look at the usage
        throw new IllegalArgumentException("Invalid event has been sent, rolling back");
    }

    private Object findBean(WorkflowElementDescriptor source)
        throws AccessException {
        String path = source.getPath();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Source=" + source);
        }

        Assert.notNull(path, "Path is null");

        Object object = beanResolver.resolve(null, path);

        Assert.notNull(object, "Failed to resolve the object from path:'" + path + "'");

        BeanWrapper bw = new BeanWrapperImpl(object);
        PropertyValues propertyValues = new MutablePropertyValues((Map<?, ?>) source);

        bw.setPropertyValues(propertyValues, true, true);

        return object;
    }

    private Object loadDomainObject(String type, Object id) {
        return domainFindingService.find(type, id);
    }
}
