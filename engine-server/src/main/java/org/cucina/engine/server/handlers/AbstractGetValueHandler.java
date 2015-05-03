package org.cucina.engine.server.handlers;

import org.springframework.integration.annotation.ServiceActivator;

import org.cucina.engine.server.event.GetValueEvent;
import org.cucina.engine.server.event.ValueEvent;
import org.cucina.engine.service.ProcessSupportService;


/**
 * 
 *
 * @author vlevine
  *
 * @param <T> .
 */
public abstract class AbstractGetValueHandler<T extends GetValueEvent>
    extends AbstractProcessSupportHolder
    implements GetValueHandler<T> {
    /**
     * Creates a new AbstractGetValueHandler object.
     *
     * @param processSupportService .
     */
    public AbstractGetValueHandler(ProcessSupportService processSupportService) {
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
    public ValueEvent getValue(T event) {
        Object result = doGetValue(event);
        ValueEvent valueEvent = new ValueEvent(event);

        valueEvent.setValue(result);

        return valueEvent;
    }

    /**
     *
     *
     * @param event .
     *
     * @return .
     */
    protected abstract Object doGetValue(T event);
}
