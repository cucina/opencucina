package org.cucina.engine.server.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.cucina.engine.definition.Token;
import org.cucina.engine.server.event.workflow.StartWorkflowEvent;
import org.cucina.engine.server.model.EntityDescriptor;
import org.cucina.engine.service.ProcessSupportService;


/**
 *
 *
 * @author vlevine
  */
@Component
public class StartWorkflowEventHandler
    extends AbstractProcessEventHandler<StartWorkflowEvent> {
    /**
     * Creates a new StartWorkflowEventHandler object.
     *
     * @param processSupportService .
     */
    @Autowired
    public StartWorkflowEventHandler(ProcessSupportService processSupportService) {
        super(processSupportService);
    }

    /**
     *
     *
     * @param event .
     */
    @Override
    protected void doProcess(StartWorkflowEvent event) {
        Token token = getProcessSupportService()
                          .startWorkflow(new EntityDescriptor(event.getType(),
                    event.getId().toString(), event.getApplicationName()), event.getType(),
                event.getParameters());

        if (token == null) {
            throw new RuntimeException("Failed to start workflow");
        }
    }
}
