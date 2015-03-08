
package org.cucina.search.query;


/**
 * Represents a Join in whichever language implemented for. getJoin should return appropriate join syntax.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface Join {
    /**
     * Get join syntax for query language
     *
     * @return join String.
     */
    String getJoin();

    /**
     * Get property represents
     *
     * @return property String.
     */
    String getProperty();

    /**
     *  If this is a unique join, i.e. should have separate join from others
     *
     * @return unique.
     */
    boolean isUnique();
}
