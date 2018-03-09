package org.cucina.cluster;

import org.cucina.loader.agent.Agent;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PassiveNotificationServiceImplTest {
	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testNoExceptionWhenNoRegistry() {
		PassiveNotificationServiceImpl impl = new PassiveNotificationServiceImpl(null);

		impl.notify("anyOldThing");

		impl = new PassiveNotificationServiceImpl(Collections.<String, Agent>emptyMap());
		impl.notify("anyOldOtherThing");
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testSunnyDay() {
		Map<String, Agent> map = new HashMap<String, Agent>();
		Agent executor = mock(Agent.class);

		map.put("this", executor);

		PassiveNotificationServiceImpl impl = new PassiveNotificationServiceImpl(map);

		impl.notify("anyOldThing");
		verify(executor, never()).execute();

		impl.notify("this");

		verify(executor).execute();
	}
}
