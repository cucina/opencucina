package org.cucina.search;

import org.cucina.search.query.SearchBean;

import java.util.Collection;
import java.util.Map;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface SearchBeanFactory {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param applicationType JAVADOC.
	 * @param alias           JAVADOC.
	 * @return JAVADOC.
	 */
	String getPropertyFromAlias(String applicationType, String alias);

	/**
	 * Add a <code>CountProjection</code> for the subjectType.
	 *
	 * @param subjectType
	 * @param search
	 * @param alias       String of the count projection
	 */
	void addCountProjection(String subjectType, SearchBean search, String alias);

	/**
	 * Creates an <code>OrderBy</code> for the property alias and adds it to the
	 * <code>SearchBean</code>
	 *
	 * @param property search property alias which requires sorting
	 * @param asc      direction in which to sort. When true ascending else descending
	 * @param search   the SearchBean to which the order should be added
	 */
	void addOrder(String property, boolean asc, SearchBean search);

	/**
	 * Add a projection for each of the requested projection aliases. Projections will be added in the
	 * same order as that of <code>projections</code>
	 *
	 * @param subjectType
	 * @param search
	 * @param projections
	 * @param projectionGroup - optional parameter to specify which group of projections we want to access.
	 */
	void addProjections(String subjectType, SearchBean search, Collection<String> projections,
						Class<?>... projectionGroup);

	/**
	 * Adds the aliased projections to the <code>SearchBean</code> we do this because we
	 * don't add the projections when the searches are generated. This is because we'll eventually
	 * drive which fields are displayed from the front-end or user's profile.
	 *
	 * @param search
	 * @param projectionGroup - optional parameter to specify which group of projections we want to access
	 */
	void addProjections(String subjectType, SearchBean search, Class<?>... projectionGroup);

	/**
	 * Builds a <code>SearchBean</code> and adds restrictions from the map provided.
	 *
	 * @param subjectType
	 * @param criteria
	 * @return
	 */
	SearchBean buildSearchBean(String subjectType, Map<String, Object> criteria,
							   SearchType... searchType);

	/**
	 * Marshall the <code>SearchBean</code> criteria into a <code>Map</code>
	 * that the front-end will understand.
	 *
	 * @param subjectType
	 * @param criteria
	 * @return
	 */
	Map<String, Object> marshallCriteria(String subjectType, SearchBean criteria);
}
