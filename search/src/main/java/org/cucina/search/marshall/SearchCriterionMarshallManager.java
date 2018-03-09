package org.cucina.search.marshall;

import org.cucina.search.query.SearchCriterion;

import java.util.Map;


/**
 * Manages marshalling of search criterion. Implementations should be able to handle
 * multiple types.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface SearchCriterionMarshallManager {
	/**
	 * Marshalls according to the type of the criterion.
	 *
	 * @param alias               String.
	 * @param propertyName        String of the form Type.Property.
	 * @param criterion           SearchCriterion.
	 * @param marshalledCriterion Map<String, Object>.
	 */
	void marshall(String alias, String propertyName, SearchCriterion criterion,
				  Map<String, Object> marshalledCriterion);

	/**
	 * Unmarshalls according to the propertyType of the property.
	 *
	 * @param propertyType String.
	 * @param propertyName String.
	 * @param alias        String.
	 * @param rootType     String.
	 * @param rootAlias    String.
	 * @param criteria     Map<String, Object>.
	 * @return SearchCriterion.
	 */
	SearchCriterion unmarshall(String propertyType, String propertyName, String alias,
							   String rootType, String rootAlias, Map<String, Object> criteria);

	/**
	 * Should unmarshall according to the alias.
	 *
	 * @param alias     String.
	 * @param rootType  String.
	 * @param rootAlias String.
	 * @param criteria  Map<String, Object>.
	 * @return SearchCriterion.
	 */
	SearchCriterion unmarshall(String alias, String rootType, String rootAlias,
							   Map<String, Object> criteria);
}
