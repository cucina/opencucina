package org.cucina.search;

import org.cucina.search.query.SearchBean;
import org.cucina.search.query.projection.HasAttachmentsProjection;


/**
 * Generates a <code>Projection</code> to establish whether or not the
 * history contains an <code>Attachment</code>
 *
 */
public class HasAttachmentsProjectionProvider
    implements ProjectionProvider {
    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     * @param bean JAVADOC.
     */
    @Override
    public void provide(String type, SearchBean bean) {
        bean.addProjection(new HasAttachmentsProjection(bean.getAliasByType().get(type), type));
    }
}
