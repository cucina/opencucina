package org.cucina.cluster;

import org.cucina.cluster.ClusterServiceImpl.WakeupEvent;
import org.cucina.cluster.event.ClusterHeartbeatEvent;
import org.cucina.cluster.event.ClusterNotificationEvent;
import org.cucina.cluster.event.ClusterRefreshEvent;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ClusterServiceImplListenersTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testHeartbeat() {
        ClusterService clusterService = mock(ClusterService.class);

        ClusterServiceImpl.ClusterHeartbeatEventListener listener = new ClusterServiceImpl.ClusterHeartbeatEventListener(clusterService);
        ClusterHeartbeatEvent event = new ClusterHeartbeatEvent("source", "nodeId");

        listener.onApplicationEvent(event);
        verify(clusterService).resetByHeartbeat("source");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNotification() {
        ClusterService clusterService = mock(ClusterService.class);

        ClusterServiceImpl.ClusterNotificationEventListener listener = new ClusterServiceImpl.ClusterNotificationEventListener(clusterService);
        ClusterNotificationEvent event = new ClusterNotificationEvent("source", "nodeId");

        listener.onApplicationEvent(event);
        verify(clusterService).complete("source");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testRefresh() {
        ClusterService clusterService = mock(ClusterService.class);

        ClusterServiceImpl.ClusterRefreshEventListener listener = new ClusterServiceImpl.ClusterRefreshEventListener(clusterService);
        ClusterRefreshEvent event = new ClusterRefreshEvent("source", "nodeId");

        listener.onApplicationEvent(event);
        verify(clusterService).refresh();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testWakeup() {
        ClusterService clusterService = mock(ClusterService.class);

        ClusterServiceImpl.WakeupEventListener listener = new ClusterServiceImpl.WakeupEventListener(clusterService);
        WakeupEvent event = new WakeupEvent("source");

        listener.onApplicationEvent(event);
        verify(clusterService).checkOutstanding();
    }
}
