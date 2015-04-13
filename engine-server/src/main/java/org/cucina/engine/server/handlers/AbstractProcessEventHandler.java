package org.cucina.engine.server.handlers;

import org.cucina.engine.server.event.CommitEvent;
import org.cucina.engine.server.event.EngineEvent;
import org.cucina.engine.server.event.RollbackEvent;
import org.cucina.engine.server.event.workflow.ProcessEvent;
import org.cucina.engine.service.ProcessSupportService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;


/**
 *
 *
 * @author vlevine
  *
 * @param <T> .
 */
public abstract class AbstractProcessEventHandler<T extends ProcessEvent>
    extends AbstractProcessSupportHolder
    implements ProcessEventHandler<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractProcessEventHandler.class);

    /**
     * Creates a new AbstractProcessEventHandler object.
     *
     * @param processSupportService .
     */
    public AbstractProcessEventHandler(ProcessSupportService processSupportService) {
        super(processSupportService);
    }

    /**
     *
     *
     * @param event .
     *
     * @return .
     */
    @Override
    @ServiceActivator
    public EngineEvent act(T event) {
        try {
            doProcess(event);

            return new CommitEvent(event);
        } catch (Exception e) {
            LOG.error("Oops", e);

            return new RollbackEvent(event);
        }
    }

    /**
     *
     *
     * @param event .
     */
    protected abstract void doProcess(T event);
}