
package org.cucina.engine.schedule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.cucina.engine.event.PluralTransitionEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TaskScheduleScheduleManagerTest {
    private static final String NAME = "name";
    private static final String WORKFLOW = "workflow";
    private static final String TRANSITION = "transition";
    private static final String CRON = "0 0 */2 * * ?";
    private static final Map<String, Object> PARAMS = new HashMap<String, Object>();
    @Mock
    private ApplicationContext applicationContext;
    @SuppressWarnings("rawtypes")
    @Mock
    private ScheduledFuture future;
    @Mock
    private TaskScheduler taskScheduler;
    private TaskSchedulerScheduleManager manager;

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void createTest() {
        when(taskScheduler.schedule(any(Runnable.class), any(CronTrigger.class))).thenReturn(future);

        manager.createSchedule(NAME, WORKFLOW, TRANSITION, CRON, PARAMS);

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<CronTrigger> triggerCaptor = ArgumentCaptor.forClass(CronTrigger.class);

        verify(taskScheduler).schedule(runnableCaptor.capture(), triggerCaptor.capture());
        verify(applicationContext, never())
            .publishEvent(new PluralTransitionEvent(TRANSITION, WORKFLOW, WORKFLOW, null, PARAMS));

        assertEquals("Incorrect cron", CRON, triggerCaptor.getValue().getExpression());
        assertNotNull("Should have created runnable", runnableCaptor.getValue());
        runnableCaptor.getValue().run();

        verify(applicationContext)
            .publishEvent(new PluralTransitionEvent(TRANSITION, WORKFLOW, WORKFLOW, null, PARAMS));
    }

    /**
     * Name cannot be null
     */
    @Test(expected = IllegalArgumentException.class)
    public void createNameCannotBeNull() {
        manager.createSchedule(null, WORKFLOW, TRANSITION, CRON, PARAMS);
    }

    /**
     * Workflow Id cannot be null
     */
    @Test(expected = IllegalArgumentException.class)
    public void createWorkflowIdCannotBeNull() {
        manager.createSchedule(NAME, null, TRANSITION, CRON, PARAMS);
    }

    /**
     * Transition id cannot be null
     */
    @Test(expected = IllegalArgumentException.class)
    public void createTransitionIdCannotBeNull() {
        manager.createSchedule(NAME, WORKFLOW, null, CRON, PARAMS);
    }

    /**
     * Cron cannot be null
     */
    @Test(expected = IllegalArgumentException.class)
    public void createCronCannotBeNull() {
        manager.createSchedule(NAME, WORKFLOW, TRANSITION, null, PARAMS);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @SuppressWarnings("unchecked")
    @Test
    public void updateTest() {
        when(taskScheduler.schedule(any(Runnable.class), any(CronTrigger.class))).thenReturn(future);

        manager.createSchedule(NAME, WORKFLOW, TRANSITION, CRON, PARAMS);
        verify(future, never()).cancel(false);

        manager.updateSchedule(NAME, WORKFLOW, TRANSITION, CRON, PARAMS);

        verify(future).cancel(false);
        verify(taskScheduler, times(2)).schedule(any(Runnable.class), any(CronTrigger.class));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAfterPropertiesSet()
        throws Exception {
        when(taskScheduler.schedule(any(Runnable.class), any(CronTrigger.class))).thenReturn(future);

        Collection<CronSlot> slots = new ArrayList<CronSlot>();

        CronSlot cronSlot = new CronSlot(NAME, WORKFLOW, TRANSITION, CRON);

        cronSlot.setParameters(new HashMap<String, Object>());
        slots.add(cronSlot);
        
        manager.setSlots(slots);
        manager.afterPropertiesSet();

        verify(taskScheduler).schedule(any(Runnable.class), any(CronTrigger.class));
    }

    /**
     * Sets up for test
     */
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        manager = new TaskSchedulerScheduleManager(taskScheduler);
        manager.setApplicationContext(applicationContext);
        manager.afterPropertiesSet();
    }
}
