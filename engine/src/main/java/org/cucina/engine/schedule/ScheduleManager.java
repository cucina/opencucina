package org.cucina.engine.schedule;

import java.util.Map;


/**
 * All implementation manage workflow scheduling functionality for engine.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface ScheduleManager {
	/**
	 * Create new named schedule with appropriate workflow details and cronExpression.
	 *
	 * @param name           String name of this schedule instance.
	 * @param workflowId     String.
	 * @param transitionId   String.
	 * @param cronExpression String cron expression describing when to run schedule.
	 * @param parameters     optional parameters to pass to workflow execution.
	 */
	void createSchedule(String name, String workflowId, String transitionId, String cronExpression,
						Map<String, Object> parameters);

	/**
	 * Update named schedule with appropriate workflow details and cronExpression.
	 *
	 * @param name           String name of this schedule instance to update.
	 * @param workflowId     String.
	 * @param transitionId   String.
	 * @param cronExpression String cron expression describing when to run schedule.
	 * @param parameters     optional parameters to pass to workflow execution.
	 */
	void updateSchedule(String name, String workflowId, String transitionId, String cronExpression,
						Map<String, Object> parameters);
}
