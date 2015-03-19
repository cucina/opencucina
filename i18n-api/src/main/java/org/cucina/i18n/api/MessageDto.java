package org.cucina.i18n.api;

import java.io.Serializable;

import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class MessageDto
    implements Serializable {
    private static final long serialVersionUID = 6002519996213393349L;
    private Locale locale;
    private Long id;
    @NotNull
    @Size(min = 1)
    private String application;
    @NotNull
    @Size(min = 1)
    private String code;
    private String text;

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
     * @param id JAVADOC.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Long getId() {
        return id;
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
         * Default toString implementation
         *
         * @return This object as String.
         */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
