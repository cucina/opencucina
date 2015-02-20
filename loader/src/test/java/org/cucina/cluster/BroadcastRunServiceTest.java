package org.cucina.cluster;

import static org.mockito.Mockito.verify;

import org.cucina.cluster.event.ClusterBroadcastEvent;
import org.cucina.loader.agent.AgentRunner;
import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BroadcastRunServiceTest  {

	@Mock
	AgentRunner executorRunner;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		LoggingEnabler.enableLog(BroadcastRunServiceTest.class);
	}

	@Test
	public void testOnApplicationEvent() {

		BroadcastRunService service = new BroadcastRunService(executorRunner);
		service.onApplicationEvent(new ClusterBroadcastEvent("eventName"));
		
		verify(executorRunner).run("eventName", null);
	
	}

}
