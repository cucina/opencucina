package org.cucina.loader.agent;

import java.util.Collections;
import java.util.Map;

import org.cucina.core.service.ContextService;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AgentRunnerImplTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testExecuteActive() {
        Agent executor = mock(Agent.class);
        ContextService contextService = mock(ContextService.class);
        AgentRunnerImpl service = new AgentRunnerImpl(contextService);

        service.setExecutorRegistry(Collections.singletonMap("eventName", executor));

        Map<Object, Object> props = Collections.singletonMap(new Object(), new Object());

        service.run("eventName", props);

        verify(contextService).putAll(props);
        verify(executor).execute();
        verify(contextService).clear();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testExecuteActiveNoExecutor() {
        Agent executor = mock(Agent.class);
        ContextService contextService = mock(ContextService.class);
        AgentRunnerImpl service = new AgentRunnerImpl(contextService);

        service.setExecutorRegistry(Collections.singletonMap("eventName", executor));

        Map<Object, Object> props = Collections.singletonMap(new Object(), new Object());

        service.run("eventName2", props);

        verify(contextService).putAll(props);
        verify(executor, never()).execute();
        verify(contextService).clear();
    }
}
