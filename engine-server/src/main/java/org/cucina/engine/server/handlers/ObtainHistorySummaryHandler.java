package org.cucina.engine.server.handlers;

import org.cucina.engine.server.event.ObtainHistorySummaryEvent;
import org.cucina.engine.service.ProcessSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author vlevine
 */
@Component
public class ObtainHistorySummaryHandler
		extends AbstractGetValueHandler<ObtainHistorySummaryEvent> {
	/**
	 * Creates a new ListActionableTransitionsHandler object.
	 *
	 * @param processSupportService .
	 */
	@Autowired
	public ObtainHistorySummaryHandler(ProcessSupportService processSupportService) {
		super(processSupportService);
	}

	/**
	 * @param event .
	 * @return .
	 */
	@Override
	protected Object doGetValue(ObtainHistorySummaryEvent event) {
		return getProcessSupportService()
				.obtainHistorySummary(event.getId(), event.getApplicationType());
	}
}
