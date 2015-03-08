package org.cucina.cluster;

import static org.mockito.Mockito.verify;

import org.cucina.cluster.event.ClusterBroadcastEvent;
import org.cucina.loader.agent.AgentRunner;
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
public class BroadcastRunServiceTest {
    @Mock
    AgentRunner executorRunner;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testOnApplicationEvent() {
        BroadcastRunService service = new BroadcastRunService(executorRunner);

        service.onApplicationEvent(new ClusterBroadcastEvent("eventName"));

        verify(executorRunner).run("eventName", null);
    }
}
