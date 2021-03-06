package org.cucina.engine.server.handlers;

import org.cucina.engine.server.event.ListProcessPropertiesEvent;
import org.cucina.engine.service.ProcessSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author vlevine
 */
@Component
public class ListProcessPropertiesHandler
		extends AbstractGetValueHandler<ListProcessPropertiesEvent> {
	/**
	 * Creates a new ListActionableTransitionsHandler object.
	 *
	 * @param processSupportService .
	 */
	@Autowired
	public ListProcessPropertiesHandler(ProcessSupportService processSupportService) {
		super(processSupportService);
	}

	/**
	 * @param event .
	 * @return .
	 */
	@Override
	protected Object doGetValue(ListProcessPropertiesEvent event) {
		return getProcessSupportService()
				.listWorkflowProperties(event.getIds(), event.getApplicationType());
	}
}
