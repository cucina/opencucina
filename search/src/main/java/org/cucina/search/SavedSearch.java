
package org.cucina.search;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.cucina.search.query.SearchBean;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */

// these named queries here only as example for interpreting class as they will
// not get loaded by openjpa from interface
@NamedQueries({@NamedQuery(name = SavedSearch.BY_ID_QUERY, query = "select s from SavedSearch s where s.id = ?1")
    , @NamedQuery(name = SavedSearch.BY_NAME_QUERY, query = "select s from SavedSearch s where s.name = ?1 and s.owner = ?2")
    , @NamedQuery(name = SavedSearch.BY_USER_QUERY, query = "select s from SavedSearch s where s.owner = ?1")
    , @NamedQuery(name = SavedSearch.BY_USER_AND_PUBLIC_QUERY, query = "select s from SavedSearch s where (s.public = true) or s.owner = ?1")
})
public interface SavedSearch {
    /** This is a field JAVADOC */
    public static final String BY_ID_QUERY = "SavedSearch.byId";

    /** This is a field JAVADOC */
    public static final String BY_NAME_QUERY = "SavedSearch.byName";

    /** This is a field JAVADOC */
    public static final String BY_USER_QUERY = "SavedSearch.byUser";

    /** This is a field JAVADOC */
    public static final String BY_USER_AND_PUBLIC_QUERY = "SavedSearch.byUserAndPublic";

    /**
     * JAVADOC Method Level Comments
     *
     * @param owner
     *            JAVADOC.
     */
    public void setOwner(String owner);

    /**
     * Get owner
     *
     * @return owner String.
     */
    public String getOwner();

    /**
     * JAVADOC Method Level Comments
     *
     * @param name
     *            JAVADOC.
     */
    void setName(String name);

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    String getName();

    /**
     * JAVADOC Method Level Comments
     *
     * @param isPublic
     *            JAVADOC.
     */
    void setPublic(boolean isPublic);

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    boolean isPublic();

    /**
     * JAVADOC Method Level Comments
     *
     * @param searchBean
     *            JAVADOC.
     */
    void setSearchBean(SearchBean searchBean);

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    SearchBean getSearchBean();

    /**
     * JAVADOC Method Level Comments
     *
     * @param type
     *            JAVADOC.
     */
    void setType(String type);

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    String getType();
}
