
package org.cucina.core.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ScheduleServiceImpl
    implements ScheduleService {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleServiceImpl.class);
    private Map<String, ScheduledFuture<?>> schedules = new HashMap<String, ScheduledFuture<?>>();
    private TaskScheduler scheduler;

    /**
     * Creates a new ScheduleServiceImpl object.
     *
     * @param scheduler JAVADOC.
     */
    public ScheduleServiceImpl(TaskScheduler scheduler) {
        Assert.notNull(scheduler, "scheduler is null");
        this.scheduler = scheduler;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     * @param group JAVADOC.
     * @param interval JAVADOC.
     * @param bean JAVADOC.
     * @param method JAVADOC.
     * @param properties JAVADOC.
     */
    @Override
    public void start(String name, String group, long interval, final Object bean,
        final String method, final Map<String, Object> properties) {
        Assert.notNull(name, "name cannot be null");
        Assert.notNull(group, "group cannot be null");
        Assert.notNull(bean, "bean cannot be null");
        Assert.notNull(method, "method cannot be null");

        String identifier = getIdentifier(name, group);

        Assert.isNull(schedules.get(identifier),
            "Cannot schedule with the same name and group as an existing scheduled item with name [" +
            name + "] of group [" + group + "]");

        @SuppressWarnings("rawtypes")
        ScheduledFuture future = scheduler.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // TODO beanwrapperfactory
                            BeanWrapper beanWrapper = new BeanWrapperImpl(bean);

                            if (properties != null) {
                                for (String propName : properties.keySet()) {
                                    beanWrapper.setPropertyValue(propName, properties.get(propName));
                                }
                            }

                            Class<?> clazz = bean.getClass();
                            Method m = clazz.getMethod(method, (Class[]) null);

                            m.invoke(bean, (Object[]) null);
                        } catch (Exception e) {
                            LOG.error("Oops", e);
                        }
                    }
                }, interval);

        schedules.put(identifier, future);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Created new schedule with name [" + name + "], group [" + group +
                "], interval [" + interval + "] on bean of class [" + bean.getClass().getName() +
                "], calling method  [" + method + "] with properties [" + properties + "]");
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     */
    @Override
    public void stop(String name, String group) {
        Assert.notNull(name, "name cannot be null");
        Assert.notNull(group, "group cannot be null");

        String identifier = getIdentifier(name, group);

        ScheduledFuture<?> future = schedules.get(identifier);

        if (future == null) {
            LOG.info("There is no schedule to stop name [" + name + "] of group [" + group + "]");

            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Cancelling schedule name [" + name + "] of group [" + group + "]");
        }

        future.cancel(false);
        schedules.remove(identifier);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Canceled schedule name [" + name + "] of group [" + group + "]");
        }
    }

    private String getIdentifier(String name, String group) {
        return name + group;
    }
}
