
package org.cucina.loader;

import org.cucina.core.model.PersistableEntity;



/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class LoadedObjectWrapper {
    private PersistableEntity entity;

    /**
     * Creates a new LoadedObjectWrapper object.
     *
     * @param object JAVADOC.
     */
    public LoadedObjectWrapper(PersistableEntity object) {
        this.entity = object;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public PersistableEntity getEntity() {
        return entity;
    }
}
