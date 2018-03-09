package org.cucina.search.query.modifier;

import org.cucina.search.query.SearchBean;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface PermissionCriteriaBuilder {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param searchBean      JAVADOC.
	 * @param user            JAVADOC.
	 * @param applicationType JAVADOC.
	 * @param searchAlias     JAVADOC.
	 * @param accessLevel     JAVADOC.
	 * @return JAVADOC.
	 */
	SearchBean buildCriteria(SearchBean searchBean, String username, String applicationType,
							 String searchAlias, String accessLevel);
}
