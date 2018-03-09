package org.cucina.engine.server.handlers;

import org.cucina.engine.server.event.LoadTransitionInfoEvent;
import org.cucina.engine.service.ProcessSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author vlevine
 */
@Component
public class LoadTransitionInfoHandler
		extends AbstractGetValueHandler<LoadTransitionInfoEvent> {
	/**
	 * Creates a new ListActionableTransitionsHandler object.
	 *
	 * @param processSupportService .
	 */
	@Autowired
	public LoadTransitionInfoHandler(ProcessSupportService processSupportService) {
		super(processSupportService);
	}

	/**
	 * @param event .
	 * @return .
	 */
	@Override
	protected Object doGetValue(LoadTransitionInfoEvent event) {
		return getProcessSupportService().loadTransitionInfo(event.getWorkflowId());
	}
}
