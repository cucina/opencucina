package org.cucina.engine.server.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.cucina.engine.definition.Token;
import org.cucina.engine.server.event.CommitEvent;
import org.cucina.engine.server.event.EngineEvent;
import org.cucina.engine.server.event.workflow.StartWorkflowEvent;
import org.cucina.engine.server.model.EntityDescriptor;
import org.cucina.engine.service.ProcessSupportService;

public class StartWorkflowEventHandlerTest {
	private StartWorkflowEventHandler handler;

	@Mock
	private ProcessSupportService processSupportService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		handler = new StartWorkflowEventHandler(processSupportService);
	}

	@Test
	public void testAct() {
		StartWorkflowEvent event = new StartWorkflowEvent();
		event.setType("type");
		event.setId("id");
		event.setApplicationName("applicationName");
		event.setParameters(new HashMap<String, Object>());
		Token token = mock(Token.class);
		when(processSupportService.startWorkflow(any(), any(), any())).thenReturn(token);
		EngineEvent result = handler.act(event);
		assertTrue(result instanceof CommitEvent);
		ArgumentCaptor<EntityDescriptor> acd = ArgumentCaptor.forClass(EntityDescriptor.class);
		verify(processSupportService).startWorkflow(acd.capture(), eq(event.getType()),
				eq(event.getParameters()));
		EntityDescriptor ed = acd.getValue();
		assertEquals("type", ed.getApplicationType());
		assertEquals("applicationName", ed.getApplicationName());
		assertEquals("id", ed.getRemoteId());
	}

}
