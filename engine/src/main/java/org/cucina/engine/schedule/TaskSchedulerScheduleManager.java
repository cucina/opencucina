package org.cucina.engine.schedule;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.engine.event.PluralTransitionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;


/**
 * ScheduleManager that schedules using a ${@link TaskScheduler}. Keeps a handle on the
 * ${@link ScheduleFuture}s created when scheduling in order to update in the future.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class TaskSchedulerScheduleManager
		implements ScheduleManager, ApplicationContextAware, InitializingBean {
	private static final Logger LOG = LoggerFactory.getLogger(TaskSchedulerScheduleManager.class);
	private ApplicationContext applicationContext;
	private Collection<CronSlot> slots;
	private Map<String, ScheduledFuture<?>> schedules = new HashMap<String, ScheduledFuture<?>>();
	private TaskScheduler scheduler;

	/**
	 * Creates a new TaskSchedulerScheduleManager object.
	 *
	 * @param scheduler JAVADOC.
	 * @param slots     JAVADOC.
	 */
	public TaskSchedulerScheduleManager(TaskScheduler scheduler) {
		Assert.notNull(scheduler, "scheduler cannot be null");
		this.scheduler = scheduler;
	}

	/**
	 * Set slots
	 *
	 * @param slots Collection<CronSlot>.
	 */
	public void setSlots(Collection<CronSlot> slots) {
		this.slots = slots;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Override
	public void afterPropertiesSet()
			throws Exception {
		if (CollectionUtils.isNotEmpty(slots)) {
			for (CronSlot slot : slots) {
				createSchedule(slot.getName(), slot.getWorkflowId(), slot.getTransitionId(),
						slot.getCronExpression(), slot.getParameters());
			}
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param applicationContext JAVADOC.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param name           JAVADOC.
	 * @param workflowId     JAVADOC.
	 * @param transitionId   JAVADOC.
	 * @param cronExpression JAVADOC.
	 * @param parameters     JAVADOC.
	 */
	@Override
	public void createSchedule(final String name, final String workflowId,
							   final String transitionId, final String cronExpression, final Map<String, Object> parameters) {
		Assert.notNull(name, "name cannot be null");
		Assert.notNull(workflowId, "workflowId cannot be null");
		Assert.notNull(transitionId, "transitionId cannot be null");
		Assert.notNull(cronExpression, "cronExpression cannot be null");

		if (LOG.isDebugEnabled()) {
			LOG.debug("Creating a new schedule with name [" + name + "], workflowId [" +
					workflowId + "], transition [" + transitionId + "], cron [" + cronExpression +
					"] and params [" + parameters + "]");
		}

		ScheduledFuture<?> future = scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				PluralTransitionEvent event = new PluralTransitionEvent(transitionId,
						workflowId, workflowId, null, parameters);

				applicationContext.publishEvent(event);
			}
		}, new CronTrigger(cronExpression));

		schedules.put(name, future);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Created a new schedule with name [" + name + "], workflowId [" + workflowId +
					"], transition [" + transitionId + "], cron [" + cronExpression + "] and params [" +
					parameters + "]");
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param name           JAVADOC.
	 * @param workflowId     JAVADOC.
	 * @param transitionId   JAVADOC.
	 * @param cronExpression JAVADOC.
	 * @param parameters     JAVADOC.
	 */
	@Override
	public void updateSchedule(String name, String workflowId, String transitionId,
							   String cronExpression, Map<String, Object> parameters) {
		Assert.notNull(name, "name cannot be null");

		ScheduledFuture<?> future = schedules.get(name);

		if (future != null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("There is an existing schedule, so attempting to stop it first");
			}

			future.cancel(false);
		}

		//Once cancelled then create a new one
		createSchedule(name, workflowId, transitionId, cronExpression, parameters);
	}
}
