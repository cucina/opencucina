package org.cucina.eggtimer.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SchedulingServiceImpl
    implements SchedulingService {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulingServiceImpl.class);
    private Map<String, ScheduledFuture<?>> schedules = new HashMap<String, ScheduledFuture<?>>();
    private MessageChannel channel;
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    /**
    * Creates a new SchedulingServiceImpl object.
    *
    * @param channel JAVADOC.
    */
    public SchedulingServiceImpl(MessageChannel channel) {
        Assert.notNull(channel, "channel is null");
        this.channel = channel;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param executorService JAVADOC.
     */
    public void setExecutorService(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     */
    @Override
    public void cancel(String name) {
        if (StringUtils.isEmpty(name)) {
            LOG.debug("Empty name for cancel");

            return;
        }

        ScheduledFuture<?> sf = schedules.get(name);

        if (sf != null) {
            sf.cancel(true);
            schedules.remove(name);
        } else {
            LOG.debug("No schedule for '" + name + "' found");
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param scheduleRequest JAVADOC.
     */
    @Override
    public void schedule(ScheduleRequest scheduleRequest) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Received request:" + scheduleRequest);
        }

        String destination = scheduleRequest.getDestination();
        String message = scheduleRequest.getMessage();

        if (StringUtils.isEmpty(destination) || StringUtils.isEmpty(message)) {
            LOG.warn("Either destination or message are empty, no scheduling");
        }

        long delay = scheduleRequest.getDelay();
        TimeUnit unit = scheduleRequest.getTimeUnit();

        if (unit == null) {
            unit = TimeUnit.SECONDS;
        }

        Long period = scheduleRequest.getPeriod();
        ScheduledFuture<?> sf;

        if (period == null) {
            sf = executorService.schedule(createCommand(destination, message), delay, unit);
        } else {
            sf = executorService.scheduleAtFixedRate(createCommand(destination, message), delay,
                    period, unit);
        }

        String name = scheduleRequest.getName();

        if (StringUtils.isNotEmpty(name)) {
            schedules.put(name, sf);
        }
    }

    private Runnable createCommand(final String destination, final String messageText) {
        return new Runnable() {
                @Override
                public void run() {
                    Message<String> message = MessageBuilder.withPayload(messageText)
                                                            .setHeader("destination", destination)
                                                            .build();

                    channel.send(message);
                }
            };
    }
}
