package org.cucina.eggtimer.service;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface SchedulingService {
    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     */
    void cancel(String name);

    /**
     * JAVADOC Method Level Comments
     *
     * @param scheduleRequest JAVADOC.
     */
    void schedule(ScheduleRequest scheduleRequest);
}
