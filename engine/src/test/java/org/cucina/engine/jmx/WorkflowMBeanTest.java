package org.cucina.engine.jmx;

import org.cucina.engine.event.PluralTransitionEvent;
import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.testassist.Foo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;

import static org.mockito.Mockito.verify;


/**
 * Tests that WorkflowMBean functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class WorkflowMBeanTest {
	private static final String WORKFLOW_ID = "workflowId";
	private static final String TRANSITION_ID = "transitionId";
	private static final String TYPE = Foo.TYPE;
	private static final String COMMENT = "oh hello";

	/**
	 * This is a field JAVADOC
	 */
	@Mock
	public ApplicationEventPublisher applicationEventPublisher;
	private WorkflowMBean functions;

	/**
	 * Tests that comment is required field
	 */
	@Test(expected = IllegalArgumentException.class)
	public void commentRequired() {
		functions.transition(TYPE, WORKFLOW_ID, TRANSITION_ID, null);
	}

	/**
	 * Sets up for test
	 */
	@Before
	public void onsetup() {
		MockitoAnnotations.initMocks(this);
		functions = new WorkflowMBean();
		functions.setApplicationEventPublisher(applicationEventPublisher);
	}

	/**
	 * Tests event is published correctly
	 */
	@Test
	public void testWorkflow() {
		functions.transition(TYPE, WORKFLOW_ID, TRANSITION_ID, COMMENT);

		verify(applicationEventPublisher)
				.publishEvent(new PluralTransitionEvent(TRANSITION_ID, WORKFLOW_ID, TYPE, null,
						Collections.<String, Object>singletonMap(HistoryRecord.COMMENTS_PROPERTY, COMMENT)));
	}

	/**
	 * Tests that transitionId is required field
	 */
	@Test(expected = IllegalArgumentException.class)
	public void transitionIdRequired() {
		functions.transition(TYPE, WORKFLOW_ID, null, COMMENT);
	}

	/**
	 * Tests that type is required field
	 */
	@Test(expected = IllegalArgumentException.class)
	public void typeRequired() {
		functions.transition(null, WORKFLOW_ID, TRANSITION_ID, COMMENT);
	}

	/**
	 * Tests that workflowId is required field
	 */
	@Test(expected = IllegalArgumentException.class)
	public void workflowIdRequired() {
		functions.transition(TYPE, null, TRANSITION_ID, COMMENT);
	}
}
