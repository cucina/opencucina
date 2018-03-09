package org.cucina.engine.service;

import org.cucina.engine.ProcessEnvironment;
import org.cucina.engine.definition.ProcessDefinitionHelper;
import org.cucina.engine.event.TransitionEvent;
import org.cucina.engine.repository.TokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.util.*;

import static org.mockito.Mockito.*;


/**
 * Test that BulkWorkflowServiceImpl functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class BulkWorkflowServiceImplTest {
	private static final String APP_TYPE = "applicationType";
	private static final String WORKFLOW_ID = "workflowId";
	private static final String TRANSITION_ID = "transitionId";
	private static final String PLACE_ID = "placeId";
	@Mock
	private ApplicationContext context;
	private BulkWorkflowServiceImpl service;
	private Map<String, Object> parameters;
	@Mock
	private ProcessEnvironment workflowEnvironment;
	@Mock
	private TokenRepository tokenRepository;

	/**
	 * Creates new map if null
	 */
	@Test
	public void executeHandlesEmptyParameters()
			throws Exception {
		Collection<Long> ids = new ArrayList<Long>();

		ids.add(1L);

		service.executeTransitions(ids, APP_TYPE, WORKFLOW_ID, TRANSITION_ID, null);
		verify(context)
				.publishEvent(new TransitionEvent(TRANSITION_ID, WORKFLOW_ID, APP_TYPE, 1L, parameters));
	}

	/**
	 * Nothing happens when no worklows are returned
	 */
	@Test
	public void runNoWorkflows() {
		ProcessDefinitionHelper helper = mock(ProcessDefinitionHelper.class);

		when(helper.findPlaceId(WORKFLOW_ID, TRANSITION_ID)).thenReturn(PLACE_ID);

		when(workflowEnvironment.getProcessDefinitionHelper()).thenReturn(helper);

		when(tokenRepository.findDomainIdsByWorkflowIdPlaceIdApplicationType(WORKFLOW_ID, PLACE_ID, APP_TYPE)).thenReturn(null);

		service.executeTransitions(APP_TYPE, WORKFLOW_ID, TRANSITION_ID, parameters);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Test
	public void runTransitions()
			throws Exception {
		ProcessDefinitionHelper helper = mock(ProcessDefinitionHelper.class);

		when(helper.findPlaceId(WORKFLOW_ID, TRANSITION_ID)).thenReturn(PLACE_ID);

		Collection<Long> ids = new HashSet<Long>();

		ids.add(2L);
		ids.add(5L);

		when(tokenRepository.findDomainIdsByWorkflowIdPlaceIdApplicationType(WORKFLOW_ID, PLACE_ID, APP_TYPE)).thenReturn(ids);

		when(workflowEnvironment.getProcessDefinitionHelper()).thenReturn(helper);

		service.executeTransitions(APP_TYPE, WORKFLOW_ID, TRANSITION_ID, parameters);

		verify(context)
				.publishEvent(new TransitionEvent(TRANSITION_ID, WORKFLOW_ID, APP_TYPE, 2L, parameters));
		verify(context)
				.publishEvent(new TransitionEvent(TRANSITION_ID, WORKFLOW_ID, APP_TYPE, 5L, parameters));
	}

	/**
	 * Publishes events for items
	 */
	@Test
	public void runTransitionsByCollection()
			throws Exception {
		Collection<Long> ids = new ArrayList<Long>();

		ids.add(1L);
		ids.add(8L);

		ApplicationContext context = mock(ApplicationContext.class);

		BulkWorkflowServiceImpl service = new BulkWorkflowServiceImpl(workflowEnvironment,
				tokenRepository);

		service.setApplicationContext(context);

		service.executeTransitions(ids, APP_TYPE, WORKFLOW_ID, TRANSITION_ID, parameters);

		verify(context)
				.publishEvent(new TransitionEvent(TRANSITION_ID, WORKFLOW_ID, APP_TYPE, 1L, parameters));
		verify(context)
				.publishEvent(new TransitionEvent(TRANSITION_ID, WORKFLOW_ID, APP_TYPE, 8L, parameters));
	}

	/**
	 * Set up for test
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		service = new BulkWorkflowServiceImpl(workflowEnvironment, tokenRepository);
		service.setApplicationContext(context);

		parameters = new HashMap<String, Object>();
		parameters.put("THIS", "THAT");
	}
}
