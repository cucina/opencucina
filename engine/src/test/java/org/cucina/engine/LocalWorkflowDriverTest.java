package org.cucina.engine;

import org.cucina.engine.definition.Check;
import org.cucina.engine.definition.Operation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class LocalWorkflowDriverTest {
	@Mock
	private Operation action;
	@Mock
	private Check condition;
	@Mock
	private ExecutionContext context;
	private LocalProcessDriver executor;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		executor = new LocalProcessDriver();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testExecute() {
		List<Operation> actions = new ArrayList<Operation>();

		actions.add(action);
		executor.execute(actions, context);
		verify(action).execute(context);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testTest() {
		executor.test(condition, context);
		verify(condition).test(context);
	}
}
