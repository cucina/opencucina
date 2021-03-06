package org.cucina.engine.server.handlers;

import org.cucina.engine.server.event.LoadTransitionInfoEvent;
import org.cucina.engine.server.event.ValueEvent;
import org.cucina.engine.service.ProcessSupportService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * @author vlevine
 */
@Component
public class LoadTransitionsInfoHandlerTest {
	private LoadTransitionInfoHandler handler;
	@Mock
	private ProcessSupportService processSupportService;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		handler = new LoadTransitionInfoHandler(processSupportService);
	}

	/**
	 *
	 */
	@Test
	public void testGetValue() {
		LoadTransitionInfoEvent event = new LoadTransitionInfoEvent();

		event.setWorkflowId("workflowId");

		Collection<Map<String, String>> value = new ArrayList<Map<String, String>>();

		when(processSupportService.loadTransitionInfo("workflowId")).thenReturn(value);

		ValueEvent result = handler.getValue(event);

		assertEquals(value, result.getValue());
	}
}
