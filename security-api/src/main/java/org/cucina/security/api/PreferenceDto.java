package org.cucina.security.api;

import java.io.Serializable;


/**
 * 
 *
 * @author vlevine
  */
public class PreferenceDto
    implements Serializable {
    private static final long serialVersionUID = -1219134940169838664L;
    private String name;
    private String value;

    /**
     *
     *
     * @param name .
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     *
     * @return .
     */
    public String getName() {
        return name;
    }

    /**
     *
     *
     * @param value .
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     *
     * @return .
     */
    public String getValue() {
        return value;
    }
}
