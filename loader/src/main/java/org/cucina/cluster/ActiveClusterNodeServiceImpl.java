package org.cucina.cluster;

import java.util.Map;

import org.cucina.cluster.event.ClusterNotificationEvent;
import org.cucina.cluster.repository.ClusterControlRepository;
import org.cucina.core.spring.integration.MessagePublisher;
import org.cucina.loader.agent.AgentRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ActiveClusterNodeServiceImpl
    implements ActiveClusterNodeService {
    private static final Logger LOG = LoggerFactory.getLogger(ActiveClusterNodeServiceImpl.class);
    private ClusterControlRepository clusterControlRepository;
    private AgentRunner executorRunner;
    private HeartbeatService activeHeartbeatService;
    private MessagePublisher messagePublisher;

    /**
     * Creates a new ActiveClusterNodeServiceImpl object.
     *
     * @param clusterControlDao
     *            JAVADOC.
     * @param heartbeatService
     *            JAVADOC.
     * @param jmsSender
     *            JAVADOC.
     */
    public ActiveClusterNodeServiceImpl(ClusterControlRepository clusterControlRepository,
        AgentRunner executorRunner, HeartbeatService activeHeartbeatService,
        MessagePublisher messagePublisher) {
        this.clusterControlRepository = clusterControlRepository;
        this.executorRunner = executorRunner;
        this.activeHeartbeatService = activeHeartbeatService;
        this.messagePublisher = messagePublisher;
    }

    /** JAVADOC Method Level Comments
     *
     * @param eventName
     *            JAVADOC.
     * @param nodeId
     *            JAVADOC.
     */
    @Override
    public void executeActive(String eventName, String nodeId, Map<Object, Object> properties) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Starting execution for event [" + eventName + "] on this node " + nodeId +
                " with properties [" + properties + "]");
        }

        // start heartbeat
        activeHeartbeatService.start(eventName, nodeId);

        try {
            executorRunner.run(eventName, properties);

            if (!clusterControlRepository.complete(eventName, nodeId)) {
                LOG.warn("My event control has been removed or completed already:" + eventName);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed executors for event [" + eventName + "] on this node " +
                    nodeId);
            }

            // notify other nodes regardless
            messagePublisher.publish(new ClusterNotificationEvent(eventName, nodeId));
        } catch (RuntimeException e) {
            LOG.warn("Oops", e);
        } finally {
            // stop heartbeat
            activeHeartbeatService.stop(eventName);

            // this one to stop potential memory leak
            // TODO make this functionality in a AOP after as it is useful in a
            // number of places
        }
    }
}
