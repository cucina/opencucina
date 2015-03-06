package org.cucina.core.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.TaskScheduler;

import org.cucina.core.testassist.Foo;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ScheduleServiceImplTest {
    private static final String GROUP_NAME = "group";
    @Mock
    private Foo foo;
    private ScheduleServiceImpl service;
    @SuppressWarnings("rawtypes")
    @Mock
    private ScheduledFuture future;
    @Mock
    private TaskScheduler scheduler;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        service = new ScheduleServiceImpl(scheduler);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void testDupeStart()
        throws Exception {
        when(scheduler.scheduleWithFixedDelay(any(Runnable.class), eq(10L))).thenReturn(future);

        service.start("name", GROUP_NAME, 10L, foo, "getValue", new HashMap<String, Object>());

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        verify(scheduler).scheduleWithFixedDelay(runnableCaptor.capture(), eq(10L));

        assertNotNull("Should have created runnable", runnableCaptor.getValue());

        service.start("name", GROUP_NAME, 10L, foo, "getValue", new HashMap<String, Object>());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testStart()
        throws Exception {
        when(scheduler.scheduleAtFixedRate(any(Runnable.class), eq(10L))).thenReturn(future);

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("name", "fred");

        service.start("name", GROUP_NAME, 10L, foo, "getValue", params);

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);

        verify(scheduler).scheduleWithFixedDelay(runnableCaptor.capture(), eq(10L));

        assertNotNull("Should have created runnable", runnableCaptor.getValue());
        runnableCaptor.getValue().run();

        verify(foo).setName("fred");
        verify(foo).getValue();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testStopJob()
        throws Exception {
        when(scheduler.scheduleWithFixedDelay(any(Runnable.class), eq(10L))).thenReturn(future);
        service.start("name", GROUP_NAME, 10L, foo, "hashCode", new HashMap<String, Object>());

        when(future.cancel(false)).thenReturn(true);

        service.stop("name", GROUP_NAME);
        verify(future).cancel(false);
        //Just test that we can reschedule, i.e. future is cleared from cache
        when(scheduler.scheduleWithFixedDelay(any(Runnable.class), eq(10L))).thenReturn(future);
    }
}
