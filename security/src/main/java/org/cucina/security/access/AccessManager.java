
package org.cucina.security.access;

import java.util.Collection;
import java.util.Map;

import org.cucina.core.model.PersistableEntity;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * Manager relating to user's permissions.

 *
 */
public interface AccessManager<T extends UserDetails> {
    /**
     * Returns true if the current user has the required admin <code>Privilege</code>
     *
     * @return
     */
    boolean isAdmin();

    /**
     * JAVADOC Method Level Comments
     *
     * @param privilegeName JAVADOC.
     * @param typeName JAVADOC.
     * @param propertyValues JAVADOC.
     *
     * @return JAVADOC.
     */
    boolean hasPermission(String privilegeName, String typeName, Map<String, Object> propertyValues);

    /**
     * JAVADOC Method Level Comments
     *
     * @param privilegeName JAVADOC.
     * @param typeName JAVADOC.
     * @param entity JAVADOC.
     *
     * @return JAVADOC.
     */
    boolean hasPermission(String privilegeName, PersistableEntity entity);

    /**
     * Returns true if the current user has the required Privilege. NB. THIS IS NOT A FULL PERMISSION CHECK
     * PERMISSION CHECKING SHOULD BE DONE IN TERMS OF DOMAIN OBJECT AND DIMENSIONS TOO
     * @param privilegeName
     * @return
     */
    boolean hasPrivilege(String privilegeName);

    /**
     * JAVADOC Method Level Comments
     *
     * @param privilegeName JAVADOC.
     * @param filterCurrent JAVADOC.
     *
     * @return JAVADOC.
     */
    /**
     * Returns the list of <code>UserDetails</code>s available in
     * system.
     * @param privilegeName if provided will filter the users by privilege
     * @param filterCurrent if true then the current <code>UserDetails</code> will not be returned in the list.
     * @return
     */
    Collection<T> listActiveUsers(String privilegeName, Boolean filterCurrent);
}
