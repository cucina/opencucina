
package org.cucina.engine.search;

import org.cucina.engine.service.TransitionsAccessor;
import org.cucina.search.ResultSetModifier;
import org.springframework.beans.factory.FactoryBean;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PermittedTransitionsAppenderFactoryBean
    implements FactoryBean<ResultSetModifier> {
    private TransitionsAccessor transitionsAccessor;
    private boolean permissionsInUse = true;

    /**
     * Creates a new PermittedTransitionsAppenderFactoryBean object.
     *
     * @param permissionsInUse JAVADOC.
     * @param workflowSupportService JAVADOC.
     */
    public PermittedTransitionsAppenderFactoryBean(boolean permissionsInUse,
        TransitionsAccessor transitionsAccessor) {
        this.permissionsInUse = permissionsInUse;
        this.transitionsAccessor = transitionsAccessor;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     *
     * @throws Exception JAVADOC.
     */
    @Override
    public ResultSetModifier getObject()
        throws Exception {
        if (permissionsInUse) {
            return new PermittedTransitionsAppender(transitionsAccessor);
        }

        return new NoObjectCheckPermittedTransitionsAppender(transitionsAccessor);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Class<?> getObjectType() {
        return ResultSetModifier.class;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
