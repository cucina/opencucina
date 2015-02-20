
package org.cucina.security.access;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.cucina.core.model.PersistableEntity;
import org.cucina.security.model.User;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DummyAccessManager
    implements AccessManager<User> {
    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isAdmin() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param privilegeName JAVADOC.
     * @param typeName JAVADOC.
     * @param propertyValues JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean hasPermission(String privilegeName, String typeName,
        Map<String, Object> propertyValues) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param privilegeName JAVADOC.
     * @param entity JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean hasPermission(String privilegeName, PersistableEntity entity) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
    * JAVADOC Method Level Comments
    *
    * @param privilegeName JAVADOC.
    *
    * @return JAVADOC.
    */
    @Override
    public boolean hasPrivilege(String privilegeName) {
        return true;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param privilegeName JAVADOC.
     * @param filterCurrent JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<User> listActiveUsers(String privilegeName, Boolean filterCurrent) {
        return Collections.emptySet();
    }
}
