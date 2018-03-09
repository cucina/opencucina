package org.cucina.search.model;

import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.support.BooleanConverter;
import org.cucina.search.SavedSearch;
import org.cucina.search.query.SearchBean;

import javax.persistence.*;


/**
 * Saved Search Bean.
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Entity
@Cacheable
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
	 * Get name of search
	 *
	 * @return name String.
	 */
	@Basic(optional = false)
	public String getName() {
		return name;
	}

	/**
	 * Set name of search
	 *
	 * @param name String.
	 */
	public void setName(String name) {
		this.name = name;
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
	 * JAVADOC Method Level Comments
	 *
	 * @param owner JAVADOC.
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * Get is public search
	 *
	 * @return isPublic.
	 */
	@Convert(converter = BooleanConverter.class)
	@Column(name = "IS_PUBLIC", columnDefinition = "CHAR(1) not null")
	public boolean isPublic() {
		return isPublic;
	}

	/**
	 * Set isPublic
	 *
	 * @param isPublic boolean.
	 */
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * Get searchBean
	 *
	 * @return searchBean SearchBean.
	 */
	@Lob
	@Column(name = "MARSHALLED_SEARCH")
	@Convert(converter = SearchBeanConverter.class)
	public SearchBean getSearchBean() {
		return searchBean;
	}

	/**
	 * Set searchBean
	 *
	 * @param searchBean SearchBeanor.
	 */
	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
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

	/**
	 * Set type
	 *
	 * @param type String.
	 */
	public void setType(String type) {
		this.type = type;
	}
}
