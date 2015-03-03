package org.cucina.email.service;

import java.util.Locale;

import org.springframework.util.Assert;

/**
 * Wrapper for User in order to implement specific methods.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class EmailUserWrapper
    implements EmailUser {
    private EmailUser user;
    private Locale locale;

    /**
     * Creates a new EmailUserWrapper object.
     *
     * @param user
     *            JAVADOC.
     * @param preferenceService
     *            JAVADOC.
     */
    public EmailUserWrapper(EmailUser user, Locale locale) {
        super();
        Assert.notNull(user, "user cannot be null");
        this.user = user;
        this.locale = locale;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isActive() {
        return user.isActive();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String getEmail() {
        return user.getEmail();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Locale getLocale() {
        return locale;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
