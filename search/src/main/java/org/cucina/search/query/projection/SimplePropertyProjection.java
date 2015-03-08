
package org.cucina.search.query.projection;


/**
 * Implementation of regular projection.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SimplePropertyProjection
    extends AbstractProjection {
    /**
     * Creates a new SimplePropertyProjection object.
     *
     * @param name fully qualified field name, e.g. 'bazs.bars.name'.
     * @param alias projection alias.
     * @param rootAlias alias of 'root' Type.
     */
    public SimplePropertyProjection(String name, String alias, String rootAlias) {
        super(name, alias, rootAlias);
    }

    /**
     * Constructor is used by JSON for constructing new projections.
     */
    @SuppressWarnings("unused")
    private SimplePropertyProjection() {
        super();
    }

    /**
     * Creates projection to use in query
     *
     * @return projection.
     */
    @Override
    public String getProjection() {
        return getSearchPropertyName() + " as " + getAlias();
    }
}
