package org.cucina.search.query.modifier;

import org.cucina.search.query.SearchCriterion;
import org.cucina.security.api.PermissionDto;

import java.util.Collection;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 */
public interface PermissionCriteriaBuilderHelper {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param typeName    JAVADOC.
	 * @param searchAlias JAVADOC.
	 * @param permissions JAVADOC.
	 * @return JAVADOC.
	 */
	SearchCriterion buildClause(String typeName, String searchAlias,
								Collection<PermissionDto> permissions);
}
