package org.cucina.engine.server.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.cucina.engine.server.event.workflow.LoadTransitionInfoEvent;
import org.cucina.engine.service.ProcessSupportService;


/**
 *
 *
 * @author vlevine
 */
@Component
public class LoadTransitionsInfoHandler
    extends AbstractGetValueHandler<LoadTransitionInfoEvent> {
    /**
     * Creates a new ListActionableTransitionsHandler object.
     *
     * @param processSupportService .
     */
    @Autowired
    public LoadTransitionsInfoHandler(ProcessSupportService processSupportService) {
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
    protected Object doGetValue(LoadTransitionInfoEvent event) {
        return getProcessSupportService().loadTransitionInfo(event.getWorkflowId());
    }
}
