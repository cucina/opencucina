package org.cucina.loader.agent;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class AgentCollectionTest {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws RuntimeException JAVADOC.
	 */
	@Test(expected = RuntimeException.class)
	public void testExceptionsArePropagated()
			throws RuntimeException {
		Agent executor = mock(Agent.class);
		Router router = mock(Router.class);
		Agent executor2 = mock(Agent.class);

		doThrow(new RuntimeException("blaah")).when(executor).execute();

		AgentCollection collectionImpl = new AgentCollection(Arrays.asList(
				new Agent[]{executor, router, executor2}));

		collectionImpl.execute();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws RuntimeException JAVADOC.
	 */
	@Test(expected = RuntimeException.class)
	public void testExceptionsArePropagatedAlternative()
			throws RuntimeException {
		Agent executor = mock(Agent.class);
		Router router = mock(Router.class);
		Agent executor2 = mock(Agent.class);

		when(router.route()).thenReturn(false);
		doThrow(new RuntimeException("blaah")).when(router).runAlternative();

		AgentCollection collectionImpl = new AgentCollection(Arrays.asList(
				new Agent[]{executor, router, executor2}));

		collectionImpl.execute();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws RuntimeException JAVADOC.
	 */
	@Test(expected = RuntimeException.class)
	public void testExceptionsArePropagatedOutput()
			throws RuntimeException {
		Agent executor = mock(Agent.class);
		Router router = mock(Router.class);
		Agent executor2 = mock(Agent.class);

		when(router.route()).thenReturn(true);
		doThrow(new RuntimeException("blaah")).when(executor2).execute();

		AgentCollection collectionImpl = new AgentCollection(Arrays.asList(
				new Agent[]{executor, router}));

		collectionImpl.setOutput(executor2);
		collectionImpl.execute();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testExecute() {
		Agent executor = mock(Agent.class);

		AgentCollection collectionImpl = new AgentCollection(Collections.singletonList(executor));

		collectionImpl.execute();
		verify(executor).execute();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testRouterReturnsFalse() {
		Agent executor = mock(Agent.class);
		Router router = mock(Router.class);
		Agent executor2 = mock(Agent.class);

		when(router.route()).thenReturn(false);

		Agent outputExecutor = mock(Agent.class);

		AgentCollection collectionImpl = new AgentCollection(Arrays.asList(
				new Agent[]{executor, router, executor2}));

		collectionImpl.setOutput(outputExecutor);

		collectionImpl.execute();
		verify(executor).execute();
		verify(router).route();
		verify(router).runAlternative();
		verify(executor2, never()).execute();
		verify(outputExecutor, never()).execute();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testRouterReturnsTrue() {
		Agent executor = mock(Agent.class);
		Router router = mock(Router.class);
		Agent executor2 = mock(Agent.class);
		Agent outputExecutor = mock(Agent.class);

		when(router.route()).thenReturn(true);

		AgentCollection collectionImpl = new AgentCollection(Arrays.asList(
				new Agent[]{executor, router, executor2}));

		collectionImpl.setOutput(outputExecutor);
		collectionImpl.execute();
		verify(executor).execute();
		verify(router).route();
		verify(executor2).execute();
		verify(outputExecutor).execute();
	}
}
