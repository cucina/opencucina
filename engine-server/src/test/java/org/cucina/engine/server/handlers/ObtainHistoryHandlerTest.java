package org.cucina.engine.server.handlers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.server.definition.HistoryRecordDto;
import org.cucina.engine.server.event.ObtainHistoryEvent;
import org.cucina.engine.server.event.ValueEvent;
import org.cucina.engine.service.ProcessSupportService;

/**
 *
 *
 * @author vlevine
 */
public class ObtainHistoryHandlerTest {
	private ObtainHistoryHandler handler;

	@Mock
	private ProcessSupportService processSupportService;

	@Mock
	private ConversionService conversionService;

	/**
	 *
	 *
	 * @throws Exception .
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		handler = new ObtainHistoryHandler(processSupportService, conversionService);
	}

	/**
	 *
	 *
	 * @param event .
	 *
	 * @return .
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void testGetValue() {
		ObtainHistoryEvent event = new ObtainHistoryEvent();

		event.setApplicationType("applicationType");
		event.setId("id");

		List<HistoryRecord> value = new ArrayList<HistoryRecord>();
		HistoryRecord hr = new HistoryRecord();
		value.add(hr);
		when(processSupportService.obtainHistory("id", "applicationType")).thenReturn(value);
		HistoryRecordDto hrd = new HistoryRecordDto();
		when(conversionService.convert(hr, HistoryRecordDto.class)).thenReturn(hrd);

		ValueEvent result = handler.getValue(event);

		assertTrue(((List)result.getValue()).contains(hrd));
	}
}
