package org.cucina.engine.event;

import org.cucina.engine.service.BulkWorkflowService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PluralTransitionEventListenerTest {
	private BulkWorkflowService bulkWorkflowService;
	private PluralTransitionEventListener listener;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void setup() {
		bulkWorkflowService = mock(BulkWorkflowService.class);
		listener = new PluralTransitionEventListener(bulkWorkflowService);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testOnApplicationEvent() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		Collection<Long> ids = new ArrayList<Long>();

		ids.add(1L);

		bulkWorkflowService.executeTransitions("Foo", "workflowId", "transitionId", parameters);
		bulkWorkflowService.executeTransitions(ids, "Foo", "workflowId", "transitionId", parameters);

		PluralTransitionEvent event = new PluralTransitionEvent("transitionId", "workflowId",
				"Foo", null, parameters);

		listener.onApplicationEvent(event);

		event = new PluralTransitionEvent("transitionId", "workflowId", "Foo", ids, parameters);

		listener.onApplicationEvent(event);
	}
}
