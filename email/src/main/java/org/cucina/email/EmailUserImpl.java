
package org.cucina.email;

import java.util.Locale;


/**
 * Simple impl of EmailUser
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class EmailUserImpl
    implements EmailUser {
    private Locale locale;
    private String email;
    private String username;
    private boolean active;

    /**
     * Set active
     *
     * @param active boolean.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
    * Is this user active
    *
    * @return active boolean.
    */
    public boolean isActive() {
        return active;
    }

    /**
     * Set email
     *
     * @param email String.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get email
     *
     * @return email String.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set locale
     *
     * @param locale Locale.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Get locale
     *
     * @return locale Locale.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Set username
     *
     * @param username String.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get username
     *
     * @return username String.
     */
    public String getUsername() {
        return username;
    }
}
