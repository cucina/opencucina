package org.cucina.engine.server.definition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.engine.definition.AbstractProcessElement;


/**
 *
 *
 * @author vlevine
  */
public class AbstractElementDescriptor
    extends AbstractProcessElement
    implements Map<String, Object> {
    private static final String DOMAIN_ID= "domainId";
    private static final String DOMAIN_TYPE = "domainType";
    private static final String APPLICATION = "application";
    private static final String PATH = "path";
    private HashMap<String, Object> properties = new HashMap<String, Object>();

    /**
     *
     *
     * @param application .
     */
    public void setApplication(String application) {
        properties.put(APPLICATION, application);
    }

    /**
     *
     *
     * @return .
     */
    public String getApplication() {
        return (String) properties.get(APPLICATION);
    }

    /**
     *
     *
     * @param domainId .
     */
    public void setDomainId(String domainId) {
        properties.put(DOMAIN_ID, domainId);
    }

    /**
     *
     *
     * @return .
     */
    public String getDomainId() {
        return (String) properties.get(DOMAIN_ID);
    }

    /**
     *
     *
     * @param domainType .
     */
    public void setDomainType(String domainType) {
        properties.put(DOMAIN_TYPE, domainType);
    }

    /**
     *
     *
     * @return .
     */
    public String getDomainType() {
        return (String) properties.get(DOMAIN_TYPE);
    }

    /**
     *
     *
     * @return .
     */
    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    /**
     *
     *
     * @param path .
     */
    public void setPath(String path) {
        properties.put(PATH, path);
    }

    /**
     *
     *
     * @return .
     */
    public String getPath() {
        return (String) properties.get(PATH);
    }

    /**
     * Set properties
     *
     * @param properties HashMap<String, Object>
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
     *
     */
    @Override
    public void clear() {
        properties.clear();
    }

    /**
     *
     *
     * @param key .
     *
     * @return .
     */
    @Override
    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    /**
     *
     *
     * @param value .
     *
     * @return .
     */
    @Override
    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }

    /**
     *
     *
     * @return .
     */
    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return properties.entrySet();
    }

    /**
     *
     *
     * @param key .
     *
     * @return .
     */
    @Override
    public Object get(Object key) {
        return properties.get(key);
    }

    /**
     *
     *
     * @return .
     */
    @Override
    public Set<String> keySet() {
        return properties.keySet();
    }

    /**
     *
     *
     * @param key .
     * @param value .
     *
     * @return .
     */
    @Override
    public Object put(String key, Object value) {
        return properties.put(key, value);
    }

    /**
     *
     *
     * @param m .
     */
    @Override
    public void putAll(Map<?extends String, ?extends Object> m) {
        properties.putAll(m);
    }

    /**
     *
     *
     * @param key .
     *
     * @return .
     */
    @Override
    public Object remove(Object key) {
        return properties.remove(key);
    }

    /**
     *
     *
     * @return .
     */
    @Override
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
     *
     *
     * @return .
     */
    @Override
    public Collection<Object> values() {
        return properties.values();
    }
}
