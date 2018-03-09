package org.cucina.loader.agent;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEvent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class EventListenerAgentDelegatorTest {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEventTypeNull() {
		new EventListenerAgentDelegator(new Agent() {
			@Override
			public void execute() {
				//does nothing
			}
		}, null, "xx");
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEventTypeSourceNull() {
		new EventListenerAgentDelegator(mock(Agent.class), null, null);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testExecutorNull() {
		new EventListenerAgentDelegator(null, org.springframework.context.ApplicationEvent.class,
				"x");
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNulls() {
		new EventListenerAgentDelegator(null, null, null);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testOrder() {
		Agent executor = mock(Agent.class);

		EventListenerAgentDelegator delegator = new EventListenerAgentDelegator(executor,
				TestEvent.class, "x");

		assertEquals(0, delegator.getOrder());
		delegator.setOrder(100);
		assertEquals(100, delegator.getOrder());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testStringWrong() {
		Agent executor = mock(Agent.class);

		EventListenerAgentDelegator delegator = new EventListenerAgentDelegator(executor,
				TestEvent.class, "xxx");

		delegator.onApplicationEvent(new TestEvent("xxy"));
		verify(executor, never());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testSunnyDay() {
		Agent executor = mock(Agent.class);

		//executor.execute();
		EventListenerAgentDelegator delegator = new EventListenerAgentDelegator(executor,
				TestEvent.class, "xxx");

		delegator.onApplicationEvent(new TestEvent("xxx"));
		verify(executor).execute();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testSupportsEventType() {
		Agent executor = mock(Agent.class);

		EventListenerAgentDelegator delegator = new EventListenerAgentDelegator(executor,
				TestEvent.class, "x");

		assertTrue(delegator.supportsEventType(TestEvent.class));
		assertTrue(delegator.supportsEventType(ApplicationEvent.class));
		assertFalse(delegator.supportsEventType(ChildTestEvent.class));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testSupportsSourceType() {
		Agent executor = mock(Agent.class);

		EventListenerAgentDelegator delegator = new EventListenerAgentDelegator(executor,
				TestEvent.class, "x");

		assertTrue(delegator.supportsSourceType(Object.class));
	}

	public static final class ChildTestEvent
			extends TestEvent {
		private static final long serialVersionUID = 1L;

		public ChildTestEvent(String source) {
			super(source);
		}
	}

	public static class TestEvent
			extends ApplicationEvent {
		private static final long serialVersionUID = 1L;

		public TestEvent(String source) {
			super(source);
		}
	}
}
