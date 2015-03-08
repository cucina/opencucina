package org.cucina.search;

import org.cucina.search.query.SearchBean;


/**
 * Provides additional projections based on the type and
 * the projection group.
 */
public interface ProjectionProvider {
    /**
     * Adds the additional <code>Projection</code>s to the projection list.
     * @param type
     * @param bean
     * @param projectionGroup
     */
    void provide(String type, SearchBean bean);
}
