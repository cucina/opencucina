package org.cucina.engine.server.definition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
public class OperationDescriptorDto
    implements WorkflowElementDescriptor, Map<String, Object> {
    private HashMap<String, Object> properties = new HashMap<String, Object>();

    /**
     * JAVADOC Method Level Comments
     *
     * @param application
     *            JAVADOC.
     */
    public void setApplication(String application) {
        properties.put("application", application);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getApplication() {
        return (String) properties.get("application");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param domainId
     *            JAVADOC.
     */
    public void setDomainId(Object domainId) {
        properties.put("domainId", domainId);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Object getDomainId() {
        return properties.get("domainId");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param domainType
     *            JAVADOC.
     */
    public void setDomainType(String domainType) {
        properties.put("domainType", domainType);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String getDomainType() {
        return (String) properties.get("domainType");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param path
     *            JAVADOC.
     */
    public void setPath(String path) {
        properties.put("path", path);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getPath() {
        return (String) get("path");
    }

    /**
     * Set properties
     *
     * @param properties
     *            HashMap<String, Object>
     */
    public void setProperties(HashMap<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * Get properties
     *
     * @return HashMap<String, Object>
     */
    public HashMap<String, Object> getProperties() {
        return properties;
    }

    /**
     * JAVADOC Method Level Comments
     */
    public void clear() {
        properties.clear();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param key
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param value
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Set<Entry<String, Object>> entrySet() {
        return properties.entrySet();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param key
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public Object get(Object key) {
        return properties.get(key);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Set<String> keySet() {
        return properties.keySet();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param key
     *            JAVADOC.
     * @param value
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public Object put(String key, Object value) {
        return properties.put(key, value);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param m
     *            JAVADOC.
     */
    public void putAll(Map<?extends String, ?extends Object> m) {
        properties.putAll(m);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param key
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public Object remove(Object key) {
        return properties.remove(key);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public int size() {
        return properties.size();
    }

    /**
     * Default toString implementation
     *
     * @return This object as String.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Collection<Object> values() {
        return properties.values();
    }
}
