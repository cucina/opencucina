package org.cucina.cluster.repository;

import java.util.Collection;
import java.util.Map;

import org.cucina.cluster.model.ClusterControl;
import org.springframework.data.repository.Repository;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ClusterControlRepository
    extends Repository<ClusterControl, Long> {
    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName JAVADOC.
     * @param nodeId JAVADOC.
     *
     * @return JAVADOC.
     */
    boolean complete(String eventName, String nodeId);

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName JAVADOC.
     * @param nodeId JAVADOC.
     *
     * @return JAVADOC.
     */
    boolean create(String eventName, String nodeId, Map<Object, Object> properties);

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName JAVADOC.
     *
     * @return JAVADOC.
     */
    boolean deleteByEventName(String eventName);

    /**
     * JAVADOC Method Level Comments
     *
     * @param thisNode JAVADOC.
     *
     * @return JAVADOC.
     */
    Collection<ClusterControl> findCurrentByNotThisNode(String thisNode);

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    ClusterControl find(Long id);

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName JAVADOC.
     *
     * @return JAVADOC.
     */
    ClusterControl findByEventNameAndCurrent(String eventName);

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName JAVADOC.
     * @param nodeId JAVADOC.
     *
     * @return JAVADOC.
     */
    ClusterControl findByEventNameAndNodeIdAndCurrent(String eventName, String nodeId);
}
