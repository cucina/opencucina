package org.cucina.engine.server.definition;

import java.util.HashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CheckDescriptorDto
    implements ProcessElementDto {
    private HashMap<String, Object> properties = new HashMap<String, Object>();
    private Object domainId;
    private String application;
    private String domainType;
    private String path;

    /**
     *
     *
     * @param application .
     */
    public void setApplication(String application) {
        this.application = application;
    }

    /**
     *
     *
     * @return .
     */
    public String getApplication() {
        return application;
    }

    /**
     *
     *
     * @param domainId .
     */
    public void setDomainId(Object domainId) {
        this.domainId = domainId;
    }

    /**
     *
     *
     * @return .
     */
    public Object getDomainId() {
        return domainId;
    }

    /**
     *
     *
     * @param domainType .
     */
    public void setDomainType(String domainType) {
        this.domainType = domainType;
    }

    /**
     *
     *
     * @return .
     */
    public String getDomainType() {
        return domainType;
    }

    /**
     *
     *
     * @param path .
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     *
     *
     * @return .
     */
    public String getPath() {
        return path;
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
     * Default toString implementation
     *
     * @return This object as String.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
