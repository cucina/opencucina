package org.cucina.engine.server.handlers;

import org.cucina.engine.server.event.SingleTransitionEvent;
import org.cucina.engine.service.ProcessSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;


/**
 * @author vlevine
 */
@Component
public class SingleTransitionHandler
		extends AbstractProcessEventHandler<SingleTransitionEvent> {
	/**
	 * Creates a new StartWorkflowEventHandler object.
	 *
	 * @param processSupportService .
	 */
	@Autowired
	public SingleTransitionHandler(ProcessSupportService processSupportService) {
		super(processSupportService);
	}

	/**
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
