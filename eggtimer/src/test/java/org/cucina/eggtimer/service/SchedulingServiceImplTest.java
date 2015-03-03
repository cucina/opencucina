package org.cucina.eggtimer.service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SchedulingServiceImplTest {
    @Mock
    private MessageChannel channel;
    @Mock
    private ScheduledExecutorService executorService;
    private SchedulingServiceImpl service;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new SchedulingServiceImpl(channel);
        service.setExecutorService(executorService);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testCancel() {
        ScheduleRequest request = new ScheduleRequest();

        request.setDelay(100L);
        request.setDestination("destination");
        request.setMessage("message");
        request.setName("name");
        request.setTimeUnit(TimeUnit.MILLISECONDS);

        ScheduledFuture sf = mock(ScheduledFuture.class);

        when(executorService.schedule(any(Runnable.class), eq(100L), eq(TimeUnit.MILLISECONDS)))
            .thenReturn(sf);
        service.schedule(request);
        service.cancel("name");
        verify(sf).cancel(true);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testSchedule() {
        ScheduleRequest request = new ScheduleRequest();

        request.setDelay(100L);
        request.setDestination("destination");
        request.setMessage("message");
        request.setName("name");
        service.schedule(request);

        ArgumentCaptor<Runnable> ac = ArgumentCaptor.forClass(Runnable.class);

        verify(executorService).schedule(ac.capture(), eq(100L), eq(TimeUnit.SECONDS));

        Runnable r = ac.getValue();

        r.run();

        ArgumentCaptor<Message> mc = ArgumentCaptor.forClass(Message.class);

        verify(channel).send(mc.capture());

        Message m = mc.getValue();

        assertEquals("message", m.getPayload());
        assertEquals("destination", m.getHeaders().get("destination"));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testSchedulePeriodic() {
        ScheduleRequest request = new ScheduleRequest();

        request.setDelay(100L);
        request.setDestination("destination");
        request.setMessage("message");
        request.setName("name");
        request.setPeriod(20L);
        service.schedule(request);

        ArgumentCaptor<Runnable> ac = ArgumentCaptor.forClass(Runnable.class);

        verify(executorService)
            .scheduleAtFixedRate(ac.capture(), eq(100L), eq(20L), eq(TimeUnit.SECONDS));

        Runnable r = ac.getValue();

        r.run();

        ArgumentCaptor<Message> mc = ArgumentCaptor.forClass(Message.class);

        verify(channel).send(mc.capture());

        Message m = mc.getValue();

        assertEquals("message", m.getPayload());
        assertEquals("destination", m.getHeaders().get("destination"));
    }
}
