package org.cucina.cluster;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.cucina.cluster.model.ClusterControl;
import org.cucina.cluster.repository.ClusterControlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class NodeRegisterImpl
    implements NodeRegister {
    private static final Logger LOG = LoggerFactory.getLogger(NodeRegisterImpl.class);
    private static final int TOLERANCE = 0;
    private ClusterControlRepository clusterControlRepository;
    private Map<ClusterControl, Integer> eventsInProgress = new HashMap<ClusterControl, Integer>();
    private int toleranceThreshold = TOLERANCE;

    /**
     * Creates a new NodeRegisterImpl object.
     *
     * @param clusterControlRepository
     *            JAVADOC.
     */
    public NodeRegisterImpl(ClusterControlRepository clusterControlRepository) {
        Assert.notNull(clusterControlRepository, "clusterControlRepository is null");
        this.clusterControlRepository = clusterControlRepository;
    }

    /**
     * How many heartbeats can be missed before this node should consider
     * switching to the active more.
     *
     * @param toleranceThreshold
     *            defaults to 2.
     */
    public void setToleranceThreshold(int toleranceThreshold) {
        this.toleranceThreshold = (toleranceThreshold < 0) ? TOLERANCE : toleranceThreshold;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName
     *            JAVADOC.
     */
    @Transactional
    @Override
    public void add(String eventName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("add: passive monitoring map " + eventsInProgress);
        }

        ClusterControl cuc = findControlByName(eventName);

        if (eventsInProgress.get(cuc) != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Add: Found event " + eventName +
                    " in passive monitoring map, nothing to add, returning. [passive]");
            }

            return;
        }

        if (cuc != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("add: Putting control into passive monitoring map, count is zero: " +
                    cuc + " [passive]");
            }

            eventsInProgress.put(cuc, 0);
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info(
                    "add: Couldn't find control to add to passive monitoring map [passive]. Has task ended?");
            }
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param nodeId
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    @Transactional
    public Collection<ClusterControl> outstandingEvents(String nodeId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("outstandingEvents: passive monitoring map" + eventsInProgress +
                " [passive]");
        }

        Collection<ClusterControl> oe = new HashSet<ClusterControl>();

        for (ClusterControl cc : eventsInProgress.keySet()) {
            if (checkMissingHeartBeat(cc, nodeId)) {
                oe.add(cc);
            }
        }

        return oe;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0 JAVADOC.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<String> refresh(String myNodeId) {
        clear();

        if (LOG.isInfoEnabled()) {
            LOG.info(
                "Performing refresh on passive monitoring map. Clearing and reloading from persistence");
        }

        Collection<ClusterControl> clusterControls = clusterControlRepository.findCurrentByNotThisNode(myNodeId);

        for (ClusterControl clusterControl : clusterControls) {
            if (clusterControl != null) {
                add(clusterControl.getEvent());
            }
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("Added " + clusterControls.size() + " controls to the passive monitoring map");
        }

        return CollectionUtils.collect(clusterControls,
            new Transformer() {
                @Override
                public Object transform(Object arg0) {
                    if (arg0 == null) {
                        return null;
                    }

                    return ((ClusterControl) arg0).getEvent();
                }
            });
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param cuc
     *            JAVADOC.
     */
    @Transactional
    @Override
    public void remove(String eventName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("remove: passive monitoring map " + eventsInProgress + " [passive]");
        }

        ClusterControl cuc = findControlByName(eventName);

        if (cuc != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("remove: Removing entry from passive monitoring map [passive:" +
                    eventName + "]");
            }

            eventsInProgress.remove(cuc);
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info(
                    "remove: Couldn't find control to remove from passive monitoring map for [" +
                    eventName + "]. Has task ended?");
            }
        }
    }

    /**
     * puts this instance into the waiting for notification mode
     *
     * @param cc
     *            JAVADOC.
     */
    @Transactional
    @Override
    public void reset(String eventName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("reset: passive monitoring map " + eventsInProgress + " [passive]");
        }

        ClusterControl cuc = findControlByName(eventName);

        if (cuc != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(
                    "reset: Putting control into passive monitoring map, and resetting count: " +
                    cuc + " [passive:" + eventName + "]");
            }

            eventsInProgress.put(cuc, 0);
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info(
                    "reset: Couldn't find control to reset heartbeat count, task has probably ended");
            }
        }
    }

    /**
     * if a clustercontrol exists in db for this event or this node fails to
     * create one, then remail in passive mode.
     *
     * @param eventName
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    @Transactional
    public boolean switchActivePassive(String eventName, String nodeId,
        Map<Object, Object> properties) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("switchActivePassive:  passive monitoring map " + eventsInProgress +
                " [passive]");
        }

        ClusterControl cc = clusterControlRepository.findByEventNameAndCurrent(eventName);

        if (cc != null) {
            // someone beat us to it
            if (LOG.isDebugEnabled()) {
                LOG.debug("switchActivePassive: ClusterControl already exists:" + cc + "[passive:" +
                    eventName + "]");
            }

            add(eventName);

            return false;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("switchActivePassive: ClusterControl doesn't already exist for " + eventName +
                ", attempting to create");
        }

        if (!(clusterControlRepository.create(eventName, nodeId, properties))) {
            // someone beat us to it
            if (LOG.isInfoEnabled()) {
                LOG.info("switchActivePassive: Failed to create cc");
            }

            reset(eventName);

            return false;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(
                "About to enter active mode, making sure there is no passive record for this event [active:" +
                eventName + "]");
        }

        remove(eventName);

        if (LOG.isDebugEnabled()) {
            LOG.debug("switchActivePassive: Entering active mode  [active:" + eventName + "]");
        }

        return true;
    }

    /**
     * checks whether to switch to active mode
     *
     * @param cuc
     * @return
     */
    private boolean checkMissingHeartBeat(ClusterControl cuc, String nodeId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("checkMissingHeartBeat: " + eventsInProgress);
        }

        String eventName = cuc.getEvent();

        Assert.notNull(eventName, "Invalid ClusterControl - event is null:" + cuc);

        Integer attempts = eventsInProgress.get(cuc);

        if (attempts == null) {
            attempts = 0;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Checking for missing heartbeat for " + cuc + " number of attempts is " +
                attempts);
        }

        if (attempts > toleranceThreshold) {
            ClusterControl current = clusterControlRepository.find(cuc.getId());

            if ((current == null) || current.isComplete()) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(
                        "Tried to take over an event with missing heartbeats from another node, and discovered it had completed or been deleted (id = " +
                        cuc.getId() + "). Not doing anything further [passive:" + nodeId + ":" +
                        eventName + "]");
                }

                eventsInProgress.remove(cuc);

                return false;
            }

            if (LOG.isInfoEnabled()) {
                LOG.info(
                    "Missing heartbeat count has reached threshold, checked db, still hasn't completed so attempting to take over control of this event:" +
                    eventName + "[passive->active:" + nodeId + ":" + eventName + "]");
            }

            // has not been reset, enter into active mode
            // reset db table
            clusterControlRepository.deleteByEventName(eventName);

            return switchActivePassive(eventName, nodeId, cuc.getProperties());
        }

        // set the trap
        eventsInProgress.put(cuc, ++attempts);

        return false;
    }

    private void clear() {
        eventsInProgress.clear();
    }

    private ClusterControl findControlByName(String eventName) {
        // find corresponding event in eventsInProgress
        ClusterControl cuc = null;

        for (ClusterControl cc : eventsInProgress.keySet()) {
            if (cc.getEvent().equals(eventName)) {
                cuc = cc;

                break;
            }
        }

        if (cuc != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("findControlByName: Found info on name " + eventName +
                    " in passive monitoring map [" + cuc + "] [passive:" + eventName + "]");
            }

            return cuc;
        }

        if (LOG.isInfoEnabled()) {
            // perhaps this one is an active node
            LOG.info("findControlByName: There is no local monitoring info on the heartbeat event:" +
                eventName + " in passiveHeartbeatService monitoring map. Trying the db");
        }

        return clusterControlRepository.findByEventNameAndCurrent(eventName);
    }
}
