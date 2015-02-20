package org.cucina.loader.agent;

import java.util.HashMap;
import java.util.Map;

import org.cucina.core.service.ScheduleService;
import org.springframework.util.Assert;


/**
 * Schedules execution of the delegate agent in given time.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SchedulingAgent
    implements Agent {
    private Agent delegate;
    private Map<String, Object> properties = new HashMap<String, Object>();
    private ScheduleService scheduleService;
    private String groupName = "agent";
    private String name;
    private int intervalInSeconds;

    /**
     * Creates a new SchedulingExecutor object.
     *
     * @param scheduleService JAVADOC.
     * @param delegate JAVADOC.
     * @param name JAVADOC.
     * @param groupName JAVADOC.
     * @param intervalInSeconds JAVADOC.
     */
    public SchedulingAgent(ScheduleService scheduleService, Agent delegate, String name,
        int intervalInSeconds) {
        super();
        this.scheduleService = scheduleService;
        Assert.notNull(this.scheduleService);
        this.delegate = delegate;
        Assert.notNull(this.delegate);
        this.name = name;
        Assert.hasLength(this.name);
        this.intervalInSeconds = intervalInSeconds;
        Assert.isTrue(intervalInSeconds > 0);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param groupName JAVADOC.
     */
    public void setGroupName(String groupName) {
        Assert.hasLength(groupName);
        this.groupName = groupName;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param properties JAVADOC.
     */
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
    * JAVADOC Method Level Comments
    */
    @Override
    public void execute() {
        //in case something died, we need to make sure we don't duplicate
        scheduleService.stop(name, groupName);
        scheduleService.start(name, groupName, intervalInSeconds * 1000, delegate, "execute",
            properties);
    }
}
