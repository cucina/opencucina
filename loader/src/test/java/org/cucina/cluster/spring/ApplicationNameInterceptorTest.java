package org.cucina.cluster.spring;

import org.cucina.cluster.event.ClusterBroadcastEvent;
import org.cucina.cluster.event.ClusterControlEvent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationNameInterceptorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testPreSendMessageOfQMessageChannel() {
		String applicationName = "name";
		ApplicationNameInterceptor interceptor = new ApplicationNameInterceptor(applicationName);
		@SuppressWarnings("unchecked")
		Message<ClusterControlEvent> message = mock(Message.class);
		ClusterBroadcastEvent event = new ClusterBroadcastEvent("BRYAN");

		when(message.getPayload()).thenReturn(event);
		interceptor.preSend(message, null);
		assertEquals(event.getApplicationName(), applicationName);

	}

}
