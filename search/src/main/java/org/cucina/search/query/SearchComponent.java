package org.cucina.search.query;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Map;


/**
 * Defines contract for a search component involved in constructing a search
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface SearchComponent {
	/**
	 * Get joins required for this projection
	 *
	 * @return Map<String ,   List < Join>> joins
	 */
	@JsonIgnore
	Map<String, List<Join>> getJoins();

	/**
	 * Get property name, should be fully qualified from root object
	 *
	 * @return JAVADOC.
	 */
	String getName();

	/**
	 * Get parentAliases
	 *
	 * @return parentAliases Map<String, String>.
	 */
	@JsonIgnore
	Map<String, String> getParentAliases();

	/**
	 * Sets the parentAliases. These are the aliases keyed by the parent property name,
	 * e.g. this.that.property, key would be 'that'. If property is on parent, i.e. not
	 * nested, will use the alias provided for the rootAlias.
	 *
	 * @param parentAliases Map<String, String>.
	 */
	void setParentAliases(Map<String, String> parentAliases);

	/**
	 * Get the root alias of the 'root' type
	 *
	 * @return rootAlias String
	 */
	String getRootAlias();

	/**
	 * Alias used to identify this <code>SearchComponent</code>
	 *
	 * @return
	 */
	String getAlias();

	/**
	 * Get the search property name for this Projection
	 *
	 * @return search property name
	 */
	@JsonIgnore
	String getSearchPropertyName();
}
