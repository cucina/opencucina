package org.cucina.engine.server.handlers;

import org.cucina.engine.server.event.ObtainHistorySummaryEvent;
import org.cucina.engine.server.event.ValueEvent;
import org.cucina.engine.service.ProcessSupportService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author vlevine
 */

public class ObtainHistorySummaryHandlerTest {
	private ObtainHistorySummaryHandler handler;

	@Mock
	private ProcessSupportService processSupportService;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		handler = new ObtainHistorySummaryHandler(processSupportService);
	}

	/**
	 * @param event .
	 * @return .
	 */
	@Test
	public void testGetValue() {
		ObtainHistorySummaryEvent event = new ObtainHistorySummaryEvent();

		event.setApplicationType("applicationType");
		event.setId("id");

		List<Map<Object, Object>> value = new ArrayList<Map<Object, Object>>();

		when(processSupportService.obtainHistorySummary("id", "applicationType")).thenReturn(value);

		ValueEvent result = handler.getValue(event);

		assertEquals(value, result.getValue());
	}
}
