package org.cucina.engine.server.handlers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.cucina.engine.server.event.CommitEvent;
import org.cucina.engine.server.event.EngineEvent;
import org.cucina.engine.server.event.workflow.SingleTransitionEvent;
import org.cucina.engine.service.ProcessSupportService;



public class SingleTransitionEventHandlerTest {
	private SingleTransitionEventHandler handler;

	@Mock
	private ProcessSupportService processSupportService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		handler = new SingleTransitionEventHandler(processSupportService);
	}

	@Test
	public void testAct() {
		SingleTransitionEvent event = new SingleTransitionEvent();
		event.setType("applicationName");
		event.setId("id");
		event.setTransitionId("transitionId");
		event.setComment("comment");
		event.setApprovedAs("approvedAs");
		event.setAssignedTo("assignedTo");

		EngineEvent result = handler.act(event);
		assertTrue(result instanceof CommitEvent);
		verify(processSupportService).makeTransition((Serializable) event.getId(), event.getType(),
				event.getTransitionId(), event.getComment(), event.getApprovedAs(),
				event.getAssignedTo(), null, null);
	}

}
