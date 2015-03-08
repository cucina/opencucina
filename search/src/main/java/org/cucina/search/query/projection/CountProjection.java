
package org.cucina.search.query.projection;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CountProjection
    extends AbstractProjection {
    protected static final String COUNT = "count";

    /**
     * Creates a new CountProjection object.
     *
     * @param name JAVADOC.
     * @param alias JAVADOC.
     * @param rootAlias JAVADOC.
     */
    public CountProjection(String name, String alias, String rootAlias) {
        super(name, alias, rootAlias);
    }

    /**
     * Constructor is used by JSON for constructing new projections.
     */
    @SuppressWarnings("unused")
    private CountProjection() {
        super();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isAggregate() {
        return true;
    }

    /**
     * Aggregate should not be part of group by
     */
    @Override
    public boolean isGroupable() {
        return false;
    }

    /**
    * Creates projection to use in query
    *
    * @return projection.
    */
    @Override
    public String getProjection() {
        return COUNT + "(" + getSearchPropertyName() + ") as " + getAlias();
    }
}
