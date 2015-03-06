package org.cucina.cluster;

import java.util.Map;

import org.cucina.cluster.event.ClusterHeartbeatEvent;

import org.cucina.core.service.ScheduleService;
import org.cucina.core.spring.integration.MessagePublisher;

import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ActiveHeartbeatServiceTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void stopAllDoesNothing() {
        MessagePublisher messagePublisher = mock(MessagePublisher.class);

        ScheduleService scheduleService = mock(ScheduleService.class);
        ActiveHeartbeatService service = new ActiveHeartbeatService(5);

        service.setMessagePublisher(messagePublisher);
        service.setScheduleService(scheduleService);
        service.stopAll();
        verifyZeroInteractions(scheduleService);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testStart() {
        ScheduleService scheduleService = mock(ScheduleService.class);
        MessagePublisher messagePublisher = mock(MessagePublisher.class);

        ActiveHeartbeatService service = new ActiveHeartbeatService(5);

        service.setMessagePublisher(messagePublisher);
        service.setScheduleService(scheduleService);

        service.start("eventName", "nodeId");

        ActiveHeartbeatService.Heartbeat hb = service.new Heartbeat("eventName", "nodeId");

        hb.sendNotification();

        verify(messagePublisher).publish(new ClusterHeartbeatEvent("eventName", "nodeId"));
        verify(scheduleService)
            .start(eq("eventName"), eq(ActiveHeartbeatService.ACTIVE), eq(5000L),
            any(ActiveHeartbeatService.Heartbeat.class), eq("sendNotification"),
            eq((Map<String, Object>) null));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testStop() {
        MessagePublisher messagePublisher = mock(MessagePublisher.class);

        ScheduleService scheduleService = mock(ScheduleService.class);
        ActiveHeartbeatService service = new ActiveHeartbeatService(5);

        service.setMessagePublisher(messagePublisher);
        service.setScheduleService(scheduleService);
        service.stop("eventName");
        verify(scheduleService).stop("eventName", ActiveHeartbeatService.ACTIVE);
    }
}
