package org.cucina.cluster.agent;

import org.cucina.cluster.event.ClusterProcessEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;


/**
 * Test that DelayedClusterProcessEventExecutor functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DelayedClusterProcessEventAgentTest {
	private static final Integer DELAY = Integer.valueOf(1000);
	private static final String NAME = "name";
	@Mock
	private ApplicationContext applicationContext;
	private DelayedClusterProcessEventAgent executor;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		executor = new DelayedClusterProcessEventAgent(DELAY, NAME);
		executor.setApplicationContext(applicationContext);
	}

	/**
	 * Test that it delays correctly.
	 */
	@Test
	public void testDelays() {
		Calendar cal = Calendar.getInstance();

		executor.execute();
		cal.add(Calendar.MILLISECOND, DELAY);

		assertTrue("Should not have run before test start date plus DELAY",
				cal.getTime().compareTo(Calendar.getInstance().getTime()) <= 0);
		cal.add(Calendar.MILLISECOND, DELAY);
		assertTrue("Should have run before test start date plus (DELAY x 2)",
				cal.getTime().compareTo(Calendar.getInstance().getTime()) > 0);

		ArgumentCaptor<ClusterProcessEvent> captor = ArgumentCaptor.forClass(ClusterProcessEvent.class);

		verify(applicationContext).publishEvent(captor.capture());

		assertEquals("Should have published event with name", NAME, captor.getValue().getSource());
	}
}
