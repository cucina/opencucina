package org.cucina.security.model;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;


/**
 *
 *
 * @author vlevine
 */
public class Entity {
    @Id
    private BigInteger id;
    @Version
    private Integer version;

    /**
     * Creates a new Entity object.
     */
    public Entity() {
        super();
    }

    /**
     *
     *
     * @param id
     *            .
     */
    public void setId(BigInteger id) {
        this.id = id;
    }

    /**
     *
     *
     * @return .
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Set version
     *
     * @param version
     *            int.
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Get version number
     *
     * @return version.
     */
    public Integer getVersion() {
        return version;
    }
}
