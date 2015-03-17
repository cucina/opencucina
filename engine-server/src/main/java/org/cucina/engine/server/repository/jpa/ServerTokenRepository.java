package org.cucina.engine.server.repository.jpa;

import org.cucina.core.model.PersistableEntity;
import org.cucina.engine.repository.jpa.TokenRepositoryImpl;
import org.cucina.engine.server.model.EntityDescriptor;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ServerTokenRepository
    extends TokenRepositoryImpl {
    /**
     * Creates a new ServerTokenRepository object.
     *
     * @param entityManager JAVADOC.
     * @param instanceFactory JAVADOC.
     */
    public ServerTokenRepository() {
        super(null);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    protected Class<?extends PersistableEntity> resolveClass(String applicationType) {
        return EntityDescriptor.class;
    }
}
