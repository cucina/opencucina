package org.cucina.cluster;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.cucina.cluster.event.ClusterBroadcastEvent;
import org.cucina.cluster.event.SendBroadcastClusterEvent;
import org.cucina.core.spring.integration.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ClusterBroadcastServiceImpl
    implements ClusterBroadcastService, ApplicationListener<SendBroadcastClusterEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(ClusterBroadcastServiceImpl.class);
    private MessagePublisher messagePublisher;
    private String nodeId;

    /**
     * Creates a new ClusterBroadcastServiceImpl object.
     *
     * @param jmsSender
     *            JAVADOC.
     * @param clusterService
     *            JAVADOC.
     */
    public ClusterBroadcastServiceImpl(String nodeId) {
        Assert.notNull(nodeId, "nodeId should not be null");
        Assert.hasLength(nodeId, "nodeId should have length");
        this.nodeId = nodeId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param messagePublisher JAVADOC.
     */
    public void setMessagePublisher(MessagePublisher messagePublisher) {
        Assert.notNull(messagePublisher, "messagePublisher should not be null");
        this.messagePublisher = messagePublisher;
    }

    /**
    * JAVADOC Method Level Comments
    *
    * @param eventName
    *            JAVADOC.
    */
    @Override
    public void broadcast(String eventName) {
        broadcast(eventName, null);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName
     *            JAVADOC.
     * @param properties
     *            JAVADOC.
     */
    @Override
    public void broadcast(String eventName, Map<String, Serializable> properties) {
        ClusterBroadcastEvent event = new ClusterBroadcastEvent(eventName, nodeId);

        if (MapUtils.isNotEmpty(properties)) {
            for (String key : properties.keySet()) {
                event.addProperty(key, properties.get(key));
            }
        }

        broadcastEvent(event);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param sendEvent
     *            JAVADOC.
     */
    @Override
    public void handle(SendBroadcastClusterEvent sendEvent) {
        broadcast((String) sendEvent.getSource());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0
     *            JAVADOC.
     */
    @Override
    public void onApplicationEvent(SendBroadcastClusterEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                "Received SendBroadcastClusterEvent, converting to ClusterBroadcastEvent to send to rest of cluster");
        }

        handle(event);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param event
     *            JAVADOC.
     */
    protected void broadcastEvent(ClusterBroadcastEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Broadcasting " + event + " to the rest of the cluster");
        }

        messagePublisher.publish(event);
    }
}
