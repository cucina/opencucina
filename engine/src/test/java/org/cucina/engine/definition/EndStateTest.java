package org.cucina.engine.definition;

import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.WorkflowListener;
import org.cucina.engine.testadapters.StandardOutputWorkflowListener;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class EndStateTest {
	/**
	 * Test that if handle is provided the EndState sets it as closed.
	 */
	@Test
	public void testEnterInternalHandleIsClosed() {
		EndStation es = new EndStation();

		ExecutionContext ex = new DefaultExecutionContext(mock(Token.class), null,
				Collections.singletonList((WorkflowListener) new StandardOutputWorkflowListener()),
				null);

		try {
			es.enterInternal(ex);
		} catch (RuntimeException e) {
			fail("Handle present, so RuntimeException should not have been thrown!");
		}
	}

	/**
	 * Test that if no handle is provided the EndState throws a
	 * RuntimeException.
	 */
	@Test
	public void testEnterInternalWithoutHandle() {
		EndStation es = new EndStation();
		ExecutionContext ex = new DefaultExecutionContext(null, null,
				Collections.singletonList((WorkflowListener) new StandardOutputWorkflowListener()),
				null);

		try {
			es.enterInternal(ex);
		} catch (RuntimeException e) {
			return;
		}

		fail("No handle, so RuntimeException should have been thrown!");
	}

	/**
	 * DOCUMENT ME!
	 */

	/*
	 * Test method for
	 * 'org.cucina.opvantage.workflow.definition.EndState.leaveInternal(Transition,
	 * ExecutionContext)'
	 */
	@Test
	public void testLeaveInternal() {
		EndStation es = new EndStation();

		try {
			es.leaveInternal(null, null);
			fail("Should not be able to reach this");
		} catch (IllegalStateException e) {
			// success
		}
	}
}
