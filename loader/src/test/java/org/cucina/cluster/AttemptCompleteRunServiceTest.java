package org.cucina.cluster;

import static org.mockito.Mockito.verify;

import org.cucina.cluster.event.ClusterAttemptCompleteEvent;
import org.cucina.loader.agent.AgentRunner;
import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AttemptCompleteRunServiceTest  {

	@Mock
	AgentRunner executorRunner;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		LoggingEnabler.enableLog(AttemptCompleteRunService.class);
	}

	@Test
	public void testOnApplicationEvent() {

		AttemptCompleteRunService service = new AttemptCompleteRunService(executorRunner);
		service.onApplicationEvent(new ClusterAttemptCompleteEvent("eventName"));
		
		verify(executorRunner).run("eventName", null);
	
	}

}
