package org.cucina.engine.server.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.cucina.engine.server.event.ListAllTransitionsEvent;
import org.cucina.engine.service.ProcessSupportService;


/**
 *
 *
 * @author vlevine
 */
@Component
public class ListAllTransitionsHandler
    extends AbstractGetValueHandler<ListAllTransitionsEvent> {
    /**
     * Creates a new ListActionableTransitionsHandler object.
     *
     * @param processSupportService .
     */
	@Autowired
    public ListAllTransitionsHandler(ProcessSupportService processSupportService) {
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
    protected Object doGetValue(ListAllTransitionsEvent event) {
        return getProcessSupportService()
                   .listAllTransitions(event.getIds(), event.getApplicationType());
    }
}
