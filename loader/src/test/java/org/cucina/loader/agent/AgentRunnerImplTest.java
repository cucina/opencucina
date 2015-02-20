package org.cucina.loader.agent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.Map;

import org.cucina.core.service.ContextService;
import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Before;
import org.junit.Test;

public class AgentRunnerImplTest {

	@Before
	public void setUp() throws Exception {
	}

		   @Test
		    public void testExecuteActive() {
		        LoggingEnabler.enableLog(AgentRunnerImpl.class);

		        Agent executor = mock(Agent.class);
		        ContextService contextService = mock(ContextService.class);
		        AgentRunnerImpl service = new AgentRunnerImpl(contextService);

		        service.setExecutorRegistry(Collections.singletonMap("eventName", executor));
		        
		        Map<Object, Object> props = Collections.singletonMap(new Object(), new Object());
		        service.run("eventName",  props );

		        verify(contextService).putAll(props);
		        verify(executor).execute();
		        verify(contextService).clear();
		
	}
		   
		   @Test
		    public void testExecuteActiveNoExecutor() {
		        LoggingEnabler.enableLog(AgentRunnerImpl.class);

		        Agent executor = mock(Agent.class);
		        ContextService contextService = mock(ContextService.class);
		        AgentRunnerImpl service = new AgentRunnerImpl(contextService);

		        service.setExecutorRegistry(Collections.singletonMap("eventName", executor));
		        
		        Map<Object, Object> props = Collections.singletonMap(new Object(), new Object());
		        service.run("eventName2",  props );

		        verify(contextService).putAll(props);
		        verify(executor, never()).execute();
		        verify(contextService).clear();
		
	}

}
