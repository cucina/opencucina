package org.cucina.cluster;

import org.cucina.cluster.event.ClusterHeartbeatEvent;
import org.cucina.core.service.ScheduleService;
import org.cucina.core.spring.integration.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ActiveHeartbeatService
    implements HeartbeatService, InitializingBean {
    protected static final String ACTIVE = "active";
    private static final Logger LOG = LoggerFactory.getLogger(ActiveHeartbeatService.class);
    private Integer interval;
    private MessagePublisher messagePublisher;
    private ScheduleService scheduleService;

    /**
    * Creates a new ActiveHeartbeatService object.
    *
    * @param jmsSender
    *            JAVADOC.
    * @param scheduleService
    *            JAVADOC.
    * @param interval
    *            JAVADOC.
    */
    public ActiveHeartbeatService(Integer interval) {
        Assert.notNull(interval, "interval is null");
        this.interval = interval;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param messagePublisher JAVADOC.
     */
    public void setMessagePublisher(MessagePublisher messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param scheduleService JAVADOC.
     */
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Override
    public void afterPropertiesSet()
        throws Exception {
        Assert.notNull(messagePublisher, "messagePublisher is null");
        Assert.notNull(scheduleService, "scheduleService is null");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName
     *            JAVADOC.
     */
    @Override
    public void start(String eventName, String nodeId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("starting heartbeat for '" + eventName + "' [active:" + nodeId + ":" +
                eventName + "]");
        }

        scheduleService.start(eventName, ACTIVE, interval * 1000, new Heartbeat(eventName, nodeId),
            "sendNotification", null);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName
     *            JAVADOC.
     */
    @Override
    public void stop(String eventName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("stopping sending heartbeat for '" + eventName + "' [active:" + eventName +
                "]");
        }

        scheduleService.stop(eventName, ACTIVE);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Override
    public void stopAll() {
        //no-op
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
                LOG.debug("Active node sending a cluster heartbeat for event '" + eventName +
                    "' [active:" + nodeId + ":" + eventName + "]");
            }

            messagePublisher.publish(new ClusterHeartbeatEvent(eventName, nodeId));
        }
    }
}
