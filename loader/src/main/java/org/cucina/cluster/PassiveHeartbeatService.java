package org.cucina.cluster;

import java.util.HashSet;
import java.util.Set;

import org.cucina.core.service.ScheduleService;
import org.cucina.core.service.ScheduleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;


/**
 * The class allows to use one Scheduler for many events. It starts scheduler on
 * the first eventName and stops it when there is no eventNames left. The
 * scheduler will publish the <code>event</code> to the local
 * ApplicationContext.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PassiveHeartbeatService
    implements HeartbeatService, ApplicationContextAware {
    protected static final String PASSIVE = "passive";
    private static final Logger LOG = LoggerFactory.getLogger(PassiveHeartbeatService.class);
    private ApplicationContext applicationContext;
    private ApplicationEvent event;
    private Integer interval;
    private ScheduleService scheduleService;
    private Set<String> events = new HashSet<String>();
    private boolean running = false;

    /**
     * Creates a new PassiveHeartbeatService object.
     *
     * @param scheduleService
     *            the schedule service to use.
     * @param interval
     *            How often to publish the event in seconds
     *            {@link ScheduleServiceImpl}.
     * @param event
     *            The instance of an event to publish at each scheduled moment.
     */
    public PassiveHeartbeatService(ScheduleService scheduleService, Integer interval,
        ApplicationEvent event) {
        Assert.notNull(scheduleService, "scheduleService is null");
        this.scheduleService = scheduleService;
        Assert.notNull(interval, "interval is null");
        this.interval = interval;
        Assert.notNull(event, "event is null");
        this.event = event;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationContext
     *            JAVADOC.
     *
     * @throws BeansException
     *             JAVADOC.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName
     *            JAVADOC.
     */
    @Override
    public synchronized void start(String eventName, String nodeId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("add heartbeat for '" + eventName + "' [passive:" + nodeId + ":" + eventName +
                "]");
        }

        events.add(eventName);

        if (!running) {
            scheduleService.start(PASSIVE, PASSIVE, interval * 1000,
                new Heartbeat(eventName, nodeId), "sendNotification", null);
            running = true;
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName
     *            JAVADOC.
     */
    @Override
    public void stop(String eventName) {
        boolean ret = events.remove(eventName);

        if (ret) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("removed heartbeat for '" + eventName + "' [passive:" + eventName + "]");
            }
        }

        if (events.isEmpty()) {
            scheduleService.stop(PASSIVE, PASSIVE);
            running = false;

            if (LOG.isDebugEnabled()) {
                LOG.debug("No jobs, not running");
            }
        }
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Override
    public void stopAll() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Stop all: Stopping all jobs");
        }

        events.clear();
        scheduleService.stop(PASSIVE, PASSIVE);
        running = false;
    }

    public class Heartbeat {
        private String eventName;
        private String nodeId;

        public Heartbeat(String eventName, String nodeId) {
            this.eventName = eventName;
            this.nodeId = nodeId;
        }

        public void sendNotification() {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sending a local heartbeat [passive:" + nodeId + ":" + eventName + "]");
            }

            applicationContext.publishEvent(event);
        }
    }
}
