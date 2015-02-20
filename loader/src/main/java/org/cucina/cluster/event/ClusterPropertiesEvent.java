
package org.cucina.cluster.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class ClusterPropertiesEvent
    extends ClusterControlEvent {
    private static final long serialVersionUID = 4799573844874719475L;
    private Map<Object, Object> properties;

    /**
     * Creates a new ClusterPropertiesEvent object.
     *
     * @param source JAVADOC.
     */
    public ClusterPropertiesEvent(String source) {
        super(source);
    }

    /**
     * Creates a new ClusterPropertiesEvent object.
     *
     * @param source JAVADOC.
     * @param nodeId JAVADOC.
     */
    public ClusterPropertiesEvent(String source, String nodeId) {
        super(source, nodeId);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Map<Object, Object> getProperties() {
        return properties;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param properties JAVADOC.
     */
    public void addProperty(String name, Serializable value) {
        if (properties == null) {
            properties = new HashMap<Object, Object>();
        }

        properties.put(name, value);
    }
}
