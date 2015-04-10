package org.cucina.engine.server.handlers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.cucina.engine.server.event.CommitEvent;
import org.cucina.engine.server.event.EngineEvent;
import org.cucina.engine.server.event.workflow.BulkTransitionEvent;
import org.cucina.engine.service.ProcessSupportService;
import org.cucina.i18n.api.ListItemDto;
import org.cucina.i18n.api.ListItemService;

/**
 * 
 *
 * @author vlevine
 */
public class BulkTransitionEventHandlerTest {
	private BulkTransitionEventHandler handler;

	@Mock
	private ListItemService listItemService;

	@Mock
	private ProcessSupportService processSupportService;

	/**
	 *
	 *
	 * @throws Exception .
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		handler = new BulkTransitionEventHandler(processSupportService, listItemService);
	}

	/**
     *
     */
	@Test
	public void testAct() {
		BulkTransitionEvent event = new BulkTransitionEvent();

		event.setReason("reason");
		event.setApprovedAs("approvedAs");
		event.setAssignedTo("assignedTo");
		event.setComment("comment");
		event.setTransitionId("transitionId");
		event.setEntities(new HashMap<Serializable, Integer>());
		event.setType("type");
		event.setExtraParams(new HashMap<String, Object>());
		List<ListItemDto> li = Arrays.asList("X", "Y", "Z").stream().<ListItemDto> map(a -> {
			ListItemDto dto = new ListItemDto();
			dto.setText(a);
			return dto;
		}).collect(Collectors.toList());
		when(listItemService.loadByType("reason")).thenReturn(li);
		EngineEvent result = handler.act(event);
		assertTrue(result instanceof CommitEvent);
		verify(processSupportService).makeBulkTransition(event.getEntities(), event.getType(),
				event.getTransitionId(), event.getComment(), event.getApprovedAs(),
				event.getAssignedTo(), event.getExtraParams(), li.get(0), null);

	}
}
