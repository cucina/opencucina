package org.cucina.cluster;

import org.cucina.cluster.event.ClusterAttemptCompleteEvent;

import org.cucina.loader.agent.AgentRunner;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.verify;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AttemptCompleteRunServiceTest {
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
        AttemptCompleteRunService service = new AttemptCompleteRunService(executorRunner);

        service.onApplicationEvent(new ClusterAttemptCompleteEvent("eventName"));

        verify(executorRunner).run("eventName", null);
    }
}
