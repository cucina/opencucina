package org.cucina.cluster;

import org.cucina.cluster.event.ClusterBroadcastEvent;
import org.cucina.cluster.event.SendBroadcastClusterEvent;
import org.cucina.core.spring.integration.MessagePublisher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ClusterBroadcastServiceImplTest {
	@Mock
	private MessagePublisher messagePublisher;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testHandle() {
		String nodeId = "NODE";
		String eventName = "event";

		ClusterBroadcastServiceImpl impl = new ClusterBroadcastServiceImpl(nodeId);

		impl.setMessagePublisher(messagePublisher);

		impl.handle(new SendBroadcastClusterEvent(eventName));

		ArgumentCaptor<ClusterBroadcastEvent> argument = ArgumentCaptor.forClass(ClusterBroadcastEvent.class);

		verify(messagePublisher).publish(argument.capture());

		assertEquals(eventName, argument.getValue().getSource());
		assertEquals(nodeId, argument.getValue().getNodeId());
		assertNull(argument.getValue().getProperties());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testSunnyDay() {
		String nodeId = "NODE";
		String eventName = "event";

		ClusterBroadcastServiceImpl impl = new ClusterBroadcastServiceImpl(nodeId);

		impl.setMessagePublisher(messagePublisher);

		impl.broadcast(eventName);

		ArgumentCaptor<ClusterBroadcastEvent> argument = ArgumentCaptor.forClass(ClusterBroadcastEvent.class);

		verify(messagePublisher).publish(argument.capture());

		assertEquals(eventName, argument.getValue().getSource());
		assertEquals(nodeId, argument.getValue().getNodeId());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testWithNull() {
		String nodeId = "NODE";
		String eventName = "event";

		ClusterBroadcastServiceImpl impl = new ClusterBroadcastServiceImpl(nodeId);

		impl.setMessagePublisher(messagePublisher);

		impl.broadcast(eventName, null);

		ArgumentCaptor<ClusterBroadcastEvent> argument = ArgumentCaptor.forClass(ClusterBroadcastEvent.class);

		verify(messagePublisher).publish(argument.capture());

		assertEquals(eventName, argument.getValue().getSource());
		assertEquals(nodeId, argument.getValue().getNodeId());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testWithProperties() {
		String nodeId = "NODE";
		String eventName = "event";
		Map<String, Serializable> properties = new HashMap<String, Serializable>();

		properties.put("1", 2L);

		ClusterBroadcastServiceImpl impl = new ClusterBroadcastServiceImpl(nodeId);

		impl.setMessagePublisher(messagePublisher);

		impl.broadcast(eventName, properties);

		ArgumentCaptor<ClusterBroadcastEvent> argument = ArgumentCaptor.forClass(ClusterBroadcastEvent.class);

		verify(messagePublisher).publish(argument.capture());

		assertEquals(eventName, argument.getValue().getSource());
		assertEquals(nodeId, argument.getValue().getNodeId());
		assertEquals(properties, argument.getValue().getProperties());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testonApplicationEvent() {
		String nodeId = "NODE";
		String eventName = "event";

		ClusterBroadcastServiceImpl impl = new ClusterBroadcastServiceImpl(nodeId);

		impl.setMessagePublisher(messagePublisher);

		impl.onApplicationEvent(new SendBroadcastClusterEvent(eventName));

		ArgumentCaptor<ClusterBroadcastEvent> argument = ArgumentCaptor.forClass(ClusterBroadcastEvent.class);

		verify(messagePublisher).publish(argument.capture());

		assertEquals(eventName, argument.getValue().getSource());
		assertEquals(nodeId, argument.getValue().getNodeId());
		assertNull(argument.getValue().getProperties());
	}
}
