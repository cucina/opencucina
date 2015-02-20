package org.cucina.cluster;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import org.cucina.cluster.event.ClusterNotificationEvent;
import org.cucina.cluster.repository.ClusterControlRepository;
import org.cucina.core.spring.integration.MessagePublisher;
import org.cucina.loader.agent.AgentRunner;
import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ActiveClusterNodeServiceImplTest {
    private ActiveClusterNodeServiceImpl service;
    @Mock
    private ClusterControlRepository clusterControlRepository;
    @Mock
    private AgentRunner executorRunner;
    @Mock
    private HeartbeatService heartbeatService;
    @Mock
    private MessagePublisher messagePublisher;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        LoggingEnabler.enableLog(ActiveClusterNodeServiceImpl.class);
        service = new ActiveClusterNodeServiceImpl(clusterControlRepository, executorRunner,
                heartbeatService, messagePublisher);
    }

    /**
    * JAVADOC Method Level Comments
    */
    @Test
    public void testExecuteActive() {
        LoggingEnabler.enableLog(ActiveClusterNodeServiceImpl.class);

        when(clusterControlRepository.complete("eventName", "nodeId")).thenReturn(true);

        Map<Object, Object> props = Collections.singletonMap(new Object(), new Object());

        service.executeActive("eventName", "nodeId", props);

        verify(heartbeatService).start("eventName", "nodeId");
        verify(heartbeatService).stop("eventName");
        verify(messagePublisher).publish(any(ClusterNotificationEvent.class));
        verify(executorRunner).run("eventName", props);
    }
}
