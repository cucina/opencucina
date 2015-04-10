package org.cucina.engine.server.handlers;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.cucina.engine.server.event.workflow.SingleTransitionEvent;
import org.cucina.engine.service.ProcessSupportService;


/**
 *
 *
 * @author vlevine
  */
@Component
public class SingleTransitionEventHandler
    extends AbstractProcessEventHandler<SingleTransitionEvent> {
    /**
     * Creates a new StartWorkflowEventHandler object.
     *
     * @param processSupportService .
     */
    @Autowired
    public SingleTransitionEventHandler(ProcessSupportService processSupportService) {
        super(processSupportService);
    }

    /**
     *
     *
     * @param event .
     */
    @Override
    protected void doProcess(SingleTransitionEvent event) {
        // TODO convert we.getAttachment() to an Attachment
        getProcessSupportService()
            .makeTransition((Serializable) event.getId(), event.getType(), event.getTransitionId(),
            event.getComment(), event.getApprovedAs(), event.getAssignedTo(),
            event.getExtraParams(), null);
    }
}
