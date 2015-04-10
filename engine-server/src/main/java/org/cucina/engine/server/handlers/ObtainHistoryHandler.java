package org.cucina.engine.server.handlers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.server.communication.HistoryRecordDto;
import org.cucina.engine.server.event.workflow.ObtainHistoryEvent;
import org.cucina.engine.service.ProcessSupportService;

/**
 *
 *
 * @author vlevine
 */
@Component
public class ObtainHistoryHandler extends AbstractGetValueHandler<ObtainHistoryEvent> {
	private ConversionService conversionService;

	/**
	 * Creates a new ListActionableTransitionsHandler object.
	 *
	 * @param processSupportService .
	 */
	@Autowired
	public ObtainHistoryHandler(ProcessSupportService processSupportService,
			ConversionService conversionService) {
		super(processSupportService);
		Assert.notNull(conversionService, "conversionService is null");
		this.conversionService = conversionService;
	}

	/**
	 *
	 *
	 * @param event .
	 *
	 * @return .
	 */
	@Override
	protected Object doGetValue(ObtainHistoryEvent event) {
		List<HistoryRecord> hrs = getProcessSupportService().obtainHistory(event.getId(),
				event.getApplicationType());
		return hrs.stream().map(hr -> conversionService.convert(hr, HistoryRecordDto.class))
				.collect(Collectors.toList());
	}
}
