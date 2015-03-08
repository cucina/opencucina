package org.cucina.search.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.cucina.core.model.PersistableEntity;

import org.cucina.search.SavedSearch;
import org.cucina.search.query.SearchBean;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;


/**
 * Saved Search Bean.
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Entity
@Cache(coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Table(name = "SAVED_SEARCH")
public class PersistableSearch
    extends PersistableEntity
    implements SavedSearch {
    private static final long serialVersionUID = 1988438763524055825L;
    private SearchBean searchBean;
    private String name;
    private String owner;
    private String type;
    private boolean isPublic;

    /**
    * Set name of search
    *
    * @param name
    *            String.
    */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get name of search
     *
     * @return name String.
     */
    @Basic(optional = false)
    public String getName() {
        return name;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param owner JAVADOC.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Set isPublic
     *
     * @param isPublic
     *            boolean.
     */
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * Get is public search
     *
     * @return isPublic.
     */
    @Convert("booleanConverter")
    @Column(name = "IS_PUBLIC", columnDefinition = "CHAR(1) not null")
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Set searchBean
     *
     * @param searchBean
     *            SearchBeanor.
     */
    public void setSearchBean(SearchBean searchBean) {
        this.searchBean = searchBean;
    }

    /**
     * Get searchBean
     * @return searchBean SearchBean.
     */
    @Lob
    @Column(name = "MARSHALLED_SEARCH")
    @Converter(converterClass = SearchBeanConverter.class, name = "searchBeanConverter")
    @Convert("searchBeanConverter")
    public SearchBean getSearchBean() {
        return searchBean;
    }

    /**
     * Set type
     *
     * @param type
     *            String.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get type
     *
     * @return type String.
     */
    @Basic(optional = false)
    public String getType() {
        return type;
    }
}
