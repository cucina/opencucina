package org.cucina.engine.server.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.cucina.engine.server.event.ListTransitionsEvent;
import org.cucina.engine.service.ProcessSupportService;


/**
 *
 *
 * @author vlevine
 */
@Component
public class ListTransitionsHandler
    extends AbstractGetValueHandler<ListTransitionsEvent> {
    /**
     * Creates a new ListActionableTransitionsHandler object.
     *
     * @param processSupportService .
     */
    @Autowired
    public ListTransitionsHandler(ProcessSupportService processSupportService) {
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
    protected Object doGetValue(ListTransitionsEvent event) {
        return getProcessSupportService()
                   .listTransitions(event.getIds(), event.getApplicationType());
    }
}
