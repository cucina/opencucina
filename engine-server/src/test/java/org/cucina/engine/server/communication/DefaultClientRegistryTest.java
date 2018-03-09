package org.cucina.engine.server.communication;

import org.cucina.engine.server.event.RegistrationEvent;
import org.junit.Before;
import org.junit.Test;

import javax.management.ObjectName;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DefaultClientRegistryTest {
	private DefaultClientRegistry registry;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		Map<String, String> appr = new HashMap<String, String>();

		appr.put("applicationName", "client");
		registry = new DefaultClientRegistry(appr);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetDestination() {
		assertEquals("client", registry.getDestination("applicationName").getDestinationName());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testListRegistry()
			throws Exception {
		assertEquals(ObjectName.getInstance("souffle", "\"applicationName\"", "\"jms_//client\""),
				registry.listRegistry()[0]);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testOnApplicationEvent() {
		RegistrationEvent event = new RegistrationEvent("source", "applicationName",
				"destinationName");

		registry.onApplicationEvent(event);
		assertEquals("destinationName",
				registry.getDestination("applicationName").getDestinationName());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testPurge() {
		assertEquals("client", registry.getDestination("applicationName").getDestinationName());
		registry.purge();
		assertNull("Should be null as removed", registry.getDestination("applicationName"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testRemove() {
		assertEquals("client", registry.getDestination("applicationName").getDestinationName());
		registry.remove("applicationName");
		assertNull("Should be null as removed", registry.getDestination("applicationName"));
	}
}
