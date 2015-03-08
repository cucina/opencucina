package org.cucina.cluster;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Map;

import org.cucina.cluster.PassiveHeartbeatService.Heartbeat;
import org.cucina.core.service.ScheduleService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PassiveHeartbeatServiceTest {
    @Mock
    private ScheduleService scheduleService;

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
    public void testHeartbeat() {
        @SuppressWarnings("serial")
        ApplicationEvent event = new ApplicationEvent(this) {
            };

        PassiveHeartbeatService service = new PassiveHeartbeatService(scheduleService, 1, event);

        ApplicationContext context = mock(ApplicationContext.class);

        service.setApplicationContext(context);

        Heartbeat heartbeat = service.new Heartbeat("eventName", "nodeId");

        heartbeat.sendNotification();
        verify(context, times(1)).publishEvent(event);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testStart() {
        @SuppressWarnings("serial")
        PassiveHeartbeatService service = new PassiveHeartbeatService(scheduleService, 1,
                new ApplicationEvent(this) {
                });

        service.start("eventName", "nodeId");
        verify(scheduleService)
            .start(eq(PassiveHeartbeatService.PASSIVE), eq(PassiveHeartbeatService.PASSIVE),
            eq(1000L), any(Heartbeat.class), eq("sendNotification"), (Map<String, Object>) any());
        service.start("eventName1", "nodeId");
        verifyNoMoreInteractions(scheduleService);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testStartStop() {
        @SuppressWarnings("serial")
        PassiveHeartbeatService service = new PassiveHeartbeatService(scheduleService, 1,
                new ApplicationEvent(this) {
                });

        service.start("eventName", "nodeId");
        service.start("eventName1", "nodeId");
        verify(scheduleService, times(1))
            .start(eq(PassiveHeartbeatService.PASSIVE), eq(PassiveHeartbeatService.PASSIVE),
            eq(1000L), any(Heartbeat.class), eq("sendNotification"), (Map<String, Object>) any());

        service.stop("eventName");
        service.stop("eventName1");

        verify(scheduleService, times(1))
            .stop(eq(PassiveHeartbeatService.PASSIVE), eq(PassiveHeartbeatService.PASSIVE));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testStopAll() {
        @SuppressWarnings("serial")
        PassiveHeartbeatService service = new PassiveHeartbeatService(scheduleService, 1,
                new ApplicationEvent(this) {
                });

        service.stopAll();
        verify(scheduleService)
            .stop(eq(PassiveHeartbeatService.PASSIVE), eq(PassiveHeartbeatService.PASSIVE));
        verifyNoMoreInteractions(scheduleService);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testStopNoEvents() {
        @SuppressWarnings("serial")
        PassiveHeartbeatService service = new PassiveHeartbeatService(scheduleService, 1,
                new ApplicationEvent(this) {
                });

        service.stop("eventName");
        verify(scheduleService)
            .stop(eq(PassiveHeartbeatService.PASSIVE), eq(PassiveHeartbeatService.PASSIVE));
        verifyNoMoreInteractions(scheduleService);
    }
}
