package org.cucina.search.query;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.utils.NameUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class AbstractSearchComponent
		implements SearchComponent {
	//This is the alias of the direct parent, i.e. foo.bars.bazs.name, would be bazs or appropriate alias for Baz table
	private Map<String, String> parentAliases;
	private String name;
	private String rootAlias;
	//This is the alias of the property name, i.e. foo.bars.bazs.name, would be name or appropriate alias for name property in select statement
	private String alias;

	/**
	 * Creates a new AbstractSearchComponent object.
	 */
	public AbstractSearchComponent() {
		super();
	}

	/**
	 * Creates a new AbstractSearchComponent object.
	 *
	 * @param name      JAVADOC.
	 * @param rootAlias JAVADOC.
	 */
	public AbstractSearchComponent(String name, String rootAlias) {
		this(name, null, rootAlias);
	}

	/**
	 * Creates a new AbstractSearchComponent object.
	 *
	 * @param name      fully qualified field name, e.g. 'bazs.bars.name'.
	 * @param alias
	 * @param rootAlias alias of root.
	 */
	public AbstractSearchComponent(String name, String alias, String rootAlias) {
		Assert.notNull(name, "Must provide a name value to constructor");
		Assert.notNull(rootAlias, "Must provide a rootAlias value to constructor");
		this.name = name;
		this.rootAlias = rootAlias;

		if (StringUtils.isNotEmpty(alias)) {
			this.alias = alias;
		} else {
			String tempAlias = name;

			this.alias = StringUtils.replace(tempAlias, NameUtils.DEFAULT_SEPARATOR, "");
		}
	}

	public String getAlias() {
		return alias;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public Map<String, List<Join>> getJoins() {
		List<Join> joins = getJoins(name);

		Map<String, List<Join>> joinsByRootAlias = new HashMap<String, List<Join>>();

		joinsByRootAlias.put(rootAlias, joins);

		return joinsByRootAlias;
	}

	/**
	 * Get propertyName in fully qualified form
	 *
	 * @return name String.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public Map<String, String> getParentAliases() {
		return parentAliases;
	}

	/**
	 * Sets the parentAliases. These are the aliases keyed by the parent property name,
	 * e.g. this.that.property, key would be 'that'. If property is on parent, i.e. not
	 * nested, will use the alias provided for the rootAlias.
	 *
	 * @param parentAlias Map<String, String>.
	 */
	@Override
	public void setParentAliases(Map<String, String> parentAliases) {
		this.parentAliases = parentAliases;
	}

	/**
	 * Get rootAlias
	 *
	 * @return rootAlias String
	 */
	public String getRootAlias() {
		return rootAlias;
	}

	/**
	 * Get searchPropertyName which returns current name if same as root table alias, the
	 * {root_alias}.name if no default separator (e.g. name). Then the final two levels if
	 * more than one (e.g. bazs.bars.name would return bars.name).
	 *
	 * @return String searchProperty.
	 */
	public String getSearchPropertyName() {
		Assert.notNull(parentAliases, "parentAlias must be set before calling");
		Assert.isTrue(parentAliases.size() == 1, "Should only be one parentAlias");

		String parentAlias = getParentAlias(name, rootAlias);

		if (name.equals(parentAlias)) {
			return name;
		}

		return getSearchPropertyName(name, parentAlias);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param prop JAVADOC.
	 * @return JAVADOC.
	 */
	protected List<Join> getJoins(String prop) {
		List<Join> joins = new ArrayList<Join>();

		String[] tokens = StringUtils.split(prop, ".");

		if (tokens.length == 1) {
			//No need to add join
			return joins;
		}

		for (int i = 0; i < (tokens.length - 1); i++) {
			String property = tokens[i];

			joins.add(new LeftJoin(property, false));
		}

		return joins;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param propertyName JAVADOC.
	 * @return JAVADOC.
	 */
	protected String getParentAlias(String propertyName, String defaultKey) {
		String[] tokens = StringUtils.split(propertyName, NameUtils.DEFAULT_SEPARATOR);

		String parentAlias = null;

		//If there's only one token, then the rootAlias will be the key to the parentAlias, otherwise, use preceding property in name
		if (tokens.length == 1) {
			parentAlias = parentAliases.get(defaultKey);
		} else {
			parentAlias = parentAliases.get(tokens[tokens.length - 2]);
		}

		Assert.notNull(parentAlias,
				"parentAlias should have been set up for propertyName [" + propertyName +
						"] with default [" + defaultKey + "]");

		return parentAlias;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param propertyName JAVADOC.
	 * @param parentAlias  JAVADOC.
	 * @return JAVADOC.
	 */
	protected String getSearchPropertyName(String propertyName, String parentAlias) {
		String[] tokens = StringUtils.split(propertyName, NameUtils.DEFAULT_SEPARATOR);

		return NameUtils.concat(parentAlias, tokens[tokens.length - 1]);
	}
}
