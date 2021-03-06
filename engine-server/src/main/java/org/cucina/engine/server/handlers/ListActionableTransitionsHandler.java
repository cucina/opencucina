package org.cucina.engine.server.handlers;

import org.cucina.engine.server.event.ListActionableTransitionsEvent;
import org.cucina.engine.service.ProcessSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author vlevine
 */
@Component
public class ListActionableTransitionsHandler
		extends AbstractGetValueHandler<ListActionableTransitionsEvent> {
	/**
	 * Creates a new ListActionableTransitionsHandler object.
	 *
	 * @param processSupportService .
	 */
	@Autowired
	public ListActionableTransitionsHandler(ProcessSupportService processSupportService) {
		super(processSupportService);
	}

	/**
	 * @param event .
	 * @return .
	 */
	@Override
	protected Object doGetValue(ListActionableTransitionsEvent event) {
		return getProcessSupportService().listActionableTransitions(event.getWorkflowId());
	}
}
