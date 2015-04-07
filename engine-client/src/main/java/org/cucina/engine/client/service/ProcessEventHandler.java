package org.cucina.engine.client.service;

import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import org.cucina.engine.client.Check;
import org.cucina.engine.client.Operation;
import org.cucina.engine.server.definition.CheckDescriptorDto;
import org.cucina.engine.server.definition.OperationDescriptorDto;
import org.cucina.engine.server.definition.WorkflowElementDto;
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
    private ConversionService conversionService;
    private DomainFindingService domainFindingService;

    /**
     * Creates a new WorkflowEventHandler object.
     */
    public ProcessEventHandler(DomainFindingService domainFindingService,
        ConversionService conversionService) {
        Assert.notNull(domainFindingService, "domainFindingService is null");
        this.domainFindingService = domainFindingService;
        Assert.notNull(conversionService, "conversionService is null");
        this.conversionService = conversionService;
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

        WorkflowElementDto source = event.getWorkflowElementDescriptor();

        Assert.notNull(source, "no descriptor in event");

        Object pe = loadDomainObject(source.getDomainType(), source.getDomainId());

        if (LOG.isDebugEnabled()) {
            LOG.debug("Loaded object " + pe);
        }

        if (source instanceof CheckDescriptorDto) {
            boolean result = conversionService.convert(source, Check.class)
                                              .test(pe, event.getParameters());

            return new BooleanEvent(event, result);
        } else if (source instanceof OperationDescriptorDto) {
            conversionService.convert(source, Operation.class).execute(pe, event.getParameters());

            return new ActionResultEvent(event.getParameters());
        }

        // TODO look at the usage
        throw new IllegalArgumentException("Invalid event has been sent, rolling back");
    }

    private Object loadDomainObject(String type, Object id) {
        return domainFindingService.find(type, id);
    }
}
