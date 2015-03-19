package org.cucina.i18n.api;

import java.io.Serializable;

import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
public class ListNodeDto
    implements Serializable {
    private static final long serialVersionUID = -940953750522936803L;
    private Boolean defaultValue;
    private Locale locale;
    @NotNull
    @Size(min = 1)
    private String application;
    @NotNull
    @Size(min = 1)
    private String code;
    private String text;
    @NotNull
    @Size(min = 1)
    private String type;

    /**
     * JAVADOC Method Level Comments
     *
     * @param application JAVADOC.
     */
    public void setApplication(String application) {
        this.application = application;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getApplication() {
        return application;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param code JAVADOC.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getCode() {
        return code;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param defaultValue JAVADOC.
     */
    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Boolean getDefaultValue() {
        return defaultValue;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param locale JAVADOC.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param text JAVADOC.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getText() {
        return text;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getType() {
        return type;
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
