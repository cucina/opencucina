package org.cucina.loader.agent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;


/**
 * Tests that CompletedExecutorsCollection functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CompletedAgentCollectionTest {
	@Mock
	private Agent fastExecutor;
	@Mock
	private Agent slowExecutor;
	@Mock
	private ExecutorService executorService;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test that the CompletedExecutorsCollection waits until the nested threads are completed.
	 */
	@Test
	public void testExecuteBlockWait() {
		CompletedAgentCollection collectionImpl = new CompletedAgentCollection();

		collectionImpl.setExecutorService(executorService);

		List<Agent> executors = new ArrayList<Agent>();

		executors.add(slowExecutor);
		executors.add(fastExecutor);
		collectionImpl.setExecutors(executors);

		doAnswer(new Answer<Object>() {
			@Override
			public Void answer(InvocationOnMock invocation)
					throws Throwable {
				@SuppressWarnings("rawtypes")
				FutureTask ft = (FutureTask) invocation.getArguments()[0];

				ft.run();

				return null;
			}
		}).when(executorService).execute(any(FutureTask.class));

		collectionImpl.execute();
	}
}
