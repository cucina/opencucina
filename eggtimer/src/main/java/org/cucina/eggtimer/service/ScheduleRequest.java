package org.cucina.eggtimer.service;

import java.util.concurrent.TimeUnit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Entity
public class ScheduleRequest {
    private Long period;
    private String destination;
    private String message;
    private String name;
    private TimeUnit timeUnit;
    private long delay;
    private long id;

    /**
    * JAVADOC Method Level Comments
    *
    * @param delay JAVADOC.
    */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public long getDelay() {
        return delay;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param destination JAVADOC.
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message JAVADOC.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getMessage() {
        return message;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getName() {
        return name;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param period JAVADOC.
     */
    public void setPeriod(Long period) {
        this.period = period;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Long getPeriod() {
        return period;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param timeUnit JAVADOC.
     */
    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
         * Default toString implementation
         *
         * @return This object as String.
         */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
